package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.GetZorgverlenerRequest;
import com.example.strost.logopedist.model.request.SetZorgverlenerRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 17-2-2017.
 */

public class AddExerciseActvitiy extends AppCompatActivity {
    private List<Patient> patients = new ArrayList<Patient>();
    private Patient patient;
    SharedPreferences myPrefs;
    Zorgverlener obj;
    Gson gson;
    GetZorgverlenerRequest zr;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addexercise_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        int id = getIntent().getExtras().getInt("id");

        GetZorgverlenerRequest gZR = new GetZorgverlenerRequest();
        obj = gZR.getZorgverlener(this);

        patients = obj.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (id == patients.get(i).getId()) {
                patient = patients.get(i);
            }
        }


        final EditText title = (EditText) findViewById(R.id.exerciseTitle);

        final int newId = patient.getList().size() + 1;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 patient.addOpdracht(newId, title.getText().toString());
                Toast.makeText(AddExerciseActvitiy.this, "Oefening is Toegevoegd", Toast.LENGTH_LONG).show();
                addToFile();

                goBack();

            }
        });
    }

    public void addToFile(){
        SetZorgverlenerRequest sZR = new SetZorgverlenerRequest();
        sZR.setZorgverlener(obj, this);

    }

    public void goBack(){
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("id", patient.getId());
        startActivity(detailIntent);
        finish();
    }
    @Override
    public void onBackPressed() {
        goBack();
        return;
    }


}
