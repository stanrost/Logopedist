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

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 20-3-2017.
 */

public class GetCaregiverHttpRequest {
    private CompositeDisposable mCompositeDisposable;
    List<Caregiver> caregivers = new ArrayList<Caregiver>();

    public List<Caregiver> getCaregiver() {
        mCompositeDisposable = new CompositeDisposable();

        mCompositeDisposable.add(restAdapter.getCaregivers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<CaregiverResponse>() {
                    @Override
                    public void onNext(CaregiverResponse value) {
                        caregivers = value.getData();
                        for (Caregiver ex :
                                caregivers) {
                            for (Patient p:
                                    ex.getPatients() ) {
                                Log.e("test patient", p.getName());
                            }
                            Log.e("test", ex.getName());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

        return caregivers;
    }
}
