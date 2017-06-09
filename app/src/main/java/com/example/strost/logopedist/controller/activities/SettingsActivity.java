package com.example.strost.logopedist.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.SaveSharedPreference;
import com.example.strost.logopedist.model.entities.Caregiver;

/**
 * Created by strost on 28-2-2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private Caregiver mCaregiver;

    private final String CAREGIVER_KEY = "Caregiver";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);
        final Button button = (Button) findViewById(R.id.btnLogout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog();
            }
        });

        Button changepassword = (Button) findViewById(R.id.btnChangePassword);
        changepassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToChangePassword();
            }
        });
    }

    public void goToChangePassword() {
        Intent detailIntent = new Intent(this, ChangePasswordActivity.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
    }

    public void openDialog() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        logout();
                        Toast.makeText(SettingsActivity.this, getString(R.string.logged_out), Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_logout)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }


    public void logout() {
        SaveSharedPreference sp = new SaveSharedPreference();
        sp.setCaretakerId(this, "");
        Intent detailIntent = new Intent(this, LoginActivity.class);
        startActivity(detailIntent);
        finish();
    }

}