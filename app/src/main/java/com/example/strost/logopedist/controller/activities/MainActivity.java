package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.SetZorgverlenerRequest;

public class MainActivity extends AppCompatActivity{

    private boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Zorgverlener z1 = new Zorgverlener(1, "Esmee");
        SetZorgverlenerRequest zr = new SetZorgverlenerRequest();
        zr.setZorgverlener(z1, this);

        /*SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(z1);
        editor.putString("MEM1", json);
        editor.commit();*/

        if (loggedIn == true){
            Intent detailIntent = new Intent(this, MainPageActivity.class);
            startActivity(detailIntent);
            finish();
        }
        else{
            Intent detailIntent = new Intent(this, LoginV2Activity.class);
            startActivity(detailIntent);
            finish();
        }


    }
}
