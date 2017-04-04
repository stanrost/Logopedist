package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.PasswordEncryption;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.SendEmailPatient;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 20-2-2017.
 */
public class AddPatientActivity extends AppCompatActivity{
    private List<Patient> patients = new ArrayList<Patient>();
    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private int caregiverId;
    private Caregiver caregiver = null, newCaregiver = null;
    private Patient p;
    private EditText password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpatient_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        caregiverId = getIntent().getExtras().getInt("caregiverId");

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

        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                caregiver = caregivers.get(i);
            }
        }

        patients = caregiver.getPatients();

        final EditText name = (EditText) findViewById(R.id.AddPatientName);
        final EditText email = (EditText) findViewById(R.id.addEmail);
        password = (EditText) findViewById(R.id.patientPassword);
        int maxId = 0;

        for (int i = 0; i < patients.size(); i++)
        {
            if (patients.get(i).getId() > maxId)
            {
                maxId = patients.get(i).getId();
            }
        }
        final int id = maxId + 1;

        newCaregiver = caregiver;
        final PasswordEncryption pE = new PasswordEncryption();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pE.encryptPassword(password.getText().toString());
                p = new Patient();
                p.setId(id);
                p.setName(name.getText().toString());
                p.setEmail(email.getText().toString());
                p.setPassword(pE.encryptPassword(password.getText().toString()));
                newCaregiver.addPatient(p);
                Toast.makeText(AddPatientActivity.this, getString(R.string.added_patient), Toast.LENGTH_LONG).show();
                addToFile();
                sendEmail();
                goBack();

            }
        });
    }

    public void sendEmail(){
        SendEmailPatient sep = new SendEmailPatient();
        sep.sendEmail(password.getText().toString(), p);
    }

    public void addToFile(){
        final UpdateCaregiverRequest uzr = new UpdateCaregiverRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                uzr.updateCaregiver(caregiver, newCaregiver);
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

    public void goBack(){
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }

}
