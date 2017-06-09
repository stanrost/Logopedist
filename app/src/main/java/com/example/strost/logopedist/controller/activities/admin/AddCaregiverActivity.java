package com.example.strost.logopedist.controller.activities.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.PatientActivity;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.PostCaregiverHttpRequest;
import com.example.strost.logopedist.model.request.SendEmailCaregiver;

import java.security.SecureRandom;
import java.util.Random;

public class AddCaregiverActivity extends AppCompatActivity {
    private Caregiver mCaregiver, mNewCaregiver;
    private EditText mFirstName, mLastName, mEmail;
    private Switch mIsAdmin, mActivated;
    private String mPassword;
    private final String CAREGIVER_ID_KEY = "caregiverId";
    private final String CAREGIVER_KEY = "Caregiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcaregiver);

        final int mCaregiverId = getIntent().getExtras().getInt(CAREGIVER_ID_KEY);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);

        mFirstName = (EditText) findViewById(R.id.etCaregiverFirstName);
        mLastName = (EditText) findViewById(R.id.etCaregiverLastName);
        mEmail = (EditText) findViewById(R.id.etCaregiverEmail);
        mIsAdmin = (Switch) findViewById(R.id.swtchCaregiverIsAdimn);
        mActivated = (Switch) findViewById(R.id.swtchCaregiverActivated);

        mNewCaregiver = new Caregiver();

        FloatingActionButton mAddCaregiver = (FloatingActionButton) findViewById(R.id.fabAddCaregiver);
        mAddCaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPassword = randomString();
                if(!mFirstName.getText().toString().equals("") && !mLastName.getText().toString().equals("") && !mEmail.getText().toString().equals("")) {
                    if(mEmail.getText().toString().contains("@") && mEmail.getText().toString().contains("@")) {
                        addCaregiver(mCaregiverId);
                        saveCaregiver();
                        Toast.makeText(AddCaregiverActivity.this, getString(R.string.added_caregiver), Toast.LENGTH_LONG).show();
                        sendEmail();
                        finish();
                    }
                    else{
                        noEmailAdress();
                    }
                }
                else{
                    emptyEditText();
                }
            }
        });
    }

    public void emptyEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.fields_are_not_filled_in))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void noEmailAdress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.email_is_not_correct))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addCaregiver(int mCaregiverId){
        PasswordEncryption pe = new PasswordEncryption();
        mNewCaregiver.setId(mCaregiverId);
        mNewCaregiver.setFirstName(mFirstName.getText().toString());
        mNewCaregiver.setLastName(mLastName.getText().toString());
        mNewCaregiver.setEmail(mEmail.getText().toString());
        mNewCaregiver.setIsAdmin(mIsAdmin.isChecked());
        mNewCaregiver.setActivated(mActivated.isChecked());
        mNewCaregiver.setPassword(pe.encryptPassword(mPassword));
        mNewCaregiver.setChangedGenaratedPassword(false);


    }

    public static String randomString() {
        char[] characterSet = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[8];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }


    public void saveCaregiver(){
        final PostCaregiverHttpRequest ucr = new PostCaregiverHttpRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    ucr.postCaregiver(mNewCaregiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void sendEmail(){
        SendEmailCaregiver sec = new SendEmailCaregiver();
        sec.sendEmail(mPassword, mNewCaregiver);
    }



}
