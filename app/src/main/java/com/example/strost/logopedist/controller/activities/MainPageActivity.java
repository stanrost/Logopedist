package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Zorgverlener;
import com.example.strost.logopedist.model.request.GetZorgverlenerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class MainPageActivity extends AppCompatActivity {
    private int patientId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ListView lv = (ListView) findViewById(R.id.patient_listview);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).

        GetZorgverlenerRequest zr = new GetZorgverlenerRequest();
        Zorgverlener z1 = zr.getZorgverlener(this);
        List<Patient> patients = new ArrayList<Patient>();
        patients = z1.getPatients();


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<Patient> arrayAdapter = new ArrayAdapter<Patient>(
                this, android.R.layout.simple_list_item_1, patients);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String filename = ""+ parent.getItemAtPosition(position);     // full file name
                String[] parts = filename.split("\\."); // String array, each element is text between dots

                String beforeFirstDot = parts[0];
                patientId = Integer.parseInt(beforeFirstDot);


                goToPatientPage();


            }
        });


    }

    public void goToPatientPage(){
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("id", patientId);
        startActivity(detailIntent);
        finish();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.zorgverlener_menu, menu);
        return true;
    }

    public void goToAddPatientActivity(){
        finish();
        Intent detailIntent = new Intent(this, AddPatientActivity.class);
        startActivity(detailIntent);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}