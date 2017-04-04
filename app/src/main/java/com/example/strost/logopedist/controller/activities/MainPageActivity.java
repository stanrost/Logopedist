package com.example.strost.logopedist.controller.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;
import com.example.strost.logopedist.controller.adapter.DataAdapter;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.CaregiverResponse;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.request.GetCaregiverHttpRequest;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.strost.logopedist.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 16-2-2017.
 */

public class MainPageActivity extends AppCompatActivity {

    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private List<Patient> patients = new ArrayList<Patient>();
    private ArrayList<Patient> patientslist = new ArrayList<Patient>();

    private ArrayList<String> listItems = new ArrayList<String>();
    private String[] items;
    private ArrayAdapter<String> adapter;

    private int patientId, caregiverId;

    private ListView lv;
    private EditText searchtext;


    private RecyclerView mRecyclerView;

    private CompositeDisposable mCompositeDisposable;

    private DataAdapter mAdapter;
    private List<Patient> mAndroidArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //lv = (ListView) findViewById(R.id.patient_listview);
        caregiverId = getIntent().getExtras().getInt("caregiverId");
        initRecyclerView();
        getZorgverlender();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddPatientActivity();
            }
        });

    }


    public void searchItem(String textToSearch) {

        for (Patient p : patients) {
            if (p.getName().toUpperCase().contains(textToSearch.toUpperCase()) && !mAndroidArrayList.contains(p)) {
                mAndroidArrayList.add(p);
            }
        }
        for (int i = 0; i < patients.size(); i++) {
            if (!patients.get(i).getName().toUpperCase().contains(textToSearch.toUpperCase())) {
                mAndroidArrayList.remove(i);
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

    public void getZorgverlender() {
        GetCaregiverHttpRequest gcr = new GetCaregiverHttpRequest();
        caregivers = gcr.getCaregiver();

        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(restAdapter.getCaregivers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<CaregiverResponse>() {
                    @Override
                    public void onNext(CaregiverResponse value) {
                        caregivers = value.getData();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        setPatientsList();
                    }
                }));


    }


    public void setPatientsList() {
        Caregiver z1 = null;
        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                z1 = caregivers.get(i);
            }
        }
        patients = z1.getPatients();
        Collections.sort(patients, new Comparator<Patient>() {
            @Override
            public int compare(Patient patient1, Patient patient2) {
                String id1 = patient1.getName() + "";
                String id2 = patient2.getName() + "";
                return id1.compareTo(id2);
            }
        });

        mAndroidArrayList = patients;
        patientslist = (ArrayList<Patient>) patients;
        mAdapter = new DataAdapter((ArrayList<Patient>) mAndroidArrayList, z1);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void goToPatientPage(int patientId) {
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        detailIntent.putExtra("patientid", patientId);
        startActivity(detailIntent);
        finish();

    }

    public void goToAddPatientActivity() {

        Intent detailIntent = new Intent(this, AddPatientActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        startActivity(detailIntent);
        finish();
    }

    public void goToSettingsActvivity() {
        Intent detailIntent = new Intent(this, SettingsActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO write your code what you want to perform on search
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.settings) {
            goToSettingsActvivity();
        }

        return super.onOptionsItemSelected(item);
    }

}