package com.example.strost.logopedist.model.entities;

import java.io.Serializable;

/**
 * Created by strost on 29-3-2017.
 */

public class Device implements Serializable {
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    private String deviceId;
}
