package com.example.strost.logopedist.model.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.google.gson.Gson;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by strost on 17-2-2017.
 */

public class GetZorgverlenerRequest implements Serializable {


    public GetZorgverlenerRequest() {
    }

    public Zorgverlener getZorgverlener(Context c) {
        SharedPreferences myPrefs;
        Zorgverlener obj;
        Gson gson;
        myPrefs = c.getSharedPreferences("myPrefs", MODE_PRIVATE);
        gson = new Gson();
        String z1 = myPrefs.getString("MEM1", "");
        obj = gson.fromJson(z1, Zorgverlener.class);
        return obj;
    }
}
