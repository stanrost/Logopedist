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
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.GetZorgverlenerRequest;
import com.example.strost.logopedist.model.request.SetZorgverlenerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 20-2-2017.
 */

public class ChangePatientActivity extends AppCompatActivity {

    Zorgverlener obj;
    private List<Patient> patients = new ArrayList<Patient>();
    Patient rightPatient;
    EditText name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepatient_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        GetZorgverlenerRequest gZR = new GetZorgverlenerRequest();
        obj = gZR.getZorgverlener(this);

        name = (EditText) findViewById(R.id.changePatientName);

        final int id = getIntent().getExtras().getInt("id");

        GetZorgverlenerRequest zr = new GetZorgverlenerRequest();
        obj = zr.getZorgverlener(this);
        patients = obj.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (id == patients.get(i).getId()) {
                rightPatient = patients.get(i);
            }
        }
        name.setText(rightPatient.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeObj();
                Toast.makeText(ChangePatientActivity.this, "Patient is gewijzigd", Toast.LENGTH_LONG).show();
                addToFile();
                goBack();

            }
        });
    }

    public void changeObj(){
        obj.changePatient(rightPatient.getId(), name.getText().toString());
    }

    public void addToFile(){
        SetZorgverlenerRequest sZR = new SetZorgverlenerRequest();
        sZR.setZorgverlener(obj, this);

    }

    public void goBack(){
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("id", rightPatient.getId());
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }

}
