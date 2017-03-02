package com.example.strost.logopedist.model.entities;

import android.util.Log;

import com.example.strost.logopedist.model.entities.Patient;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class Caregiver {
    private int id;
    private String objectId;
    private String name;
    private String email;
    private String password;
    List<Patient> patients = new ArrayList<Patient>();


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

   public String getName(){
        return name;
    }

    public List<Patient> getPatients(){
        List<Patient> getpatients = new ArrayList<Patient>();
        if(patients.size() > 0){
            getpatients = patients;
        }
        return getpatients;
    }

    public void addPatient(Patient p){

        patients.add(p);
    }

    public void changePatient(int ChangeId, String ChangeName){
        Patient p = getPatient(ChangeId);

        p.setName(ChangeName);
    }

    public Patient getPatient(int id){
        Patient rightPatient = null;
        for (int i = 0; i < patients.size(); i++) {
            if (id == patients.get(i).getId()) {
                Log.e("hoi", "" + (patients.get(i).toString()));
                rightPatient = patients.get(i);
            }
        }
        return rightPatient;
    }

    public void removePatient(Patient patient){
        patients.remove(patient);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
