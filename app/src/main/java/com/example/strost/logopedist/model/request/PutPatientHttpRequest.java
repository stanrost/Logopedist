package com.example.strost.logopedist.model.request;

import android.util.Log;

import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 6-4-2017.
 */

public class PutPatientHttpRequest {

    public void putPatient(final Patient patient){

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Backendless.Persistence.save(patient);
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
