package com.example.strost.logopedist.model.entities;

import android.util.Log;

import com.example.strost.logopedist.model.entities.Patient;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class Zorgverlener {
    private int id;
    private String name;
    List<Patient> patients = new ArrayList<Patient>();

    public Zorgverlener(int id, String name){
        this.id = id;
        this.name = name;
        Patient p1 = new Patient(1, "Stan");
        Patient p2 = new Patient(2, "Rick");
        Patient p3 = new Patient(3, "Robbert");
        patients.add(p1);
        patients.add(p2);
        patients.add(p3);

        p1.addOpdracht(1, "spraak");
        p1.addOpdracht(2, "stem");
        p1.addOpdracht(3, "slik");
    }

    public String getName(){
        return name;
    }

    public List<Patient> getPatients(){
        return patients;
    }

    public void addPatient(int addId, String addName){
        Patient addP = new Patient(addId, addName);
        patients.add(addP);
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
}
