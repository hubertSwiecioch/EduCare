package com.hswie.educaremobile.dialog;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.ExpandableListAdapter;
import com.hswie.educaremobile.api.dao.CarerMessageRDH;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.helper.CarerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddMessageDialog extends DialogFragment {
    private static final String TAG = "AddTaskDialog";


    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private ArrayList<Carer> carers;
    private String carersHeader;

    int carerPosition;




    private EditText  messageTitle, messageContents;


    public DismissCallback callback;


    void onReturnToOverview() {
        callback.dismissAddMessageDialog();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carers = CarerModel.get().getCarers();

        carersHeader = (getString(R.string.carer));



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
        View rootView = inflater.inflate(R.layout.dialog_add_carer_message, container, false);



        messageTitle = (EditText) rootView.findViewById(R.id.new_carer_message_title);
        messageContents = (EditText) rootView.findViewById(R.id.new_carer_message_contents);


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


                    carersHeader = carers.get(childPosition).getFullName();
                    prepareListData();
                    Log.d(TAG, "listDataChild: " + listDataChild.size());
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

        Button addButton = (Button) rootView.findViewById(R.id.send_button);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Long tsLong = System.currentTimeMillis()/1000;
                String time = tsLong.toString();


                ArrayList<String> params = new ArrayList<String>();

                params.add(messageContents.getText().toString());
                params.add(time);
                params.add(CarerModel.get().getCurrentCarer().getID());
                params.add(messageTitle.getText().toString());
                params.add(carers.get(carerPosition).getID());

                new AddCarerMessage().execute(params);
                dismiss();


            }
        });

        return rootView;
    }



    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(carersHeader);

        // Adding child data

        List<String> carersList = new ArrayList<String>();
        for (Carer carer: carers) {

            carersList.add(carer.getFullName());
        }


        listDataChild.put(listDataHeader.get(0), carersList); // Header, Child data

    }


    private class AddCarerMessage extends AsyncTask<Object, Void, Void>{


        @Override
        protected Void doInBackground(Object... params) {


            CarerMessageRDH carerMessageRDH = new CarerMessageRDH();
            carerMessageRDH.addCarerMessage((ArrayList<String>) params[0]);


            return null;

        }
    }

    public static AddMessageDialog newInstance(){
        Bundle args = new Bundle();


        AddMessageDialog dialog = new AddMessageDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public interface DismissCallback {
        void dismissAddMessageDialog();
    }

}