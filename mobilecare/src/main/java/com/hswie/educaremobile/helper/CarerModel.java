package com.hswie.educaremobile.helper;

import android.util.Log;

import com.hswie.educaremobile.api.pojo.Carer;


import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class CarerModel {

    private static final String TAG = "CarerModel";
    private static CarerModel carerModel = null;
    private ArrayList<Carer> carers;
    private int currentCarerIndex;

    private CarerModel() {
    }

    public static CarerModel get() {
        if (carerModel == null) {
            carerModel = new CarerModel();
            carerModel.carers = new ArrayList<>();
            carerModel.currentCarerIndex = -1;
        }

        return carerModel;
    }

    public void setCurrentCarerIndex(Carer carer) {
        currentCarerIndex = carers.indexOf(carer);
        PreferencesManager.setCurrentCarerIndex(currentCarerIndex);
    }

    public void setCurrentCarerIndex(int index) {
        currentCarerIndex = index;
        PreferencesManager.setCurrentCarerIndex(currentCarerIndex);
    }

    public void setCurrentCarer(Carer carer) {
        if(currentCarerIndex >= 0 && carers.size() > 0)
            carers.set(currentCarerIndex, carer);
    }

    public Carer getCurrentCarer() throws NullPointerException{
        if(currentCarerIndex >= 0 && carers.size() > 0)
            return carers.get(currentCarerIndex);

        return null;
    }

    public boolean setCurrentCarerOverview(Carer carer){
        if(currentCarerIndex >= 0 && carers.size() > 0 && carer != null) {
            carers.get(currentCarerIndex).setFullName(carer.getFullName());
            carers.get(currentCarerIndex).setID(carer.getID());
            carers.get(currentCarerIndex).setOnlineTest(carer.getOnlineTest());
            carers.get(currentCarerIndex).setPhoto(carer.getPhoto());
            carers.get(currentCarerIndex).setPhotoByte(carer.getPhotoByte());


            return true;
        }
        return false;
    }



    public ArrayList<Carer> getCarers() {
        return carers;
    }

    public ArrayList<Carer> getCarersWithoutCurrent() {

        ArrayList<Carer> items  = new ArrayList<>();
        items = (ArrayList<Carer>)CarerModel.get().getCarers().clone();
        for (int i = 0; i< items.size(); i++){

            if(Integer.parseInt(items.get(i).getID()) == PreferencesManager.getCurrentCarerID()){
                Log.d(TAG, "RemoveCarer:" + items.get(i).getFullName() + "ItmesSize:" + items.size());

                items.remove(items.get(i));
                items.trimToSize();

                break;
            }

        }

        Log.d(TAG,"ItmesSize:" + items.size());

        return items;
    }

    public void setCarers(ArrayList<Carer> carers) {
        this.carers = carers;
    }
}
