package com.hswie.educaremobile.dialog;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.adapter.ExpandableListAdapter;
import com.hswie.educaremobile.api.dao.CarerMessageRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.network.NetworkHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddMessageDialog extends DialogFragment {
    private static final String TAG = "AddMessageDialog";


    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private ArrayList<Carer> carers;
    private String carersHeader;

    int carerPosition = -1;




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

                    //Log.d(TAG, "childPosition: " + childPosition);
                    expListView.setSelectedChild(groupPosition, childPosition, true);
                    expListView.setItemChecked(childPosition, true);
                    expListView.collapseGroup(groupPosition);
                    carerPosition = childPosition;


                    carersHeader = carers.get(childPosition).getFullName();
                    prepareListData();
                    //Log.d(TAG, "listDataChild: " + listDataChild.size());
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

                messageContents.setError(null);
                messageTitle.setError(null);



                boolean error = false;

                if (messageContents.getText().toString().length() == 0) {
                    messageContents.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (messageTitle.getText().toString().length() == 0) {
                    messageTitle.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if(carerPosition == -1){
                    Toast.makeText(getContext(), getString(R.string.no_carer_selected), Toast.LENGTH_LONG).show();
                    error = true;
                }

                if(!error) {

                    Long tsLong = System.currentTimeMillis() / 1000;
                    String time = tsLong.toString();


                    ArrayList<String> params = new ArrayList<String>();

                    params.add(messageContents.getText().toString());
                    params.add(time);
                    params.add(CarerModel.get().getCurrentCarer().getID());
                    params.add(messageTitle.getText().toString());
                    params.add(carers.get(carerPosition).getID());

                    new AddCarerMessage(getContext()).execute(params);
                }

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
        private ProgressDialog dialog;

        public AddCarerMessage(Context context) {
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.message_is_current_sending));
            dialog.show();
        }

        @Override
        protected Void doInBackground(Object... params) {
            if(NetworkHelper.isConnectedToNetwork(getContext())) {
                try {
                    CarerMessageRDH carerMessageRDH = new CarerMessageRDH();
                    carerMessageRDH.addCarerMessage((ArrayList<String>) params[0]);
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
            Toast.makeText(getContext(), R.string.send_message_unsuccessful, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Toast.makeText(getContext(), R.string.send_message_successful, Toast.LENGTH_LONG).show();
            onReturnToOverview();
            dismiss();

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