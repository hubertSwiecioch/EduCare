package com.hswie.educaremobile.helper;

import com.hswie.educaremobile.api.pojo.Carer;

import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class CarersModel {

    private static CarersModel carersModel = null;
    private ArrayList<Carer> carers;


    private CarersModel() {
    }

    public static CarersModel get() {
        if (carersModel == null) {
            carersModel = new CarersModel();
            carersModel.carers = new ArrayList<>();

        }

        return carersModel;
    }


    public ArrayList<Carer> getCarers() {
        return carers;
    }

    public void setCarers(ArrayList<Carer> carers) {
        this.carers = carers;
    }
}
