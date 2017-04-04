package com.example.strost.logopedist.controller.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity {
    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private int caregiverId;
    private Caregiver caregiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        caregiverId = getIntent().getExtras().getInt("caregiverId");
        getZorgverlender();
        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                caregiver = caregivers.get(i);
            }
        }
        final EditText oldPassword = (EditText)findViewById(R.id.oldpassword);

        final EditText newPassword = (EditText)findViewById(R.id.new_password);
        final EditText newPasswordCheck = (EditText)findViewById(R.id.newpasswordcheck);

        final Button changePassword = (Button) findViewById(R.id.change_password);

        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PasswordEncryption pe = new PasswordEncryption();
                if (!oldPassword.getText().toString().equals("") && !newPassword.getText().toString().equals("") && !newPasswordCheck.getText().toString().equals("")){
                    if (caregiver.getPassword().equals(pe.encryptPassword(oldPassword.getText().toString()))) {

                        if (newPassword.getText().toString().equals(newPasswordCheck.getText().toString())) {
                            PasswordEncryption pE = new PasswordEncryption();
                            caregiver.setPassword(pE.encryptPassword(newPassword.getText().toString()));
                            final UpdateCaregiverRequest uCR = new UpdateCaregiverRequest();
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    uCR.updateCaregiver(caregiver, caregiver);
                                }
                            };
                            Thread mythread = new Thread(runnable);
                            mythread.start();
                            try {
                                mythread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
                        } else {
                            PasswordsAreNotTheSame();
                        }
                    } else {
                        wrongOldPassword();
                    }
            }else{
                    EmptyEditText();
            }

            }

        });



    }

    public void wrongOldPassword(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Uw oude wachtwoord is fout!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void PasswordsAreNotTheSame(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Uw nieuwe wachtwoorden zijn niet hetzelfde!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void EmptyEditText(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Niet alle velden zijn ingevuld")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
}
