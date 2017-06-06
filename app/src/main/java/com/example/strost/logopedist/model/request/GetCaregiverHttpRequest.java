package com.example.strost.logopedist.model.request;

import android.util.Log;

import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.CaregiverResponse;
import com.example.strost.logopedist.model.entities.Patient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 20-3-2017.
 */

public class GetCaregiverHttpRequest {
    List<Caregiver> caregivers = new ArrayList<Caregiver>();

    public List<Caregiver> getCaregiver() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Call<CaregiverResponse> call = restAdapter.getCaregivers();
                    CaregiverResponse result = call.execute().body();
                    caregivers = result.getData();
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

        return caregivers;
    }
}
