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
import com.hswie.educaremobile.api.dao.ResidentRDH;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.NetworkHelper;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AddMedicineDialog extends DialogFragment {
    private static final String TAG = "AddMedicineDialog";



    private Calendar myCalendar = Calendar.getInstance();
    private int currentEditDate = -1;

    private long startDateCalendar, endDateCalendar;



    private EditText startDate, endDate,  medicineName, medicineDose;


    public DismissCallback callback;


    void onReturnToOverview() {
        callback.dismissMedicineDialog();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View rootView = inflater.inflate(R.layout.dialog_add_medicine, container, false);

        startDate = (EditText) rootView.findViewById(R.id.medicine_start_date);
        startDate.setOnClickListener(setDateListener);

        endDate = (EditText) rootView.findViewById(R.id.medicine_end_date);
        endDate.setOnClickListener(setDateListener);

        medicineName = (EditText) rootView.findViewById(R.id.new_medicine_name);
        medicineDose = (EditText) rootView.findViewById(R.id.medicine_dose);





        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                medicineName.setError(null);
                medicineDose.setError(null);
                startDate.setError(null);
                endDate.setError(null);



                boolean error = false;

                if (medicineName.getText().toString().length() == 0) {
                    medicineName.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (medicineDose.getText().toString().length() == 0) {
                    medicineDose.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (startDate.getText().toString().length() == 0) {
                    startDate.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (endDate.getText().toString().length() == 0) {
                    endDate.setError(getString(R.string.error_field_required));
                    error = true;
                }



                if(!error) {

                    ArrayList<String> params = new ArrayList<String>();
                    params.add(medicineName.getText().toString());
                    params.add(medicineDose.getText().toString());
                    params.add(ResidentsModel.get().getCurrentResident().getID());
                    params.add(Long.toString(startDateCalendar / 1000));
                    params.add(Long.toString(endDateCalendar / 1000));
                    params.add(CarerModel.get().getCurrentCarer().getID());

                    for (String param : params) {

                        Log.d(TAG, "Dialog param: " + param);
                    }

                    new AddMedicine(getContext()).execute(params);
                }

            }
        });

        return rootView;
    }


    private OnClickListener setDateListener= new OnClickListener() {

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

        if (currentEditDate == startDate.getId()) {
            startDateCalendar = myCalendar.getTimeInMillis();
            startDate.setText(sdf.format(myCalendar.getTime()));
        }

        if (currentEditDate == endDate.getId()) {
            endDateCalendar = myCalendar.getTimeInMillis();
            endDate.setText(sdf.format(myCalendar.getTime()));

        }
    }



    private class AddMedicine extends AsyncTask<Object, Void, Void>{

        private ProgressDialog dialog;

        public AddMedicine(Context context) {
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.adding_medicine));
            dialog.show();
        }


        @Override
        protected Void doInBackground(Object... params) {

            if(NetworkHelper.isConnectedToNetwork(getContext())) {
                try {
                ResidentRDH residentRDH = new ResidentRDH();
                residentRDH.addResidentMedicine((ArrayList<String>) params[0]);
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
            Log.d(TAG, "Exepction");
            Toast.makeText(getActivity(), R.string.adding_medicine_unsuccessful, Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.d(TAG, "noExepction");
            onReturnToOverview();
            dismiss();

        }
    }

    public static AddMedicineDialog newInstance(){
        Bundle args = new Bundle();


        AddMedicineDialog dialog = new AddMedicineDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public interface DismissCallback {
        void dismissMedicineDialog();
    }

}