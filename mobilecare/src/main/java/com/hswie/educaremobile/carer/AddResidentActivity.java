package com.hswie.educaremobile.carer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.hswie.educaremobile.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddResidentActivity extends AppCompatActivity {

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

    private void initView(){

        dateOfAdoption = (EditText) findViewById(R.id.date_of_adoption);
        birthDate = (EditText) findViewById(R.id.birth_date);

        switchImage = (Switch) findViewById(R.id.imageSwitch);
        switchImage.setTextOn(getText(R.string.switch_camera));
        switchImage.setTextOff(getText(R.string.switch_gallery));

        dateOfAdoption.setOnClickListener(setDateListener);
        birthDate.setOnClickListener(setDateListener);
    }

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




}
