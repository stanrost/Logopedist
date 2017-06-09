package com.example.strost.logopedist.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

public class ChangePasswordActivity extends AppCompatActivity {

    private Caregiver mCaregiver;
    private String mPassword = "";

    private final String CAREGIVER_KEY = "Caregiver";
    private Boolean mChangedGeneratedPassword = false;
    private final String PASSWORD_KEY = "Password";
    private final String EMAIL_KEY = "Email";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);
        mPassword = getIntent().getStringExtra(PASSWORD_KEY);
        mChangedGeneratedPassword = mCaregiver.getChangedGenaratedPassword();

        final EditText mOldPassword = (EditText) findViewById(R.id.etOldPassword);
        final EditText mNewPassword = (EditText) findViewById(R.id.etNewPassword);
        final EditText mNewPasswordCheck = (EditText) findViewById(R.id.etRepeatNewPassword);
        mOldPassword.setText(mPassword);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPatient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordEncryption pe = new PasswordEncryption();
                if (!mOldPassword.getText().toString().equals("") && !mNewPassword.getText().toString().equals("") && !mNewPasswordCheck.getText().toString().equals("")) {
                    if (mCaregiver.getPassword().equals(pe.encryptPassword(mOldPassword.getText().toString()))) {

                        if (mNewPassword.getText().toString().equals(mNewPasswordCheck.getText().toString())) {
                            PasswordEncryption pE = new PasswordEncryption();
                            mCaregiver.setPassword(pE.encryptPassword(mNewPassword.getText().toString()));
                            mCaregiver.setChangedGenaratedPassword(true);
                            Toast.makeText(ChangePasswordActivity.this, getString(R.string.password_is_changed), Toast.LENGTH_SHORT).show();
                            final UpdateCaregiverRequest uCR = new UpdateCaregiverRequest();
                            uCR.updateCaregiver(mCaregiver);
                            goBack();
                            finish();
                        } else {
                            passwordsAreNotTheSame();
                        }
                    } else {
                        wrongOldPassword();
                    }
                } else {
                    emptyEditText();
                }
            }
        });
    }

    public void wrongOldPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.password_is_wrong))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void passwordsAreNotTheSame() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.passwords_are_not_the_same))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

    public void goBack() {
        if (mChangedGeneratedPassword == true) {
        } else if (mChangedGeneratedPassword == false) {
            Intent detailIntent = new Intent(this, MainActivity.class);
            detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (mChangedGeneratedPassword == true) {
            goBack();
        } else if (mChangedGeneratedPassword == false) {
            Intent detailIntent = new Intent(this, LoginActivity.class);
            detailIntent.putExtra(EMAIL_KEY, mCaregiver.getEmail());
            startActivity(detailIntent);
        }
        finish();
        return;
    }


}
