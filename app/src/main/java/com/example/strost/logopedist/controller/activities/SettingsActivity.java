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

/**
 * Created by strost on 28-2-2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private int caregiverId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        caregiverId = getIntent().getExtras().getInt("caregiverId");

        final Button button = (Button) findViewById(R.id.LogoutButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog();
            }
        });

        Button changepassword = (Button) findViewById(R.id.change_password_button);
        changepassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               goToChangePassword();
            }
        });
    }

    public void goToChangePassword(){
        Intent detailIntent = new Intent(this, ChangePasswordActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
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
                        finish();
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


    public void logout(){
        SaveSharedPreference sp = new SaveSharedPreference();
        sp.setCaretakerId(this, "");
    }

    public void goBack(){
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        finish();
        return;
    }
}
