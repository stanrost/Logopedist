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

import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.GetPatiënt;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.RemovePatientRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */


public class PatientActivity extends AppCompatActivity {
    private List<Exercise> exercises = new ArrayList<Exercise>();
    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private Patient rightPatient;
    private int caregiverId;
    private Caregiver caregiver, newCaregiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        int id = getIntent().getExtras().getInt("patientid");
        caregiverId = getIntent().getExtras().getInt("caregiverId");
        getZorgverlender();

        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                caregiver = caregivers.get(i);
            }
        }

        GetPatiënt gp = new GetPatiënt();
        rightPatient = gp.getPatient(id, caregiverId);

        EditText setId = (EditText) findViewById(R.id.patientId);
        EditText setName = (EditText) findViewById(R.id.patientName);

        setId.setText(rightPatient.getId()+ "");
        setName.setText(rightPatient.getName() + "");

        ListView lv = (ListView) findViewById(R.id.Exercise_View);

        exercises = rightPatient.getExercises();

        ArrayAdapter<Exercise> arrayAdapter = new ArrayAdapter<Exercise>(
                this, android.R.layout.simple_list_item_1, exercises);

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
        detailIntent.putExtra("caregiverId", caregiverId);
        detailIntent.putExtra("id", rightPatient.getId());
        startActivity(detailIntent);
        finish();
    }

    public void goToExerciseActivity(int exerciseId) {
        Intent detailIntent = new Intent(this, ExerciseActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        detailIntent.putExtra("patientId", rightPatient.getId());
        detailIntent.putExtra("exerciseId", exerciseId);
        startActivity(detailIntent);
        finish();
    }

    public void goToChangePatientActivity(){
        Intent detailIntent = new Intent(this, ChangePatientActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
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
        final RemovePatientRequest rpr = new RemovePatientRequest();

        Runnable runnable = new Runnable() {
            public void run() {
                rpr.removePatient(rightPatient);
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

                        newCaregiver = caregiver;

                        newCaregiver.removePatient(rightPatient);
                        Toast.makeText(PatientActivity.this, getString(R.string.removed_patient), Toast.LENGTH_LONG).show();
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
        builder.setMessage(getString(R.string.message_remove_patient)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
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

    @Override
    public void onBackPressed() {
        goBack();
        finish();
        return;
    }
}