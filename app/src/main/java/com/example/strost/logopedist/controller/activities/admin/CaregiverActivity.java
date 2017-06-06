package com.example.strost.logopedist.controller.activities.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.DeleteCaregiverHttpRequest;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

public class CaregiverActivity extends AppCompatActivity {

    private Caregiver mCaregiver, mCaregiverUser;
    private final String CAREGIVER_KEY = "Caregiver";
    private final String CAREGIVER_USER_KEY = "CaregiverUser";
    private EditText mFirstName, mLastName, mEmail;
    private Switch mIsAdmin, mActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);
        mCaregiverUser = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_USER_KEY);

        mFirstName = (EditText) findViewById(R.id.etCaregiverFirstName);
        mLastName = (EditText) findViewById(R.id.etCaregiverLastName);
        mEmail = (EditText) findViewById(R.id.etCaregiverEmail);
        mIsAdmin = (Switch) findViewById(R.id.swtchCaregiverIsAdimn);
        mActivated = (Switch) findViewById(R.id.swtchCaregiverActivated);

        mFirstName.setText(mCaregiver.getFirstName());
        mLastName.setText(mCaregiver.getLastName());
        mEmail.setText(mCaregiver.getEmail());

        mIsAdmin.setChecked(mCaregiver.getIsAdmin());
        mActivated.setChecked(mCaregiver.getActivated());

        FloatingActionButton mChangeCaregiver = (FloatingActionButton) findViewById(R.id.fabChangeCaregiver);
        mChangeCaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFirstName.getText().toString().equals("") && !mLastName.getText().toString().equals("") && !mEmail.getText().toString().equals("")) {
                    if (mEmail.getText().toString().contains("@") && mEmail.getText().toString().contains("@")) {
                        changeCaregiver();
                        saveCaregiver();
                        Toast.makeText(CaregiverActivity.this, getString(R.string.changed_caregiver), Toast.LENGTH_LONG).show();
                    } else {
                        noEmailAdress();
                    }
                } else {
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

    public void changeCaregiver() {
        mCaregiver.setFirstName(mFirstName.getText().toString());
        mCaregiver.setLastName(mLastName.getText().toString());
        mCaregiver.setEmail(mEmail.getText().toString());
        mCaregiver.setIsAdmin(mIsAdmin.isChecked());
        mCaregiver.setActivated(mActivated.isChecked());
    }

    public void saveCaregiver() {
        final UpdateCaregiverRequest ucr = new UpdateCaregiverRequest();
        ucr.updateCaregiver(mCaregiver);

    }

    public void openRemoveDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(CaregiverActivity.this, getString(R.string.changed_caregiver), Toast.LENGTH_LONG).show();
                        deleteCaregiver();
                        goBack();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_remove_patient)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    public void deleteCaregiver() {
        DeleteCaregiverHttpRequest dchr = new DeleteCaregiverHttpRequest();
        dchr.deleteCaregiver(mCaregiver);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admincaregiver, menu);

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.removeCaregiver) {
            openRemoveDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goBack() {
        Intent detailIntent = new Intent(this, CaregiverListActivity.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiverUser);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

}
