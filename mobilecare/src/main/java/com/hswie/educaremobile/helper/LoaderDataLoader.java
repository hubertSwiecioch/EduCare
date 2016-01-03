package com.hswie.educaremobile.helper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.hswie.educaremobile.BuildConfig;
import com.hswie.educaremobile.api.dao.CarerMessageRDH;
import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.dao.FamilyRDH;
import com.hswie.educaremobile.api.dao.ResidentRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.CarerMessage;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.api.pojo.Family;
import com.hswie.educaremobile.api.pojo.Resident;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hswie on 11/6/2015.
 */
public class LoaderDataLoader extends AsyncTaskLoader {

    private boolean isFamily;


    private static final String TAG = "LoaderDataLoader";

    public LoaderDataLoader(Context context) {
        super(context);
        isFamily = BuildConfig.IS_FAMILY;
    }

    @Override
    public Object loadInBackground() {


            CarerRDH carerRDH = new CarerRDH();
            ResidentRDH residentRDH = new ResidentRDH();
            FamilyRDH familyRDH = new FamilyRDH();
            ResidentsModel.get().setResidents(residentRDH.getAllResidents());
            ResidentsModel.get().setResidentsMedicines(residentRDH.getMedicines());
            ResidentsModel.get().getResidentsImages(getContext());

            CarerModel.get().setCarers(carerRDH.getAllCarers());

        if (!isFamily) {
            CarerModel.get().setCurrentCarrer();
            CarerModel.get().setCurrentCarrerTasks();
            CarerModel.get().setCurrentCarrerMessages();
            CarerModel.get().getCarerImages(getContext());
        }

            FamilyModel.get().setFamilies(familyRDH.getAllFamilies());

        if (isFamily){

            FamilyModel.get().setCurrentFamily();
            Family family = FamilyModel.get().getCurrentFamily();

            PreferencesManager.setCurrentResidentIndex(Integer.parseInt(family.getResidentID()));
            ResidentsModel.get().setCurrentResidentIndex(Integer.parseInt(family.getResidentID()));
            ResidentsModel.get().setCurrentResidentByID(family.getResidentID());

            CarerTasksRDH carerTasksRDH = new CarerTasksRDH();
            ResidentsModel.get().getCurrentResident().setCarerTasks(carerTasksRDH.getResidentTasks(ResidentsModel.get().getCurrentResident().getID()));
        }


       return null;
    }


}



