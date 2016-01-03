package com.hswie.educaremobile.helper;

import android.util.Log;

import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.Family;
import com.hswie.educaremobile.api.pojo.Resident;

import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class FamilyModel {

    private static final String TAG = "FamilyModel";
    private static FamilyModel familyModel = null;
    private ArrayList<Family> families;
    private int currentFamilyIndex;
    private Resident resident;

    private FamilyModel() {
    }

    public static FamilyModel get() {
        if (familyModel == null) {
            familyModel = new FamilyModel();
            familyModel.families = new ArrayList<>();
            familyModel.currentFamilyIndex = -1;
        }

        return familyModel;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public void setCurrentFamilyIndex(Family family) {
        currentFamilyIndex = families.indexOf(family);
        PreferencesManager.setCurrentFamilyIndex(currentFamilyIndex);
    }

    public void setCurrentFamilyIndex(int index) {
        currentFamilyIndex = index;
        PreferencesManager.setCurrentCarerIndex(currentFamilyIndex);
    }

    public void setCurrentFamily(Family family) {
        if(currentFamilyIndex >= 0 && families.size() > 0)
            families.set(currentFamilyIndex, family);
    }

    public Family getCurrentFamily() throws NullPointerException{
        if(currentFamilyIndex >= 0 && families.size() > 0)
            return families.get(currentFamilyIndex);

        return null;
    }

    public void setCurrentFamily(){

        for (Family family: FamilyModel.get().getFamilies()) {

            if(Integer.parseInt(family.getID()) == PreferencesManager.getCurrentFamilyID()){
                FamilyModel.get().setCurrentFamilyIndex(family);
                Log.d(TAG, "SetCurrentFamilyID: " + FamilyModel.get().getCurrentFamily().getID());
            }


        }
    }

    public ArrayList<Family> getResidentFamily(String residentID){

        ArrayList<Family> residentFamilies = new ArrayList<>();

        for (Family family: families ) {

            if (family.getResidentID().equals(residentID))
                residentFamilies.add(family);

        }


        return residentFamilies;

    }

    public Family getFamilyByID(String familyID){



        for (Family item: this.getFamilies()) {

            if(item.getID().equals(familyID)){

                return item;
            }

        }

        return null;
    }





//    public boolean setCurrentFamilyOverview(Family family){
//        if(currentFamilyIndex >= 0 && families.size() > 0 && family != null) {
//            family.get(currentFamilyIndex).setFullName(family.getFullName());
//            family.get(currentFamilyIndex).setID(family.getID());
//            family.get(currentFamilyIndex).setOnlineTest(family.getOnlineTest());
//            family.get(currentFamilyIndex).setPhoto(family.getPhoto());
//            family.get(currentFamilyIndex).setPhotoByte(family.getPhotoByte());
//
//
//            return true;
//        }
//        return false;
//    }



    public ArrayList<Family> getFamilies() {
        return families;
    }

    public ArrayList<Family> getFamiliesWithoutCurrent() {

        ArrayList<Family> items  = new ArrayList<>();
        items = (ArrayList<Family>) FamilyModel.get().getFamilies().clone();
        for (int i = 0; i< items.size(); i++){

            if(Integer.parseInt(items.get(i).getID()) == PreferencesManager.getCurrentFamilyID()){
                Log.d(TAG, "RemoveFamily:" + items.get(i).getFullName() + "ItmesSize:" + items.size());

                items.remove(items.get(i));
                items.trimToSize();

                break;
            }

        }

        Log.d(TAG,"ItmesSize:" + items.size());

        return items;
    }

    public void setFamilies(ArrayList<Family> families) {
        this.families = families;
    }
}
