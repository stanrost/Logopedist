package com.example.strost.logopedist.Netwerk;

import com.example.strost.logopedist.model.entities.CaregiverResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by strost on 20-3-2017.
 */

public interface Requests {

    final String BASE_URL = "https://api.backendless.com/v1/";

    @GET("data/Caregiver")
    Observable<CaregiverResponse> getCaregivers();


}
