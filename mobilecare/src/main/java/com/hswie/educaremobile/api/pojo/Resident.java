package com.hswie.educaremobile.api.pojo;

import android.util.Log;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class Resident implements Serializable {

    private String firstName;
    private String lastName;
    private String dateOfAdoption;
    private String birthDate;
    private String ID;
    private String address;
    private String city;
    private String photo;
    private String photoCache;
    private byte[] photoByte;
    private String TAG = "Resident";
    private ArrayList<Family> families = new ArrayList<Family>();
    private ArrayList<Medicine> medicines = new ArrayList<>();


    public boolean contains(String query) {
        String[] queries = query.split(" ");

        if (queries.length == 2) {
            if ((firstName.toLowerCase().contains(queries[0].toLowerCase())
                    && lastName.toLowerCase().contains(queries[1].toLowerCase()))
                    || (firstName.toLowerCase().contains(queries[1].toLowerCase())
                    && lastName.toLowerCase().contains(queries[0].toLowerCase()))) {
                //Log.d(TAG, "TRUE. firstname = " + firstname + " lastname = " + lastname);
                return true;
            }
        } else if (queries.length == 1) {
            if (firstName.toLowerCase().contains(queries[0].toLowerCase())
                    || lastName.toLowerCase().contains(queries[0].toLowerCase())) {
                //Log.d(TAG, "TRUE. firstname = " + firstname + " lastname = " + lastname);
                return true;
            }
        }

        return false;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfAdoption() {
        return dateOfAdoption;
    }

    public void setDateOfAdoption(String dateOfAdoption) {
        this.dateOfAdoption = dateOfAdoption;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoCache() {
        return photoCache;
    }

    public void setPhotoCache(String photoCache) {
        Log.d(TAG, "photoCache = " + photoCache);
        this.photoCache = photoCache;
    }

    public byte[] getPhotoByte() {
        return photoByte;
    }

    public void setPhotoByte(byte[] photoByte) {
        this.photoByte = photoByte;
    }

    public ArrayList<Family> getFamilies() {
        return families;
    }

    public void setFamilies(ArrayList<Family> families) {
        this.families = families;
    }

    public Family getFamily(int position) {
        return families.get(position);
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }
}
