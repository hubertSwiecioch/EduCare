package com.hswie.educaremobile.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.ExpandableListAdapter;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DateTimeConvert;
import com.hswie.educaremobile.helper.NetworkHelper;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AddTaskDialog extends DialogFragment {
    private static final String TAG = "AddTaskDialog";


    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private Calendar myCalendar = Calendar.getInstance();
    private int currentEditDate = -1;


    private ArrayList<Carer> carers;
    private ArrayList<Resident> residents;
    private String carersHeader;
    private String residentsHeader;

    int carerPosition = -1;
    int residentPosition = -1;



    private EditText taskDate, taskHeader, taskDescription;


    public DismissCallback callback;


    void onReturnToOverview() {
        callback.dismissTaskDialog();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carers = CarerModel.get().getCarers();
        residents = ResidentsModel.get().getResidents();

        carersHeader = (getString(R.string.carer));
        residentsHeader =(getString(R.string.resident));


        if(getArguments() != null){

        }
    }


    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(TAG, "OnDismiss");
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.dialog_add_carer_task, container, false);

        taskDate = (EditText) rootView.findViewById(R.id.date_task);
        taskDate.setOnClickListener(setDateListener);

        taskHeader = (EditText) rootView.findViewById(R.id.new_task_header);
        taskDescription = (EditText) rootView.findViewById(R.id.new_task_description);


        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if(groupPosition == 0){

                    expListView.setSelectedChild(groupPosition, childPosition, true);
                    expListView.setItemChecked(childPosition, true);
                    expListView.collapseGroup(groupPosition);
                    carerPosition = childPosition;

                    Log.d(TAG, "GroupPosition " + groupPosition + "ChildPosition " + childPosition +
                            "CarerFullName: " + CarerModel.get().getCarers().get(childPosition).getFullName());

                    carersHeader = carers.get(childPosition).getFullName();
                    prepareListData();
                    Log.d(TAG, "listDataChild: " + listDataChild.size());
                    listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);




                }

                if(groupPosition == 1) {

                    expListView.setSelectedChild(groupPosition, childPosition, true);
                    expListView.setItemChecked(childPosition, true);
                    expListView.collapseGroup(groupPosition);
                    residentPosition = childPosition;
                    Log.d(TAG, "GroupPosition " + groupPosition + "ChildPosition " + childPosition +
                            "ResidentFullName : " + ResidentsModel.get().getResidents().get(childPosition).getFirstName());


                    residentsHeader = residents.get(childPosition).getFirstName() + " " +  residents.get(childPosition).getLastName();
                    prepareListData();
                    listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);

                }
                return false;
            }
        });

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);



        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onReturnToOverview();
                dismiss();
            }
        });

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                taskHeader.setError(null);
                taskDescription.setError(null);
                taskDate.setError(null);



                boolean error = false;

                if (taskHeader.getText().toString().length() == 0) {
                    taskHeader.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (taskDescription.getText().toString().length() == 0) {
                    taskDescription.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (taskDate.getText().toString().length() == 0) {
                    taskDate.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if(carerPosition == -1){
                    Toast.makeText(getContext(), getString(R.string.no_carer_selected), Toast.LENGTH_LONG).show();
                    error = true;
                }

                if(residentPosition == -1){
                    Toast.makeText(getContext(), getString(R.string.no_resident_selected), Toast.LENGTH_LONG).show();
                    error = true;
                }


                if(!error) {

                    ArrayList<String> params = new ArrayList<String>();

                    params.add(carers.get(carerPosition).getID());
                    params.add(residents.get(residentPosition).getID());
                    params.add(taskHeader.getText().toString());
                    params.add(Long.toString(myCalendar.getTimeInMillis() / 1000));
                    params.add(taskDescription.getText().toString());

                    new addTask(getContext()).execute(params);


                }

            }
        });

        return rootView;
    }


    private View.OnClickListener setDateListener= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            currentEditDate = v.getId();
        }
    };



    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        if (currentEditDate == taskDate.getId())
            taskDate.setText(sdf.format(myCalendar.getTime()));


    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(carersHeader);
        listDataHeader.add(residentsHeader);

        // Adding child data

        List<String> carersList = new ArrayList<String>();
        for (Carer carer: carers) {

            carersList.add(carer.getFullName());
        }



        List<String> residentsList = new ArrayList<String>();
        for (Resident resident: residents) {

            residentsList.add(resident.getFirstName() + " " + resident.getLastName());
        }


        listDataChild.put(listDataHeader.get(0), carersList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), residentsList);
    }


    private class addTask extends AsyncTask<Object, Void, Void>{

        private ProgressDialog dialog;

        public addTask(Context context) {
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.adding_task));
            dialog.show();
        }


        @Override
        protected Void doInBackground(Object... params) {

            if(NetworkHelper.isConnectedToNetwork(getContext())) {
                try {
                    CarerTasksRDH carerTasksRDH = new CarerTasksRDH();
                    carerTasksRDH.addCarerTask((ArrayList<String>) params[0]);
                } catch (Exception e) {

                    cancel(true);
                }
            }else
            cancel(true);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getContext(), R.string.adding_task_unsuccessful, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Toast.makeText(getContext(), R.string.adding_task_successful, Toast.LENGTH_LONG).show();
            onReturnToOverview();
            dismiss();

        }
    }

    public static AddTaskDialog newInstance(){
        Bundle args = new Bundle();


        AddTaskDialog dialog = new AddTaskDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public interface DismissCallback {
        void dismissTaskDialog();
    }

}