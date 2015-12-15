package com.hswie.educaremobile.helper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.hswie.educaremobile.api.dao.CarerMessageRDH;
import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
import com.hswie.educaremobile.api.dao.FamilyRDH;
import com.hswie.educaremobile.api.dao.ResidentRDH;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.CarerMessage;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.api.pojo.Resident;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hswie on 11/6/2015.
 */
public class LoaderDataLoader extends AsyncTaskLoader {


    private static final String TAG = "LoaderDataLoader";

    public LoaderDataLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {

        ResidentRDH residentRDH = new ResidentRDH();
        CarerRDH carerRDH = new CarerRDH();
        FamilyRDH familyRDH = new FamilyRDH();

        ResidentsModel.get().setResidents(residentRDH.getAllResidents());
        ResidentsModel.get().setResidentsMedicines(residentRDH.getMedicines());
        CarerModel.get().setCarers(carerRDH.getAllCarers());
        FamilyModel.get().setFamilies(familyRDH.getAllFamilies());

        CarerModel.get().setCurrentCarrer();

        CarerTasksRDH carerTasksRDH = new CarerTasksRDH();
        Log.d(TAG, "CurrentCarerPreferencesManager: " + PreferencesManager.getCurrentCarerID());
        Log.d(TAG, "CurrentCarerCarerModel: " + CarerModel.get().getCurrentCarer().getID());

        ArrayList<CarerTask> carerTasks = carerTasksRDH.getCarerTasks(String.valueOf(PreferencesManager.getCurrentCarerID()));
        CarerModel.get().getCurrentCarer().setCarerTasks(carerTasks);

        CarerModel.get().setCurrentCarrerMessages();


        ResidentsModel.get().getResidentsImages();

        CarerModel.get().getCarerImages();


        return null;
    }



}



