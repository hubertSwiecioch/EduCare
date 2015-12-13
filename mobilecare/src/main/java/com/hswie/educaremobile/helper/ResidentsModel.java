package com.hswie.educaremobile.helper;

import com.hswie.educaremobile.api.pojo.Medicine;
import com.hswie.educaremobile.api.pojo.Resident;

import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class ResidentsModel {

    private static ResidentsModel residentsModel = null;
    private ArrayList<Resident> residents;
    private ArrayList<Resident> familyResidents;
    private int currentResidentIndex;

    private ResidentsModel() {
    }

    public static ResidentsModel get() {
        if (residentsModel == null) {
            residentsModel = new ResidentsModel();
            residentsModel.familyResidents = new ArrayList<>();
            residentsModel.residents = new ArrayList<>();
            residentsModel.currentResidentIndex = -1;
        }

        return residentsModel;
    }

    public void setCurrentResidentIndex(Resident resident) {
        currentResidentIndex = residents.indexOf(resident);
        PreferencesManager.setCurrentResidentIndex(currentResidentIndex);
    }

    public void setCurrentResidentIndex(int index) {
        currentResidentIndex = index;
        PreferencesManager.setCurrentResidentIndex(currentResidentIndex);
    }

    public void setCurrentResident(Resident resident) {
        if(currentResidentIndex >= 0 && residents.size() > 0)
            residents.set(currentResidentIndex, resident);
    }

    public Resident getCurrentResident() throws NullPointerException{
        if(currentResidentIndex >= 0 && residents.size() > 0)
            return residents.get(currentResidentIndex);

        return null;
    }

    public boolean setCurrentResidentOverview(Resident resident){
        if(currentResidentIndex >= 0 && residents.size() > 0 && resident != null) {
            residents.get(currentResidentIndex).setFirstName(resident.getFirstName());
            residents.get(currentResidentIndex).setLastName(resident.getLastName());
            residents.get(currentResidentIndex).setAddress(resident.getAddress());
            residents.get(currentResidentIndex).setCity(resident.getCity());
            residents.get(currentResidentIndex).setPhoto(resident.getPhoto());
            residents.get(currentResidentIndex).setPhotoByte(resident.getPhotoByte());


            return true;
        }
        return false;
    }

    public ArrayList<Resident> getFamilyResidents() {
        return familyResidents;
    }

    public void setFamilyResidents(ArrayList<Resident> familyResidents) {
        this.familyResidents = familyResidents;
    }



    public ArrayList<Resident> getResidents() {
        return residents;
    }

    public void setResidents(ArrayList<Resident> residents) {
        this.residents = residents;
    }

    public void setResidentsMedicines(ArrayList<Medicine> medicines){

        for (Resident resident: residents) {

            for (Medicine medicine: medicines) {

                if (resident.getID().equals(medicine.getResidentID()))
                    resident.getMedicines().add(medicine);
            }

        }
    }
}
