package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.strost.logopedist.R;

/**
 * Created by strost on 16-2-2017.
 */

public class LoginActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button loginButton = (Button) findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextPage();
            }
        });

    }

    public void nextPage(){
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        Toast.makeText(LoginActivity.this, "U bent ingelogd", Toast.LENGTH_LONG).show();
        startActivity(detailIntent);
        finish();
    }
}
