package com.example.strost.logopedist.model.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.google.gson.Gson;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by strost on 20-2-2017.
 */

public class SetZorgverlenerRequest implements Serializable{

    public SetZorgverlenerRequest(){

    }

   public void setZorgverlener(Zorgverlener z1, Context c){
        SharedPreferences myPrefs;

        myPrefs = c.getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(z1);
        editor.putString("MEM1", json);
        editor.commit();
    }


}
