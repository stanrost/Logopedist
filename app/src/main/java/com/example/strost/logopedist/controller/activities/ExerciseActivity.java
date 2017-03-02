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
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.request.GetPatiënt;
import com.example.strost.logopedist.model.request.RemoveExerciseRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 20-2-2017.
 */

public class ExerciseActivity extends AppCompatActivity {
    private List<Exercise> exercises = new ArrayList<Exercise>();
    private Patient rightPatient;
    private Exercise rightExercise;
    private int caregiverId = 0, patientId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        patientId = getIntent().getExtras().getInt("patientId");
        caregiverId = getIntent().getExtras().getInt("caregiverId");
        int exerciseId = getIntent().getExtras().getInt("exerciseId");

        GetPatiënt gp = new GetPatiënt();
        rightPatient = gp.getPatient(patientId, caregiverId);
        exercises = rightPatient.getExercises();

        for (int i = 0; i < exercises.size(); i++) {
            if (exerciseId == exercises.get(i).getId()) {
               rightExercise = exercises.get(i);
            }
        }
        EditText setId = (EditText) findViewById(R.id.exerciseId);
        final EditText setName = (EditText) findViewById(R.id.exerciseName);

        setId.setText(exerciseId+"");
        setName.setText(rightExercise.getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightExercise.setTitle(setName.getText().toString());
                Toast.makeText(ExerciseActivity.this, getString(R.string.changed_exercise), Toast.LENGTH_LONG).show();
                addToFile();
                goBack();
            }
        });
    }

    public void addToFile(){
        final RemoveExerciseRequest rer = new RemoveExerciseRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                rer.removeExercise(rightExercise);
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
        detailIntent.putExtra("caregiverId", caregiverId);
        detailIntent.putExtra("patientid", patientId);
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

                      //  rightPatient.removeExcersice(rightExercise);
                        Toast.makeText(ExerciseActivity.this, getString(R.string.removed_exercise), Toast.LENGTH_LONG).show();

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
        builder.setMessage(getString(R.string.message_remove_exercies)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }


}
