package com.example.strost.logopedist.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.GetPatientHttpRequest;
import com.example.strost.logopedist.model.request.DeletePatientHttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */


public class PatientActivity extends AppCompatActivity {
    private List<Exercise> exercises = new ArrayList<Exercise>();
    private Patient mPatient, mOldPatient;
    private Caregiver mCaregiver, newCaregiver;
    private int mCaregiverId;

    private final String EXERCISE_KEY = "Exercise";
    private final String CAREGIVER_KEY = "Caregiver";
    private final String CAREGIVER_ID_KEY = "caregiverId";
    private final String PATIENT_KEY = "Patient";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOldPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);
        mCaregiverId = mCaregiver.getId();

        FloatingActionButton mAddExercise = (FloatingActionButton) findViewById(R.id.fabAddExercise);
        LinearLayout testContainer = (LinearLayout) findViewById(R.id.llExercises);
        FloatingActionButton mChangePatient = (FloatingActionButton) findViewById(R.id.fabChangePatient);
        FloatingActionButton mFABPatientOverview = (FloatingActionButton) findViewById(R.id.fabOverviewPatient);


        GetPatientHttpRequest gPR = new GetPatientHttpRequest();
        mPatient = gPR.getPatient(mOldPatient);

        try {
            exercises = mPatient.getExercises();
            if(mPatient.getExercises().size() == 0){
                mFABPatientOverview.setVisibility(View.GONE);
            }
        }catch(NullPointerException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.no_internet_connection))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            exercises = mOldPatient.getExercises();
            mPatient = mOldPatient;
            if(mOldPatient.getExercises().size() == 0){
                mFABPatientOverview.setVisibility(View.GONE);
            }
        }


        Collections.sort(exercises, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise exercise1, Exercise exercise2) {
                int exerciseId1 = exercise1.getId();
                int exercieseId2 = exercise2.getId();
                return exercieseId2 > exerciseId1 ? +1 : exercieseId2 < exerciseId1 ? -1 : 0;
            }
        });

        ArrayAdapter exercisesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, exercises);

        final int adapterCount = exercisesAdapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = exercisesAdapter.getView(i, null, null);

            testContainer.addView(item);
            final int exerciseIndex = i;
            item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            goToExerciseActivity(exercises.get(exerciseIndex));
                                        }
                                    }
            );
        }

        mAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddExerciseActivity();
            }
        });
        mChangePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChangePatientActivity();
            }
        });


        mFABPatientOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientOverviewActivity();
            }
        });

        setTitle(mPatient.getFirstName() + " " + mPatient.getLastName() );

    }

    public void goToPatientOverviewActivity() {
        Intent detailIntent = new Intent(this, NumberOfTimesOverviewActivity.class);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
    }

    public void goToAddExerciseActivity() {
        Intent detailIntent = new Intent(this, AddExerciseActvitiy.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
        finish();
    }

    public void goToExerciseActivity(Exercise exercise) {
        Intent detailIntent = new Intent(this, ExerciseActivity.class);
        detailIntent.putExtra(EXERCISE_KEY, exercise);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
        finish();
    }

    public void goToChangePatientActivity() {
        Intent detailIntent = new Intent(this, ChangePatientActivity.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_caregiverandpatient, menu);
        return true;
    }

    public void addToFile() {
        final DeletePatientHttpRequest rpr = new DeletePatientHttpRequest();

        Runnable runnable = new Runnable() {
            public void run() {
                rpr.removePatient(mPatient);
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

    public void goBack() {
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        detailIntent.putExtra(CAREGIVER_ID_KEY, mCaregiverId);
        startActivity(detailIntent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

    public void openDialog() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        newCaregiver = mCaregiver;
                        newCaregiver.removePatient(mPatient);
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

    @Override
    public void onBackPressed() {
        goBack();
        finish();
        return;
    }
}