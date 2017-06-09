package com.example.strost.logopedist.controller.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.fragments.ExerciseFeedbackTabFragment;
import com.example.strost.logopedist.controller.fragments.ExerciseTabFragment;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Patient;

/**
 * Created by strost on 22-3-2017.
 */

public class ExerciseActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Exercise mExercise;
    private Patient mPatient;
    private Caregiver mCaregiver;

    private final String PATIENT_KEY = "Patient";
    private final String EXERCISE_KEY = "Exercise";
    private final String CAREGIVER_KEY = "Caregiver";
    FloatingActionButton mPatientOverview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExercise = (Exercise) getIntent().getSerializableExtra(EXERCISE_KEY);
        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);


        mPatientOverview = (FloatingActionButton) findViewById(R.id.fabExerciseOverview);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 0);
        }

        mPatientOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientOverviewActivity();
            }
        });

    }

    public void goToPatientOverviewActivity() {
        Intent detailIntent = new Intent(this, ExerciseRatingOverviewActivity.class);
        detailIntent.putExtra(EXERCISE_KEY, mExercise);
        startActivity(detailIntent);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    ExerciseTabFragment tab1 = new ExerciseTabFragment();
                    tab1.setExercise(mExercise);
                    mPatientOverview.hide();
                    return tab1;
                case 1:
                    ExerciseFeedbackTabFragment tab2 = new ExerciseFeedbackTabFragment();
                    tab2.setExercise(mExercise);
                    mPatientOverview.show();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.exercise);
                case 1:
                    return getString(R.string.fill_exercise);
            }
            return null;
        }
    }

    public void audioPlayer(String type) {
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();
        String patientRec = "";

        try {
                patientRec = mExercise.getRecord();
            } catch (Exception e) {

            }

        try {
            if (!patientRec.equals("")) {
                try {
                    mp.setDataSource(mExercise.getRecord());
                    mp.prepare();
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException e){

        }
    }





}
