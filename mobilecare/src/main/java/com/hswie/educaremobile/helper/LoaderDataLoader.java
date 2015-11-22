package com.hswie.educaremobile.helper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.hswie.educaremobile.api.dao.CarerMessageRDH;
import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.api.dao.CarerTasksRDH;
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

        ResidentsModel.get().setResidents(residentRDH.getAllResidents());
        CarerModel.get().setCarers(carerRDH.getAllCarers());

        for (Carer carer: CarerModel.get().getCarers()) {

            if(Integer.parseInt(carer.getID()) == PreferencesManager.getCurrentCarerID()){
                CarerModel.get().setCurrentCarerIndex(carer);
                Log.d(TAG, "SetCurrentCarerID: " + CarerModel.get().getCurrentCarer().getID());
            }


        }

        CarerTasksRDH carerTasksRDH = new CarerTasksRDH();
        Log.d(TAG, "CurrentCarerPreferencesManager: " + PreferencesManager.getCurrentCarerID());
        Log.d(TAG, "CurrentCarerCarerModel: " + CarerModel.get().getCurrentCarer().getID());

        ArrayList<CarerTask> carerTasks = carerTasksRDH.getCarerTasks(String.valueOf(PreferencesManager.getCurrentCarerID()));
        CarerModel.get().getCurrentCarer().setCarerTasks(carerTasks);

        CarerMessageRDH carerMessageRDH = new CarerMessageRDH();
        ArrayList<CarerMessage> carerMessages = carerMessageRDH.getCarerMessages(String.valueOf(PreferencesManager.getCurrentCarerID()));
        CarerModel.get().getCurrentCarer().setCarerMessages(carerMessages);


        for ( int i = 0; i<ResidentsModel.get().getResidents().size(); i++){

            String url = ResidentsModel.get().getResidents().get(i).getPhoto();
            try {
                    byte[] imageByte = ImageHelper.scaleFromHttp(url, 100, 100);
                    ResidentsModel.get().getResidents().get(i).setPhotoByte(imageByte);
                    Log.d(TAG, "GetResidentImageFromHttpSuccess");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for ( int i = 0; i<CarerModel.get().getCarers().size(); i++){

            String url = CarerModel.get().getCarers().get(i).getPhoto();
            try {
                    byte[] imageByte = ImageHelper.scaleFromHttp(url, 100, 100);
                    CarerModel.get().getCarers().get(i).setPhotoByte(imageByte);
                    Log.d(TAG, "GetCarerImageFromHttpSuccess");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}



