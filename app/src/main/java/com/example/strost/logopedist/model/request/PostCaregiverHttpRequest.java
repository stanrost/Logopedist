package com.example.strost.logopedist.model.request;

import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.CaregiverResponse;
import com.example.strost.logopedist.model.entities.Patient;

import retrofit2.Call;

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 16-5-2017.
 */

public class PostCaregiverHttpRequest {

    public void postCaregiver(final Caregiver c){
    Runnable runnable = new Runnable() {
        public void run() {
            try {
                Call<CaregiverResponse> call = restAdapter.postCaregiver(c);
                CaregiverResponse result = call.execute().body();
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

}
}
