package com.example.strost.logopedist.model.entities;

import java.io.Serializable;

/**
 * Created by strost on 29-3-2017.
 */

public class Device implements Serializable{

    private String objectId;
    private String deviceId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


}
