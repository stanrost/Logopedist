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
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 17-2-2017.
 */

public class AddExerciseActvitiy extends AppCompatActivity {
    private List<Patient> patients = new ArrayList<Patient>();
    private Patient patient;
    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private int caregiverId;
    private Caregiver caregiver, newCaregiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addexercise_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        int patientId = getIntent().getExtras().getInt("id");
        caregiverId = getIntent().getExtras().getInt("caregiverId");

        getZorgverlender();

        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                caregiver = caregivers.get(i);
            }
        }

        newCaregiver = caregiver;
        patients = newCaregiver.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (patientId == patients.get(i).getId()) {
                patient = patients.get(i);
            }
        }

        final EditText title = (EditText) findViewById(R.id.exerciseTitle);
        final int newId = patient.getExercises().size() + 1;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient.addExercise(newId, title.getText().toString());
                Toast.makeText(AddExerciseActvitiy.this, getString(R.string.added_exercise), Toast.LENGTH_LONG).show();
                addToFile();
                goBack();
            }
        });
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

    public void getZorgverlender() {
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
    }


    public void goBack(){
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("patientid", patient.getId());
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
