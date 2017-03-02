package com.example.strost.logopedist.model.request;

import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;
import java.util.ArrayList;
import java.util.List;

public class GetPatiënt {

    private Patient rightPatient;
    List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private List<Patient> patients = new ArrayList<Patient>();

    public Patient getPatient(int patientid, int caregiverId){
        Caregiver caregiver = null;

        final GetCaregiverRequest gzr = new GetCaregiverRequest();

        Runnable runnable = new Runnable() {
            public void run() {
                caregivers = gzr.getCaregiver();
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                caregiver = caregivers.get(i);
            }
        }
        patients = caregiver.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (patientid == patients.get(i).getId()) {
                rightPatient = patients.get(i);
            }
        }


        return rightPatient;
    }
}
