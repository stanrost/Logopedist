package com.example.strost.logopedist.model.request;
import android.util.Log;

import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Caregiver;

public class UpdateCaregiverRequest {

    public void updateCaregiver(final Caregiver caregiver) {
        Runnable runnable = new Runnable() {
            public void run() {
                Backendless.Persistence.save(caregiver);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


