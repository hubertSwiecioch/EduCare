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
        ResidentsModel.get().getResidentsImages();

        CarerModel.get().setCarers(carerRDH.getAllCarers());
        CarerModel.get().setCurrentCarrer();
        CarerModel.get().setCurrentCarrerTasks();
        CarerModel.get().setCurrentCarrerMessages();
        CarerModel.get().getCarerImages();

        FamilyModel.get().setFamilies(familyRDH.getAllFamilies());




        return null;
    }



}



