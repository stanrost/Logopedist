package com.example.strost.logopedist.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.fragments.LoginFragment;
import com.example.strost.logopedist.model.entities.Caregiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 23-2-2017.
 */

public class LoginV2Activity extends AppCompatActivity implements LoginFragment.OnButtonClickListener{


    List<Caregiver> caregivers = new ArrayList<Caregiver>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginv2_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getZorg();

    }

    public void getZorg(){
        Backendless.Persistence.of( Caregiver.class).find(new AsyncCallback<BackendlessCollection<Caregiver>>(){
            @Override
            public void handleResponse(BackendlessCollection<Caregiver> response) {
                caregivers = response.getCurrentPage();
            }
            @Override
            public void handleFault(BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }

    @Override
    public void nextPage(){
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        Toast.makeText(LoginV2Activity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
        startActivity(detailIntent);
        finish();
    }
}
