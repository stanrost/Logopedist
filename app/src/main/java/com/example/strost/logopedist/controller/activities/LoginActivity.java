package com.example.strost.logopedist.controller.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strost.logopedist.ForgotPassword;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.model.request.GetCaregiverHttpRequest;
import com.example.strost.logopedist.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private List<Caregiver> mCaregivers = new ArrayList<Caregiver>();
    private List<String> mEmailList = new ArrayList<String>();
    private EditText mEmailText, mPasswordText;
    private TextView mForgotPassword;
    private final String CAREGIVER_ID_KEY = "caregiverId";
    private final String CAREGIVER_KEY = "Caregiver";
    private final String PASSPORT_KEY = "Password";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button loginButton = (Button) findViewById(R.id.btnLogin);
        mEmailText = (EditText) findViewById(R.id.etEmailText);
        mPasswordText = (EditText) findViewById(R.id.etPasswordText);
        mForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        getCaregiver();
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextPage();
            }
        });
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordAction();
            }
        });

        for (Caregiver c : mCaregivers) {
            mEmailList.add(c.getEmail());
        }
    }

    private void forgotPasswordAction() {
        final ForgotPassword fP = new ForgotPassword();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_forgotpassword, null);
        builder.setView(layout);
        final EditText emailAdress = (EditText) layout.findViewById(R.id.etSetEmail);
        final TextView error = (TextView) layout.findViewById(R.id.tvErrorMessage);
        emailAdress.setText(mEmailText.getText().toString());
        builder.setMessage(getString(R.string.email_will_be_send_to))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }
                ).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;

                mPasswordText.setText("");
                if (emailAdress.getText().toString().contains("@") && emailAdress.getText().toString().contains(".") && mEmailList.contains(emailAdress.getText().toString())) {
                    fP.forgotPassword(emailAdress.getText().toString(), mCaregivers, getApplicationContext());
                    Toast.makeText(LoginActivity.this, getString(R.string.email_is_send_to) + " " + emailAdress.getText().toString(), Toast.LENGTH_LONG).show();
                    wantToCloseDialog = true;
                } else {
                    error.setText(getString(R.string.wrong_email));
                }

                if (wantToCloseDialog)
                    alert.dismiss();

            }
        });
    }

    public void getCaregiver() {

        final GetCaregiverHttpRequest gzr = new GetCaregiverHttpRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                mCaregivers = gzr.getCaregiver();
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mCaregivers.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.login_no_internet_connection))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public void nextPage() {
        int registred = 0;
        Caregiver rightCaregivers = null;
        PasswordEncryption pe = new PasswordEncryption();
        for (int i = 0; i < mCaregivers.size(); i++) {
            if (mEmailText.getText().toString().equals(mCaregivers.get(i).getEmail()) && pe.encryptPassword(mPasswordText.getText().toString()).equals(mCaregivers.get(i).getPassword())) {
                registred++;
                rightCaregivers = mCaregivers.get(i);
            }
        }

        if (registred > 0) {
            SaveSharedPreference sp = new SaveSharedPreference();
            showReminderDiaglog(rightCaregivers);
            sp.setCaretakerId(this, "" + rightCaregivers.getId());

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
        }
    }

    public void showReminderDiaglog(final Caregiver caregiver){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.reminder_diaglog))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        chooseNextPage(caregiver);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void chooseNextPage(Caregiver caregiver) {
        if (caregiver.getChangedGenaratedPassword() == true) {
            Intent detailIntent = new Intent(this, MainPageActivity.class);
            detailIntent.putExtra(CAREGIVER_ID_KEY, caregiver.getId());
            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
            startActivity(detailIntent);
        } else if (caregiver.getChangedGenaratedPassword() == false) {
            Intent detailIntent = new Intent(this, ChangePasswordActivity.class);
            detailIntent.putExtra(CAREGIVER_KEY, caregiver);
            detailIntent.putExtra(PASSPORT_KEY, mPasswordText.getText().toString());
            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
            startActivity(detailIntent);
        }

    }
}
