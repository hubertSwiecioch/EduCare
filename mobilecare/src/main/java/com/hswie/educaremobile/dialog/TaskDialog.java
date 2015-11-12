package com.hswie.educaremobile.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.helper.DateTimeConvert;


public class TaskDialog extends DialogFragment {
    private static final String TAG = "ConfirmCallDialog";

    private String title = "";
    private String message = "";
    private long date = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            title = getArguments().getString("TITLE");
            message = getArguments().getString("BODY");
            date = getArguments().getLong("DATE");
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
                dismiss();
            }
        });

        return rootView;
    }

    public static TaskDialog newInstance(CarerTask task){
        Bundle args = new Bundle();
        args.putString("TITLE", task.getHeader());
        args.putString("BODY", task.getDescription());
        args.putLong("DATE", task.getDate());

        TaskDialog dialog = new TaskDialog();
        dialog.setArguments(args);

        return dialog;
    }
}