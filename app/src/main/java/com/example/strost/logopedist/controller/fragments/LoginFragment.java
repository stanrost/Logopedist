package com.example.strost.logopedist.controller.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.LoginActivity;
import com.example.strost.logopedist.controller.activities.MainPageActivity;

/**
 * Created by strost on 23-2-2017.
 */



public class LoginFragment extends Fragment {

    OnButtonClickListener mButtonclick;

    public interface OnButtonClickListener {
        public void nextPage();

    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        super.onAttach(activity);

        try {
            mButtonclick = (OnButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement mButtonclick");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragement, container, false);

        Button loginButton = (Button) rootView.findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextfrPage();

            }
        });
        return rootView;

    }


    public void nextfrPage(){

        mButtonclick.nextPage();
    }
}

