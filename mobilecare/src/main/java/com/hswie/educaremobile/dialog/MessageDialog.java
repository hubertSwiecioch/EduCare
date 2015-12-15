package com.hswie.educaremobile.dialog;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.hswie.educaremobile.api.pojo.CarerMessage;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.DateTimeConvert;


/**
 *
 *
 * @author hswie
 * @
 */
public class MessageDialog extends DialogFragment {
    private static final String TAG = "ConfirmCallDialog";

    private String title = "";
    private String message = "";
    private String sentBy = "";
    private long date = 0;

    public DismissCallback callback;

    void onReturnToMessagesFragment() {
        callback.dismissMessageDialog();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            title = getArguments().getString("TITLE");
            message = getArguments().getString("BODY");
            sentBy = getArguments().getString("SENTBY");
            date = getArguments().getLong("DATE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_message, container, false);

        TextView titleText = (TextView) rootView.findViewById(R.id.message_title);
        titleText.setText(title);

        TextView bodyText = (TextView) rootView.findViewById(R.id.message_body);
        bodyText.setText(message);
        bodyText.setMovementMethod(new ScrollingMovementMethod());

        TextView dateText = (TextView) rootView.findViewById(R.id.message_date);
        dateText.setText(getString(R.string.date) + " " + DateTimeConvert.getDateTime(getActivity(), date));

        TextView sentByText = (TextView) rootView.findViewById(R.id.message_sent);
        sentByText.setText(getString(R.string.from) + " " + sentBy);

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onReturnToMessagesFragment();
                dismiss();
            }
        });

        return rootView;
    }

    public static MessageDialog newInstance(CarerMessage message){
        Bundle args = new Bundle();
        args.putString("TITLE", message.getTitle());
        args.putString("BODY", message.getMessage());
        args.putString("SENTBY", CarerModel.get().getCarerByID(message.getSentBy()).getFullName());
        args.putLong("DATE", message.getSendDate());

        MessageDialog dialog = new MessageDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public interface DismissCallback {
        void dismissMessageDialog();
    }
}