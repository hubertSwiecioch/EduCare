package com.hswie.educaremobile.helper;

import com.hswie.educaremobile.api.pojo.Family;
import com.hswie.educaremobile.api.pojo.Resident;

import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class FamiliesModel {

    private static FamiliesModel familiesModel = null;
    private ArrayList<Family> families ;


    private FamiliesModel() {
    }

    public static FamiliesModel get() {
        if (familiesModel == null) {
            familiesModel = new FamiliesModel();
            familiesModel.families = new ArrayList<>();

        }

        return familiesModel;
    }


    public ArrayList<Family> getFamilies() {
        return families;
    }

    public void setFamilies(ArrayList<Family> families) {
        this.families = families;
    }
}
