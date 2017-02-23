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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.strost.logopedist.model.entities.Opdracht;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.GetZorgverlenerRequest;
import com.example.strost.logopedist.model.request.SetZorgverlenerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */


public class PatientActivity extends AppCompatActivity {
    private List<Patient> patients = new ArrayList<Patient>();
    private List<Opdracht> opdrachten = new ArrayList<Opdracht>();
    private Patient rightPatient;
    Zorgverlener obj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        int id = getIntent().getExtras().getInt("id");

        GetZorgverlenerRequest zr = new GetZorgverlenerRequest();
        obj = zr.getZorgverlener(this);
        patients = obj.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (id == patients.get(i).getId()) {
                 rightPatient = patients.get(i);
            }
        }


        EditText setId = (EditText) findViewById(R.id.patientId);
        EditText setName = (EditText) findViewById(R.id.patientName);

        setId.setText(rightPatient.getId()+ "");
        setName.setText(rightPatient.getName() + "");

        ListView lv = (ListView) findViewById(R.id.opdrachtenView);

        opdrachten = rightPatient.getList();

        ArrayAdapter<Opdracht> arrayAdapter = new ArrayAdapter<Opdracht>(
                this, android.R.layout.simple_list_item_1, opdrachten);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String filename = ""+ parent.getItemAtPosition(position);     // full file name
                String[] parts = filename.split("\\."); // String array, each element is text between dots

                String beforeFirstDot = parts[0];
                int exerciseId = Integer.parseInt(beforeFirstDot);

                goToExerciseActivity(exerciseId);

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddExerciseActivity();
            }
        });

    }

    public void goToAddExerciseActivity(){
        Intent detailIntent = new Intent(this, AddExerciseActvitiy.class);
        detailIntent.putExtra("id", rightPatient.getId());
        startActivity(detailIntent);
        finish();
    }

    public void goToExerciseActivity(int exerciseId) {
        Intent detailIntent = new Intent(this, ExerciseActivity.class);
        detailIntent.putExtra("patientId", rightPatient.getId());
        detailIntent.putExtra("exerciseId", exerciseId);
        startActivity(detailIntent);
        finish();
    }

    public void goToChangePatientActivity(){
        Intent detailIntent = new Intent(this, ChangePatientActivity.class);
        detailIntent.putExtra("id", rightPatient.getId());
        startActivity(detailIntent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.zorgverlener_patientmenu, menu);
        return true;
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addExercise) {
            goToAddExerciseActivity();
        }
        if (id == R.id.changePatient) {
            goToChangePatientActivity();
        }
        if (id == R.id.removePatient) {
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

                        obj.removePatient(rightPatient);
                        Toast.makeText(PatientActivity.this, "Patient is verwijderd", Toast.LENGTH_LONG).show();
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
        builder.setMessage("Weet u zeker dat u de Patiënt wilt verwijderen?").setPositiveButton("Ja", dialogClickListener)
                .setNegativeButton("Nee", dialogClickListener).show();
    }

    @Override
    public void onBackPressed() {
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        startActivity(detailIntent);
        finish();
        return;
    }
}