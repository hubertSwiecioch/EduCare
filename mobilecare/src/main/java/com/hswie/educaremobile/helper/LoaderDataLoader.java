package com.hswie.educaremobile.helper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

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

        ResidentsModel.get().setResidents(residentRDH.getAllResidents());

        for ( int i = 0; i<ResidentsModel.get().getResidents().size(); i++){


            String url = ResidentsModel.get().getResidents().get(i).getPhoto();
            try {
                byte[] imageByte = ImageHelper.scaleFromHttp(url,50,50);
                ResidentsModel.get().getResidents().get(i).setPhotoByte(imageByte);
                Log.d(TAG, "GetImageFromHttpSuccess");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}



