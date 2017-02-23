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
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.GetZorgverlenerRequest;
import com.example.strost.logopedist.model.request.SetZorgverlenerRequest;

/**
 * Created by strost on 20-2-2017.
 */
public class AddPatientActivity extends AppCompatActivity{
    Zorgverlener obj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpatient_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        GetZorgverlenerRequest gZR = new GetZorgverlenerRequest();
        obj = gZR.getZorgverlener(this);

        final EditText name = (EditText) findViewById(R.id.AddPatientName);

        final int id = obj.getPatients().size() + 1;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                obj.addPatient(id, name.getText().toString());
                Toast.makeText(AddPatientActivity.this, "Patient is Toegevoegd", Toast.LENGTH_LONG).show();
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
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }

}
