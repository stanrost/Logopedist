package com.example.strost.logopedist.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Opdracht;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.GetZorgverlenerRequest;
import com.example.strost.logopedist.model.request.SetZorgverlenerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 20-2-2017.
 */

public class ExerciseActivity extends AppCompatActivity {
    private List<Patient> patients = new ArrayList<Patient>();
    private List<Opdracht> opdrachten = new ArrayList<Opdracht>();
    private Patient rightPatient;
    private Opdracht rightOpdracht;
    private Zorgverlener obj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        int patientId = getIntent().getExtras().getInt("patientId");
        int exerciseId = getIntent().getExtras().getInt("exerciseId");

        GetZorgverlenerRequest zr = new GetZorgverlenerRequest();
        obj = zr.getZorgverlener(this);
        patients = obj.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (patientId == patients.get(i).getId()) {
                 rightPatient = patients.get(i);
            }
        }

        opdrachten = rightPatient.getList();

        for (int i = 0; i < opdrachten.size(); i++) {
            if (exerciseId == opdrachten.get(i).getId()) {
               rightOpdracht = opdrachten.get(i);
            }
        }
        EditText setId = (EditText) findViewById(R.id.exerciseId);
        final EditText setName = (EditText) findViewById(R.id.exerciseName);

        setId.setText(exerciseId+"");
        setName.setText(rightOpdracht.getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightOpdracht.setTitle(setName.getText().toString());
                Toast.makeText(ExerciseActivity.this, "Opdracht is veranderd", Toast.LENGTH_LONG).show();
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
        detailIntent.putExtra("id", rightPatient.getId());
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.removeExercies) {
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    public void openDialog(){


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        rightPatient.removeExcersice(rightOpdracht);
                        Toast.makeText(ExerciseActivity.this, "Opdracht is Verwijderd", Toast.LENGTH_LONG).show();

                        addToFile();
                        goBack();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Weet u zeker dat u de Opdracht wilt verwijderen?").setPositiveButton("Ja", dialogClickListener)
                .setNegativeButton("Nee", dialogClickListener).show();
    }


}
