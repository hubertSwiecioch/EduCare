package com.hswie.educaremobile.helper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.hswie.educaremobile.api.dao.ResidentRDH;

/**
 * Created by hswie on 11/6/2015.
 */
public class LoaderDataLoader extends AsyncTaskLoader {


    public LoaderDataLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {

        ResidentRDH residentRDH = new ResidentRDH();

        ResidentsModel.get().setResidents(residentRDH.getAllResidents());
        return null;
    }
}



