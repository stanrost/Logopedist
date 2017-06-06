package com.example.strost.logopedist.model.request;

import com.example.strost.logopedist.model.entities.Patient;

import retrofit2.Call;

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 21-3-2017.
 */

public class GetPatientHttpRequest {

    public Patient getPatient(final Patient p) {
        final Patient[] result = new Patient[1];
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Call<Patient> call = restAdapter.getPatient(p.getObjectId());
                    result[0] = call.execute().body();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

}
