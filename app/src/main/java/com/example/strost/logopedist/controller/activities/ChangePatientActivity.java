package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.request.PutPatientHttpRequest;

/**
 * Created by strost on 20-2-2017.
 */

public class ChangePatientActivity extends AppCompatActivity {
    private Caregiver mCaregiver = null;
    private Patient mPatient;
    private EditText mFirstName, mLastName, mProblem, mEmail;
    private final String PATIENT_KEY = "Patient";
    private final String CAREGIVER_KEY = "Caregiver";
    private RadioGroup mGender;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepatient);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);
        mGender = (RadioGroup) findViewById(R.id.rgGender);
        FloatingActionButton fabAddPatient = (FloatingActionButton) findViewById(R.id.fabAddPatient);
        mFirstName = (EditText) findViewById(R.id.etPatientFistName);
        mLastName = (EditText) findViewById(R.id.etAddPatientLastName);
        mProblem = (EditText) findViewById(R.id.etAddPatientProblem);
        mEmail = (EditText) findViewById(R.id.etAddPatientEmail);
        mFirstName.setText(mPatient.getFirstName());
        mLastName.setText(mPatient.getLastName());
        mProblem.setText(mPatient.getProblem());
        mEmail.setText(mPatient.getEmail());

        setIndexRating(mPatient.getGender());

        fabAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangePatientActivity.this, getString(R.string.changed_patient), Toast.LENGTH_LONG).show();
                changePatient();
                addToFile();
                goBack();

            }
        });
    }

    public void changePatient(){
        mPatient.setFirstName(mFirstName.getText().toString());
        mPatient.setLastName(mLastName.getText().toString());
        mPatient.setProblem(mProblem.getText().toString());
        mPatient.setEmail(mEmail.getText().toString());
        mPatient.setGender(getGender());
    }

    public void addToFile(){
        final PutPatientHttpRequest pPHR = new PutPatientHttpRequest();
        pPHR.putPatient(mPatient);
    }

    public void goBack(){
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }


    public String getGender() {
        int rgid = mGender.getCheckedRadioButtonId();

        String gender = "";
        if (rgid == R.id.rbMan) {
            gender = "man";
        }

        if (rgid == R.id.rbWoman) {
            gender = "woman";
        }

        return gender;
    }

    public void setIndexRating(String gender) {
        switch (gender) {
            case "man":
                RadioButton ra1 = (RadioButton) this.findViewById(R.id.rbMan);
                ra1.setChecked(true);
                break;
            case "woman":
                RadioButton ra2 = (RadioButton) this.findViewById(R.id.rbWoman);
                ra2.setChecked(true);
                break;
        }

    }

}
