package com.hswie.educaremobile.carer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.helper.FileHelper;
import com.hswie.educaremobile.helper.ImageHelper;
import com.hswie.educaremobile.helper.JsonHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import pl.aprilapps.easyphotopicker.EasyImage;

public class RegisterCarerActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private static final String TAG = "RegisterCarerActivity";

    private EditText carerUsername, carerPassword, getPhoto, carerFullName, carerPhoneNumber;

    private Button registerCarerButton;
    private Switch switchImage;

    private File file;



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
        carerFullName = (EditText) findViewById(R.id.carer_full_name);
        carerPhoneNumber = (EditText) findViewById(R.id.phone_number);
        getPhoto = (EditText) findViewById(R.id.getPhoto);

        switchImage = (Switch) findViewById(R.id.imageSwitchCarer);
        switchImage.setTextOn(getText(R.string.switch_camera));
        switchImage.setTextOff(getText(R.string.switch_gallery));

        getPhoto.setOnClickListener(getPhotoListener);

        registerCarerButton = (Button) findViewById(R.id.register_carer);
        registerCarerButton.setOnClickListener(registerCarer);


    }


    private View.OnClickListener registerCarer= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(file !=null) {

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
            dialog.setMessage("Upload Photo...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Object... params) {

           FileHelper fileHelper = new FileHelper();
            fileHelper.uploadFile((File) params[0], JsonHelper.PERSON_TYPE_CARER);
            CarerRDH carerRDH = new CarerRDH();


            carerRDH.addCarer((ArrayList<String>) params[1]);
            return null;
        }


    }





}
