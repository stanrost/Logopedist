package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.SaveSharedPreference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class LoginActivity extends AppCompatActivity {

    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private EditText emailText, passwordText;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button loginButton = (Button) findViewById(R.id.button);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.Password_Text);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        getZorgverlender();
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextPage();
            }
        });
    }
    public void getZorgverlender() {
        final GetCaregiverRequest gzr = new GetCaregiverRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                caregivers = gzr.getCaregiver();
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void nextPage(){
        int registred = 0;
        int caregiverId = 0;
        for (int i = 0; i < caregivers.size(); i++) {
            if (emailText.getText().toString().equals(caregivers.get(i).getEmail())) {
                registred++;
                caregiverId = caregivers.get(i).getId();
            }
        }

        if(registred > 0) {
            SaveSharedPreference sp = new SaveSharedPreference();

            Intent detailIntent = new Intent(this, MainPageActivity.class);
            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
            detailIntent.putExtra("caregiverId", caregiverId);
            startActivity(detailIntent);
            sp.setCaretakerId(this, "" + caregiverId);
            finish();
        }
        else{
            Toast.makeText(LoginActivity.this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
        }
    }
}
