package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.request.GetCaregiverRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class MainPageActivity extends AppCompatActivity {

    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private List<Patient> patients = new ArrayList<Patient>();
    private ArrayList<String> listItems = new ArrayList<String>();
    private String[] items;
    private ArrayAdapter<String> adapter;

    private int patientId, caregiverId;

    private ListView lv;
    private EditText searchtext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        lv = (ListView) findViewById(R.id.patient_listview);
        searchtext = (EditText) findViewById(R.id.searchList);
        caregiverId = getIntent().getExtras().getInt("caregiverId");

        getZorgverlender();
        Caregiver z1 = null;

        Log.e("caregiverId", "" + caregiverId);
        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                z1 = caregivers.get(i);
            }
        }

        patients = z1.getPatients();
        initList();
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchItem(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void searchItem(String textToSearch){
        for (String item : items){
            if(!item.toUpperCase().contains(textToSearch.toUpperCase())){
                listItems.remove(item);
            }

            if(item.toUpperCase().contains(textToSearch.toUpperCase()) && !listItems.contains(item)){
                listItems.add(item);
            }
        }
        Collections.sort(listItems);
        adapter.notifyDataSetChanged();
    }

    public void initList(){

        for(Patient p : patients){
            listItems.add(p.getId() + ".  " + p.getName());
        }
        Collections.sort(listItems);
        items = listItems.toArray(new String[listItems.size()]);

        adapter = new ArrayAdapter<String>(
                this, R.layout.list_item, R.id.txtitem, listItems);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String filename = ""+ parent.getItemAtPosition(position);     // full file name
                String[] parts = filename.split("\\."); // String array, each element is text between dots
                String beforeFirstDot = parts[0];
                patientId = Integer.parseInt(beforeFirstDot);

                goToPatientPage(caregiverId);
            }
        });

    }


    public void getZorgverlender(){
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

    public void goToPatientPage(int caregiverId){
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        detailIntent.putExtra("patientid", patientId);
        startActivity(detailIntent);
        finish();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.zorgverlener_menu, menu);
        return true;
    }

    public void goToAddPatientActivity(){

        Intent detailIntent = new Intent(this, AddPatientActivity.class);
        detailIntent.putExtra("caregiverId", caregiverId);
        startActivity(detailIntent);
        finish();
    }

    public void goToSettingsActvivity(){
        Intent detailIntent = new Intent(this, SettingsActivity.class);
        startActivity(detailIntent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addPatiënt) {
            goToAddPatientActivity();
        }
        if (id == R.id.settings) {
            goToSettingsActvivity();
        }

        return super.onOptionsItemSelected(item);
    }

}