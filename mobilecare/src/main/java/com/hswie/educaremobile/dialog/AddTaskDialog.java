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
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DateTimeConvert;

import java.util.ArrayList;


public class AddTaskDialog extends DialogFragment {
    private static final String TAG = "AddTaskDialog";






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