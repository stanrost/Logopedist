package com.example.strost.logopedist.controller.activities.admin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.AddPatientActivity;
import com.example.strost.logopedist.controller.activities.MainPageActivity;
import com.example.strost.logopedist.controller.adapter.CaregiverDataAdapter;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.GetCaregiverHttpRequest;

import java.util.ArrayList;
import java.util.List;

public class CaregiverListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Caregiver mCaregiver;
    private List<Caregiver> mCaregivers = new ArrayList<Caregiver>();
    private CaregiverDataAdapter mAdapter;
    private final String CAREGIVER_ID_KEY = "caregiverId";
    private final String CAREGIVER_KEY = "Caregiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverlist);
        FloatingActionButton fabAddCaregiver = (FloatingActionButton) findViewById(R.id.fabAddCaregiver);
        mCaregiver = (Caregiver) getIntent().getSerializableExtra(CAREGIVER_KEY);

        initRecyclerView();
        getCaregivers();

        fabAddCaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddCaregiver();
            }
        });
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
        setCaregiverList();

    }

    public void setCaregiverList() {
        mAdapter = new CaregiverDataAdapter((ArrayList<Caregiver>) mCaregivers, mCaregiver);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void goToAddCaregiver() {
        int newId = 0;

        for (Caregiver c : mCaregivers) {
            if (c.getId() > newId) {
                newId = c.getId();
            }
        }
        newId++;
        Intent detailIntent = new Intent(this, AddCaregiverActivity.class);
        detailIntent.putExtra(CAREGIVER_ID_KEY, newId);
        detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
        startActivity(detailIntent);
        finish();
    }

    public void goBack() {
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        detailIntent.putExtra(CAREGIVER_ID_KEY, mCaregiver.getId());
        this.startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

}
