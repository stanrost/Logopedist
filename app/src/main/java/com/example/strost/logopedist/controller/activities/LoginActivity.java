package com.example.strost.logopedist.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.SaveSharedPreference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class LoginActivity extends Activity {

    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private EditText emailText, passwordText;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button loginButton = (Button) findViewById(R.id.button);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.password_text);

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
        PasswordEncryption pe = new PasswordEncryption();
        for (int i = 0; i < caregivers.size(); i++) {
            if (emailText.getText().toString().equals(caregivers.get(i).getEmail()) && pe.encryptPassword(passwordText.getText().toString()).equals(caregivers.get(i).getPassword())) {
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
