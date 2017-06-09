package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.request.SendEmailPatient;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 20-2-2017.
 */
public class AddPatientActivity extends AppCompatActivity {
    private List<Patient> mPatients = new ArrayList<Patient>();
    private int mCaregiverId;
    private Caregiver mCaregiver = null, mNewCaregiver = null;
    private Patient mPatient;
    private EditText mPasswordText;
    private RadioGroup mGender;

    private final String CAREGIVER_KEY = "Caregiver";
    private final String CAREGIVER_ID_KEY = "caregiverId";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);
        mCaregiverId = mCaregiver.getId();
        mPatients = mCaregiver.getPatients();

        mPasswordText = (EditText) findViewById(R.id.etPatientPassword);
        mGender = (RadioGroup) findViewById(R.id.rgGender);
        mNewCaregiver = mCaregiver;

        setFABButton();
    }

    public void setFABButton(){
        final EditText firstName = (EditText) findViewById(R.id.etAddPatientFistName);
        final EditText lastName = (EditText) findViewById(R.id.etAddPatientLastName);
        final EditText problem = (EditText) findViewById(R.id.etAddPatientProblem);
        final EditText email = (EditText) findViewById(R.id.etAddPatientEmail);

        final PasswordEncryption pE = new PasswordEncryption();

        int maxId = 0;
        for (int i = 0; i < mPatients.size(); i++) {
            if (mPatients.get(i).getId() > maxId) {
                maxId = mPatients.get(i).getId();
            }
        }

        final int id = maxId + 1;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPatient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("") && !firstName.getText().toString().equals("") && !mPasswordText.getText().toString().equals("")) {
                    if (email.getText().toString().contains("@") && email.getText().toString().contains(".")) {
                        pE.encryptPassword(mPasswordText.getText().toString());
                        mPatient = new Patient();
                        mPatient.setId(id);
                        mPatient.setFirstName(firstName.getText().toString());
                        mPatient.setLastName(lastName.getText().toString());
                        mPatient.setProblem(problem.getText().toString());
                        mPatient.setEmail(email.getText().toString());
                        mPatient.setPassword(pE.encryptPassword(mPasswordText.getText().toString()));
                        mPatient.setGender(getGender());
                        mNewCaregiver.addPatient(mPatient);
                        Toast.makeText(AddPatientActivity.this, getString(R.string.added_patient), Toast.LENGTH_LONG).show();
                        addToFile();
                        sendEmail();
                        finish();
                    } else {
                        Toast.makeText(AddPatientActivity.this, getString(R.string.email_is_not_correct), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddPatientActivity.this, getString(R.string.fields_are_not_filled_in), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendEmail() {
        SendEmailPatient sep = new SendEmailPatient();
        sep.sendEmail(mPasswordText.getText().toString(), mPatient);
    }

    public void addToFile() {
        final UpdateCaregiverRequest uzr = new UpdateCaregiverRequest();
        uzr.updateCaregiver(mNewCaregiver);
    }


    public String getGender() {
        int rgid = mGender.getCheckedRadioButtonId();

        String gender = "";
        if (rgid == R.id.rbMan) {
            gender = "man";
        }

        if (rgid == R.id.rbMan) {
            gender = "woman";
        }

        return gender;
    }

}
