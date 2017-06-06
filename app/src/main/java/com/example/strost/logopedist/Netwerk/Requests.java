package com.example.strost.logopedist.Netwerk;

import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.CaregiverResponse;
import com.example.strost.logopedist.model.entities.Patient;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by strost on 20-3-2017.
 */

public interface Requests {

    final String BASE_URL = "https://api.backendless.com/v1/";

    @GET("data/Caregiver")
    Call<CaregiverResponse> getCaregivers();

    @GET("data/Patient/{request_id}")
    Call<Patient> getPatient(@Path("request_id") String requestId);

    @DELETE("data/Patient/{request_id}")
    Call<ResponseBody> deletePatient(@Path("request_id") String requestId);

    @DELETE("data/Caregiver/{request_id}")
    Call<ResponseBody> deleteCaregiver(@Path("request_id") String requestId);

    @PUT("data/Patient/{request_id}")
    Call<ResponseBody> putPatient(@Path("request_id") String requestId, @Body Patient requestBody);

    @PUT("data/Exercise/{request_id}")
    Observable<Patient> putRequest(@Path("request_id") String requestId, @Body Patient requestBody);

    @POST("data/Caregiver")
    Call<CaregiverResponse> postCaregiver(@Body Caregiver caregiver);


}
