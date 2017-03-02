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
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 20-2-2017.
 */

public class ChangePatientActivity extends AppCompatActivity {
    private List<Patient> patients = new ArrayList<Patient>();
    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private int caregiverId;
    private Caregiver caregiver = null, newCaregiver = null;
    private Patient rightPatient;
    private EditText name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepatient_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final GetCaregiverRequest gzr = new GetCaregiverRequest();
        caregiverId = getIntent().getExtras().getInt("caregiverId");
        final int patientId = getIntent().getExtras().getInt("id");

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

        name = (EditText) findViewById(R.id.changePatientName);
        newCaregiver = caregiver;
        patients = newCaregiver.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (patientId == patients.get(i).getId()) {
                rightPatient = patients.get(i);
            }
        }
        name.setText(rightPatient.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeObj();
                Toast.makeText(ChangePatientActivity.this, getString(R.string.changed_patient), Toast.LENGTH_LONG).show();
                addToFile();
                goBack();

            }
        });
    }

    public void changeObj(){
        newCaregiver.changePatient(rightPatient.getId(), name.getText().toString());
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
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("patientid", rightPatient.getId());
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
