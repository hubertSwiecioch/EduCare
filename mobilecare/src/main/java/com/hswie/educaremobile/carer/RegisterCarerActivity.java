package com.hswie.educaremobile.carer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.helper.FileHelper;
import com.hswie.educaremobile.helper.ImageHelper;
import com.hswie.educaremobile.helper.JsonHelper;
import com.hswie.educaremobile.helper.PasswordStrengthRules;
import com.hswie.educaremobile.helper.PasswordValidationResult;
import com.hswie.educaremobile.helper.PasswordValidator;
import com.hswie.educaremobile.helper.ToggleUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.datatype.Duration;

import pl.aprilapps.easyphotopicker.EasyImage;

public class RegisterCarerActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private static final String TAG = "RegisterCarerActivity";

    private EditText carerUsername, carerPassword, getPhoto, carerFullName, carerPhoneNumber;

    private Button registerCarerButton;
    private Switch switchImage;

    private File file;

    private ProgressBar passwordStrenghtProgress;
    private PasswordStrengthRules passwordStrengthRules;
    private PasswordValidationResult passwordValidationResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_carer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();


    }

    private void initView() {
        carerUsername = (EditText) findViewById(R.id.carer_username);
        carerPassword = (EditText) findViewById(R.id.carer_password);
        passwordStrenghtProgress = (ProgressBar) findViewById(R.id.signup_password_strenght);

        passwordStrengthRules = (PasswordStrengthRules) findViewById(R.id.signup_password_strenght_content_container);
        carerFullName = (EditText) findViewById(R.id.carer_full_name);
        carerPhoneNumber = (EditText) findViewById(R.id.phone_number);
        getPhoto = (EditText) findViewById(R.id.getPhoto);

        switchImage = (Switch) findViewById(R.id.imageSwitchCarer);
        switchImage.setTextOn(getText(R.string.switch_camera));
        switchImage.setTextOff(getText(R.string.switch_gallery));

        getPhoto.setOnClickListener(getPhotoListener);

        registerCarerButton = (Button) findViewById(R.id.register_carer);
        registerCarerButton.setOnClickListener(registerCarer);


        carerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                passwordValidationResult = PasswordValidator.isValid(carerPassword.getText().toString());

                if (hasFocus) {
                    passwordStrenghtProgress.setVisibility(View.VISIBLE);
                    if (!passwordValidationResult.isPasswordValid())
                        ToggleUtil.toggleContentsDown(getApplicationContext(), passwordStrengthRules.getRelativeLayout());
                } else {
                    ToggleUtil.toggleContentsUp(getApplicationContext(), passwordStrengthRules.getRelativeLayout());
                    passwordStrenghtProgress.setVisibility(View.GONE);
                }
            }
        });

        carerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                passwordValidationResult = PasswordValidator.isValid(carerPassword.getText().toString());
                int progress = passwordValidationResult.getValidCount();

                if (!passwordValidationResult.isPasswordValid())
                    ToggleUtil.toggleContentsDown(getApplicationContext(), passwordStrengthRules.getRelativeLayout());
                else ToggleUtil.toggleContentsUp(getApplicationContext(), passwordStrengthRules.getRelativeLayout());

                if (progress < 3)
                    passwordStrenghtProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                if (progress <= 4 && progress >= 3)
                    passwordStrenghtProgress.getProgressDrawable().setColorFilter(Color.rgb(255, 165, 0), PorterDuff.Mode.SRC_IN);

                if (progress == 5)
                    passwordStrenghtProgress.getProgressDrawable().setColorFilter(Color.rgb(50, 205, 50), PorterDuff.Mode.SRC_IN);

                passwordStrengthRules.setColorOnTextChanged(passwordValidationResult);
                passwordStrenghtProgress.setProgress(progress);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        passwordStrenghtProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        passwordStrenghtProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ToggleUtil.toggleContents(getApplicationContext(), passwordStrengthRules);
            }
        });
    }


    private View.OnClickListener registerCarer= new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            carerUsername.setError(null);
            carerPassword.setError(null);
            carerFullName.setError(null);
            carerPhoneNumber.setError(null);
            getPhoto.setError(null);

            boolean error = false;


            if (carerUsername.getText().toString().length() == 0 || !isEmailValid(carerUsername.getText().toString())) {
                carerUsername.setError(getString(R.string.error_field_required));
                error = true;
            }

            if (carerPassword.getText().toString().length() == 0 || !PasswordValidator.isValid(carerPassword.getText().toString()).isPasswordValid()) {
                carerPassword.setError(getString(R.string.error_field_required));
                error = true;
            }

            if (carerFullName.getText().toString().length() == 0) {
                carerFullName.setError(getString(R.string.error_field_required));
                error = true;
            }

            if (carerPhoneNumber.getText().toString().length() == 0) {
                carerPhoneNumber.setError(getString(R.string.error_field_required));
                error = true;
            }

            if (getPhoto.getText().toString().length() == 0) {
                getPhoto.setError(getString(R.string.error_field_required));
                error = true;
            }



            if(file !=null && !error) {

                Log.d(TAG, "TryRegisterCarerAndUploadPhoto");
                ArrayList<String> params = new ArrayList<>();

                params.add(carerUsername.getText().toString());
                params.add(carerPassword.getText().toString());
                params.add(carerFullName.getText().toString());
                params.add(JsonHelper.HOSTNAME_CARERIMAGE + getPhoto.getText().toString());
                params.add(carerPhoneNumber.getText().toString());

                for (String param:params) {

                    Log.d(TAG, param);
                }


                new UploadPhotoToServer(RegisterCarerActivity.this).execute(file, params);
            }
            else{
            Log.d(TAG, "File = NUll");}

        }
    };


    private View.OnClickListener getPhotoListener= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (switchImage.isChecked())
            EasyImage.openCamera(RegisterCarerActivity.this);
            else
            EasyImage.openGalleryPicker(RegisterCarerActivity.this);
        }
    };

    private boolean isEmailValid(String email) {
        Log.d(TAG, "isEmailValid");
        return email.contains("@");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handle the image

                onPhotoReturned(imageFile.getName());

                try {

                    byte[] resizeImage = ImageHelper.scale(imageFile.getPath(),ImageHelper.AVATAR_SIZE,
                            ImageHelper.AVATAR_SIZE,ImageHelper.AVATAR_QUALITY);
                    FileOutputStream fos = new FileOutputStream(imageFile.getPath());
                    Log.d(TAG, "lengh" + resizeImage.length);
                    fos.write(resizeImage);
                    fos.close();
                }
                catch(FileNotFoundException ex)   {
                    System.out.println("FileNotFoundException : " + ex);
                }
                catch(IOException ioe)  {
                    System.out.println("IOException : " + ioe);
                }


                file =new File(imageFile.getPath());

            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource) {

            }
        });
    }

    private void onPhotoReturned(String name) {

        getPhoto.setText(name);

    }

    private class UploadPhotoToServer extends AsyncTask<Object, Void, Void>{

        private ProgressDialog dialog;

        public UploadPhotoToServer(RegisterCarerActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.carer_is_current_adding));
            dialog.show();
        }

        @Override
        protected Void doInBackground(Object... params) {

            FileHelper fileHelper = new FileHelper();
            CarerRDH carerRDH = new CarerRDH();

            try {
                fileHelper.uploadFile((File) params[0], JsonHelper.PERSON_TYPE_CARER);
                carerRDH.addCarer((ArrayList<String>) params[1]);
            }catch (Exception e){

                cancel(true);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), R.string.register_error, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Toast.makeText(getApplicationContext(), R.string.register_successful, Toast.LENGTH_LONG).show();
            finish();

        }




    }





}
