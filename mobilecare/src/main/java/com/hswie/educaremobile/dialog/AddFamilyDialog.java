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
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.dao.FamilyRDH;
import com.hswie.educaremobile.api.pojo.Resident;
import com.hswie.educaremobile.helper.FamilyModel;
import com.hswie.educaremobile.network.NetworkHelper;
import com.hswie.educaremobile.helper.ResidentsModel;

import java.util.ArrayList;


public class AddFamilyDialog extends DialogFragment {
    private static final String TAG = "AddFamilyDialog";


    private EditText  familyUsername, familyFullName, familyPassword, phoneNumber;


    public DismissCallback callback;


    void onReturnToOverview() {
        callback.dismissFamilyDialog();
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
        View rootView = inflater.inflate(R.layout.dialog_add_family, container, false);

        familyUsername = (EditText) rootView.findViewById(R.id.new_family_user_name);
        familyFullName = (EditText) rootView.findViewById(R.id.family_full_name);
        familyPassword = (EditText) rootView.findViewById(R.id.family_password);
        phoneNumber = (EditText) rootView.findViewById(R.id.family_phone_number);


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

                familyUsername.setError(null);
                familyFullName.setError(null);
                familyPassword.setError(null);
                phoneNumber.setError(null);



                boolean error = false;

                if (familyUsername.getText().toString().length() == 0) {
                    familyUsername.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (familyFullName.getText().toString().length() == 0) {
                    familyFullName.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (familyPassword.getText().toString().length() == 0) {
                    familyPassword.setError(getString(R.string.error_field_required));
                    error = true;
                }

                if (phoneNumber.getText().toString().length() == 0) {
                    phoneNumber.setError(getString(R.string.error_field_required));
                    error = true;
                }



                if(!error) {

                    ArrayList<String> params = new ArrayList<String>();

                    params.add(familyUsername.getText().toString());
                    params.add(familyPassword.getText().toString());
                    params.add(familyFullName.getText().toString());
                    params.add(ResidentsModel.get().getCurrentResident().getID());
                    params.add(phoneNumber.getText().toString());


                    for (String param : params) {

                        Log.d(TAG, "Dialog param: " + param);
                    }

                    new AddFamily(getContext()).execute(params);
                }

            }
        });

        return rootView;
    }



    private class AddFamily extends AsyncTask<Object, Void, Void>{

        private ProgressDialog dialog;

        public AddFamily(Context context) {
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.adding_family));
            dialog.show();
        }


        @Override
        protected Void doInBackground(Object... params) {

            if(NetworkHelper.isConnectedToNetwork(getContext())) {
                try {
                    Resident resident = ResidentsModel.get().getCurrentResident();
                    FamilyRDH familyRDH = new FamilyRDH();
                    familyRDH.addFamily((ArrayList<String>) params[0]);
                    FamilyModel.get().setFamilies(familyRDH.getAllFamilies());
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
            Toast.makeText(getActivity(), getString(R.string.adding_family_unsuccessful), Toast.LENGTH_LONG).show();

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

    public static AddFamilyDialog newInstance(){
        Bundle args = new Bundle();
        AddFamilyDialog dialog = new AddFamilyDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public interface DismissCallback {
        void dismissFamilyDialog();
    }

}