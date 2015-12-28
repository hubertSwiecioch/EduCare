package com.hswie.educaremobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.carer.CarerPanel;
import com.hswie.educaremobile.carer.RegisterCarerActivity;
import com.hswie.educaremobile.helper.CarerModel;
import com.hswie.educaremobile.helper.JsonHelper;
import com.hswie.educaremobile.helper.LoaderDataLoader;
import com.hswie.educaremobile.helper.PreferencesManager;
import com.hswie.educaremobile.network.NetworkChangeReceiver;
import com.hswie.educaremobile.network.NetworkHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public static final String TAG = "LoginActivity";




    private static final int REQUEST_READ_CONTACTS = 0;

    private UserLoginTask mAuthTask = null;


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean isFamily;
    private Button registerCarer;

    private JsonHelper jsonHelper = new JsonHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        isFamily = BuildConfig.IS_FAMILY;
        PreferencesManager.prepare(this);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        registerCarer = (Button) findViewById(R.id.carer_register_button);
        registerCarer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "StartRegisterCarerFragment");
                Intent myIntent = new Intent(LoginActivity.this, RegisterCarerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEdit", false);
                myIntent.putExtras(bundle);
                LoginActivity.this.startActivity(myIntent);


            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkHelper.isConnectedToNetwork(getApplicationContext())) {
                    attemptLogin();
                }else
                    Toast.makeText(getApplicationContext(), "Not connected to Internet", Toast.LENGTH_SHORT).show();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailView.setText("hswie@hswie.com");
        mPasswordView.setText("hubert");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if(isFamily){

            registerCarer.setVisibility(View.INVISIBLE);

        }
    }

    private void populateAutoComplete() {
        Log.d(TAG, "populateAutoComplete");
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        Log.d(TAG, "attemptLogin");
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {

            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        Log.d(TAG, "isEmailValid");
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        Log.d(TAG, "isPasswordValid");
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        Log.d(TAG, "showProgress");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(this,

                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,


                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished");
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.d(TAG, "onLoaderReset");

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        Log.d(TAG, "addEmailsToAutoComplete");
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String TAG = "UserLoginTask";
        private final String mEmail;
        private final String mPassword;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");

            try {
                int success;
                int currentID;


                    Long tsLong = System.currentTimeMillis()/1000;
                    String time = tsLong.toString();

                    List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                    paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_LOGIN));
                    paramss.add(new BasicNameValuePair(JsonHelper.TAG_USERNAME, mEmail));
                    paramss.add(new BasicNameValuePair(JsonHelper.TAG_PASSWORD, mPassword));
                    paramss.add(new BasicNameValuePair(JsonHelper.TAG_ONLINETEST, time));

                    Log.d("request!", "starting");

                    String JSONString = (JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss));
                    JSONObject json = JsonHelper.getJSONObjectFromString(JSONString);


                    Log.d("Login attempt", json.toString());


                    success = json.getInt(JsonHelper.TAG_SUCCESS);
                    currentID = json.getInt(JsonHelper.TAG_ID);

                    if (success == 1) {
                        Log.d("Login Successful!", json.toString());
                        Log.d(TAG, "CurrnetCarerID: " + currentID);
                        PreferencesManager.setCurrentCarerID(currentID);
                        Log.d(TAG, "CurrnetCarerID: " + PreferencesManager.getCurrentCarerID());
                        Log.d(TAG, json.getString(JsonHelper.TAG_MESSAGE));
                        LoaderDataLoader loaderDataLoader = new LoaderDataLoader(getApplicationContext());
                        loaderDataLoader.loadInBackground();

                        return true;
                    } else {
                        Log.d("Login Failure!", json.getString(JsonHelper.TAG_MESSAGE));
                        Log.d(TAG, json.getString(JsonHelper.TAG_MESSAGE));
                        return false;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    return false;

            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d(TAG, "onPostExecute");
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();

                Intent intent = new Intent(LoginActivity.this, CarerPanel.class);
                startActivity(intent);


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled");
            mAuthTask = null;
            showProgress(false);
        }
    }
}

