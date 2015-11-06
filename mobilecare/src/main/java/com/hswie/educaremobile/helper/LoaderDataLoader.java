package com.hswie.educaremobile.helper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.hswie.educaremobile.api.dao.CarerRDH;
import com.hswie.educaremobile.api.dao.ResidentRDH;

import java.io.IOException;

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

        Log.d(TAG, "testtest: " + CarerModel.get().getCarers().get(0).getFullName());
        Log.d(TAG, "testtest: " + CarerModel.get().getCarers().get(0).getPhoto());

        for ( int i = 0; i<ResidentsModel.get().getResidents().size(); i++){

            String url = ResidentsModel.get().getResidents().get(i).getPhoto();
            try {
                byte[] imageByte = ImageHelper.scaleFromHttp(url,50,50);
                ResidentsModel.get().getResidents().get(i).setPhotoByte(imageByte);
                Log.d(TAG, "GetResidentImageFromHttpSuccess");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for ( int i = 0; i<CarerModel.get().getCarers().size(); i++){

            String url = CarerModel.get().getCarers().get(i).getPhoto();
            try {
                byte[] imageByte = ImageHelper.scaleFromHttp(url,50,50);
                CarerModel.get().getCarers().get(i).setPhotoByte(imageByte);
                Log.d(TAG, "GetCarerImageFromHttpSuccess");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}



