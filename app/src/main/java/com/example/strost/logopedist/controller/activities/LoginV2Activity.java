package com.example.strost.logopedist.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.fragments.LoginFragment;

/**
 * Created by strost on 23-2-2017.
 */

public class LoginV2Activity extends AppCompatActivity implements LoginFragment.OnButtonClickListener{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginv2_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public void nextPage(){
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        Toast.makeText(LoginV2Activity.this, "U bent ingelogd", Toast.LENGTH_LONG).show();
        startActivity(detailIntent);
        finish();
    }
}
