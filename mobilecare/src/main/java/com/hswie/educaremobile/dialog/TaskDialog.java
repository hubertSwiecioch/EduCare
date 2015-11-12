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
import com.hswie.educaremobile.helper.PreferencesManager;

import java.util.ArrayList;


public class TaskDialog extends DialogFragment {
    private static final String TAG = "ConfirmCallDialog";

    private String title = "";
    private String message = "";
    private String id;
    private long date = 0;




    public DismissCallback callback;

    void onReturnToOverview() {
        callback.dismissTaskDialog();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            title = getArguments().getString("TITLE");
            message = getArguments().getString("BODY");
            date = getArguments().getLong("DATE");
            id = getArguments().getString("ID");
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
        View rootView = inflater.inflate(R.layout.dialog_carer_task, container, false);

        TextView titleText = (TextView) rootView.findViewById(R.id.message_title);
        titleText.setText(title);

        TextView bodyText = (TextView) rootView.findViewById(R.id.message_body);
        bodyText.setText(message);
        bodyText.setMovementMethod(new ScrollingMovementMethod());

        TextView dateText = (TextView) rootView.findViewById(R.id.message_date);
        dateText.setText(getString(R.string.date) + " " + DateTimeConvert.getDateTime(getActivity(), date));

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onReturnToOverview();
                dismiss();
            }
        });

        Button doneButton = (Button) rootView.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> params = new ArrayList<String>();

                params.add(id);
                params.add("1");


                new setIsDone().execute(params);

                onReturnToOverview();
                dismiss();
            }
        });

        return rootView;
    }


    private class setIsDone extends AsyncTask<Object, Void, Void>{


        @Override
        protected Void doInBackground(Object... params) {

            CarerTasksRDH carerTasksRDH = new CarerTasksRDH();
            carerTasksRDH.setIsDone((ArrayList<String>) params[0]);
            CarerModel.get().getCurrentCarer().setCarerTasks(carerTasksRDH.getCarerTasks(CarerModel.get().getCurrentCarer().getID()));

            return null;

        }
    }

    public static TaskDialog newInstance(CarerTask task){
        Bundle args = new Bundle();
        args.putString("TITLE", task.getHeader());
        args.putString("BODY", task.getDescription());
        args.putLong("DATE", task.getDate());
        args.putString("ID", task.getId());

        TaskDialog dialog = new TaskDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public interface DismissCallback {
        void dismissTaskDialog();
    }

}