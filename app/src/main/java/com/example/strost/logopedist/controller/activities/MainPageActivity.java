package com.example.strost.logopedist.controller.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.strost.logopedist.controller.activities.admin.CaregiverListActivity;
import com.example.strost.logopedist.controller.adapter.PatientDataAdapter;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.request.GetCaregiverHttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class MainPageActivity extends AppCompatActivity {

    private Caregiver mCaregiver;
    private List<Caregiver> mCaregivers = new ArrayList<Caregiver>();
    private List<Patient> mPatients = new ArrayList<Patient>();
    private int mCaregiverId;
    private RecyclerView mRecyclerView;
    private PatientDataAdapter mAdapter;
    private List<Patient> mAndroidArrayList;

    private final String CAREGIVER_KEY = "Caregiver";
    private final String CAREGIVER_ID_KEY = "caregiverId";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FloatingActionButton fabAddPatient = (FloatingActionButton) findViewById(R.id.fabAddPatient);

        mCaregiverId = getIntent().getExtras().getInt(CAREGIVER_ID_KEY);
        initRecyclerView();
        getCaregivers();
        fabAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddPatientActivity();
            }
        });

    }


    public void searchItem(String textToSearch) {
        for (int i = 0; i < mPatients.size(); i++) {
            if (!mPatients.get(i).getFirstName().toUpperCase().contains(textToSearch.toUpperCase())) {
                mAndroidArrayList.remove(i);
            } else if (mPatients.get(i).getFirstName().toUpperCase().contains(textToSearch.toUpperCase()) && !mAndroidArrayList.contains(mPatients.get(i))) {
                mAndroidArrayList.add(mPatients.get(i));
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void getCaregivers() {
        GetCaregiverHttpRequest gcr = new GetCaregiverHttpRequest();
        mCaregivers = gcr.getCaregiver();
        setPatientsList();

    }


    public void setPatientsList() {
        for (int i = 0; i < mCaregivers.size(); i++) {
            if (mCaregiverId == mCaregivers.get(i).getId()) {
                mCaregiver = mCaregivers.get(i);
            }
        }
        try{
        mPatients = mCaregiver.getPatients();
        }
        catch(NullPointerException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.no_internet_connection))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        Collections.sort(mPatients, new Comparator<Patient>() {
            @Override
            public int compare(Patient patient1, Patient patient2) {
                String patientName1 = patient1.getFirstName() + "";
                String patientName2 = patient2.getFirstName() + "";
                return patientName1.compareTo(patientName2);
            }
        });

        mAndroidArrayList = mPatients;
        mAdapter = new PatientDataAdapter((ArrayList<Patient>) mAndroidArrayList, mCaregiver);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void goToAddPatientActivity() {

        Intent detailIntent = new Intent(this, AddPatientActivity.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
        finish();
    }

    public void goToSettingsActvivity() {
        Intent detailIntent = new Intent(this, SettingsActivity.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
        finish();
    }
    public void goToCaregiverListActivity() {
        Intent detailIntent = new Intent(this, CaregiverListActivity.class);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchView searchView = null;
        if(mCaregiver.getIsAdmin()==true){
            getMenuInflater().inflate(R.menu.menu_searchadmin, menu);
        }
        else if(mCaregiver.getIsAdmin()==false) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchItem(query.toString());

                return true;
            }
        });
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            goToSettingsActvivity();
        }
        if(id == R.id.caregiverList){
            goToCaregiverListActivity();
        }
        return super.onOptionsItemSelected(item);
    }

}