package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.backendless.Backendless;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.SaveSharedPreference;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appVersion = "v1";
        Backendless.initApp( this, "E5A95319-DFEE-9344-FF32-50448355EC00", "FE40A368-BB4B-7B9C-FF31-240C9C1AE700", appVersion );

        String s = SaveSharedPreference.getUserName(MainActivity.this);
        if(SaveSharedPreference.getUserName(MainActivity.this).length() > 0){
            Intent detailIntent = new Intent(this, MainPageActivity.class);
            int id = Integer.parseInt(SaveSharedPreference.getUserName(MainActivity.this));
            detailIntent.putExtra("caregiverId", id);
            startActivity(detailIntent);
            finish();
        }
        else{
            Intent detailIntent = new Intent(this, LoginActivity.class);
            startActivity(detailIntent);
            finish();
        }
    }
}
