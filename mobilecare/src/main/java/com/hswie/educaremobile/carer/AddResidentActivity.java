package com.hswie.educaremobile.carer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.hswie.educaremobile.R;
import com.hswie.educaremobile.helper.FileHelper;
import com.hswie.educaremobile.helper.JsonHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import pl.aprilapps.easyphotopicker.EasyImage;

public class AddResidentActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private static final String TAG = "AddResidentActivity";

    private Calendar myCalendar = Calendar.getInstance();
    private EditText dateOfAdoption, birthDate, getPhoto;
    private Switch switchImage;

    private int currentEditDate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resident);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();


    }

    private void initView() {

        dateOfAdoption = (EditText) findViewById(R.id.date_of_adoption);
        birthDate = (EditText) findViewById(R.id.birth_date);
        getPhoto = (EditText) findViewById(R.id.getPhoto);

        switchImage = (Switch) findViewById(R.id.imageSwitch);
        switchImage.setTextOn(getText(R.string.switch_camera));
        switchImage.setTextOff(getText(R.string.switch_gallery));

        dateOfAdoption.setOnClickListener(setDateListener);
        birthDate.setOnClickListener(setDateListener);

        getPhoto.setOnClickListener(getPhotoListener);


    }


    private View.OnClickListener getPhotoListener= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (switchImage.isChecked())
            EasyImage.openCamera(AddResidentActivity.this);
            else
            EasyImage.openGalleryPicker(AddResidentActivity.this);
        }
    };


    private View.OnClickListener setDateListener= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            new DatePickerDialog(AddResidentActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            currentEditDate = v.getId();
        }
    };



    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        if (currentEditDate == dateOfAdoption.getId())
        dateOfAdoption.setText(sdf.format(myCalendar.getTime()));

        if (currentEditDate == birthDate.getId())
            birthDate.setText(sdf.format(myCalendar.getTime()));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handle the image
                onPhotoReturned(imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource) {

            }
        });
    }

    private void onPhotoReturned(File photoFile) {

        getPhoto.setText(photoFile.getName());
        new UploadPhotoToServer(this).execute(photoFile, photoFile.getName());
    }

    private class UploadPhotoToServer extends AsyncTask<Object, Void, Void>{

        private ProgressDialog dialog;

        public UploadPhotoToServer(AddResidentActivity activity) {
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
            fileHelper.uploadFile((File) params[0]);
            return null;
        }


    }





}
