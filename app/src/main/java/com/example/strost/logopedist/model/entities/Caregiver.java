package com.example.strost.logopedist.model.entities;

import android.util.Log;

import com.example.strost.logopedist.model.entities.Patient;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class Caregiver implements Serializable{
    private int id;
    private String objectId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean activated;
    List<Patient> patients = new ArrayList<Patient>();
    private boolean changedGenaratedPassword;

    public boolean getChangedGenaratedPassword() {
        return changedGenaratedPassword;
    }

    public void setChangedGenaratedPassword(boolean changedGenaratedPassword) {
        this.changedGenaratedPassword = changedGenaratedPassword;
    }



    public String getObjectId() {
        return objectId;
    }

    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    public String getFirstName(){
        return firstName;
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

    public void setFirstName(String name) {
        this.firstName = name;
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }


    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
