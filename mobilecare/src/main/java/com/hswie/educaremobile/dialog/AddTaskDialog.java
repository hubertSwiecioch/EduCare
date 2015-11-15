package com.hswie.educaremobile.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.ExpandableListAdapter;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DateTimeConvert;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddTaskDialog extends DialogFragment {
    private static final String TAG = "AddTaskDialog";


    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;



    public DismissCallback callback;

    void onReturnToOverview() {
        callback.dismissTaskDialog();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        View rootView = inflater.inflate(R.layout.dialog_add_carer_task, container, false);


        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if(groupPosition == 0){

                    expListView.setSelectedChild(groupPosition,childPosition,true);
                    expListView.collapseGroup(groupPosition);

                }

                if(groupPosition == 1){

                    expListView.setSelectedChild(groupPosition,childPosition,true);
                    expListView.collapseGroup(groupPosition);

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



            }
        });

        return rootView;
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getString(R.string.carer));
        listDataHeader.add(getString(R.string.resident));

        // Adding child data
        List<String> carersList = new ArrayList<String>();

        for (Carer carer: CarerModel.get().getCarers()) {

            carersList.add(carer.getFullName());
        }


        List<String> residentsList = new ArrayList<String>();

        for (Resident resident: ResidentsModel.get().getResidents()) {

            residentsList.add(resident.getFirstName() + " " + resident.getLastName());
        }


        listDataChild.put(listDataHeader.get(0), carersList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), residentsList);
    }


    private class addTask extends AsyncTask<Object, Void, Void>{


        @Override
        protected Void doInBackground(Object... params) {



            return null;

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