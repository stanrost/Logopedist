package com.example.strost.logopedist.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by strost on 20-3-2017.
 */

public class CaregiverResponse {
    @SerializedName("data")
    private List<Caregiver> data;

    public CaregiverResponse(List<Caregiver> data) {
        this.data = data;
    }

    public List<Caregiver> getData() {
        return data;
    }

    public void setData(List<Caregiver> data) {
        this.data = data;
    }
}
