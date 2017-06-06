package com.example.strost.logopedist.model.request;

import com.example.strost.logopedist.model.entities.Caregiver;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 16-5-2017.
 */

public class DeleteCaregiverHttpRequest {

    public void deleteCaregiver(final Caregiver c){
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Call<ResponseBody> deleteRequest = restAdapter.deleteCaregiver(c.getObjectId());
                    deleteRequest.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }
                    });
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
