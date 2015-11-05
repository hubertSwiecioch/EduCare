package com.hswie.educaremobile.helper;

import com.hswie.educaremobile.api.pojo.Resident;

import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class ResidentsModel {

    private static ResidentsModel residentsModel = null;
    private ArrayList<Resident> residents;


    private ResidentsModel() {
    }

    public static ResidentsModel get() {
        if (residentsModel == null) {
            residentsModel = new ResidentsModel();
            residentsModel.residents = new ArrayList<>();

        }

        return residentsModel;
    }


    public ArrayList<Resident> getResidents() {
        return residents;
    }

    public void setResidents(ArrayList<Resident> residents) {
        this.residents = residents;
    }
}
