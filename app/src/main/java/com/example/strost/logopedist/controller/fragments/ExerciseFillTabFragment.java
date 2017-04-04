package com.example.strost.logopedist.controller.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Exercise;
import com.squareup.picasso.Picasso;


/**
 * Created by strost on 23-3-2017.
 */

public class ExerciseFillTabFragment extends Fragment {

    RadioGroup rating;
    Exercise rightExercise;
    TextView counter;
    Switch help;
    EditText notes;

    int count;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fillexersicetab, container, false);


        counter = (TextView) rootView.findViewById(R.id.counter);
        help = (Switch) rootView.findViewById(R.id.gotHelp);
        ImageView previewImage = (ImageView) rootView.findViewById(R.id.previewImage);
        notes = (EditText) rootView.findViewById(R.id.notesText);
        final NestedScrollView nsv = (NestedScrollView) rootView.findViewById(R.id.scrollview);
        nsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nsv.clearFocus();
                return false;
            }
        });
        //Log.e("picture", rightExercise.getPatientPicture());
        try {
            Picasso.with(getContext()).load(rightExercise.getPatientPicture()).into(previewImage);
        }catch (NullPointerException e){}

        rating = (RadioGroup) rootView.findViewById(R.id.rating);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        setIndexRating(rightExercise.getPatientRating(), rootView);
        count = rightExercise.getPatientNumberOfTimes();
        counter.setText(count +"");
        help.setChecked(rightExercise.getPatientHelp());
        notes.setText(rightExercise.getPatientNotes());

        return rootView;
    }

    public void setExercise(Exercise exercise) {
        rightExercise = exercise;
    }


    public void setIndexRating(int index, View rootView){
        switch (index) {
            case 1:
                RadioButton test = (RadioButton) rootView.findViewById(R.id.reallyhappy);
                test.setChecked(true);
                break;
            case 2:
                RadioButton ra2 = (RadioButton) rootView.findViewById(R.id.happy);
                ra2.setChecked(true);
                break;
            case 3:
                RadioButton ra3 = (RadioButton) rootView.findViewById(R.id.natural);
                ra3.setChecked(true);
                break;
            case 4:
                RadioButton ra4 = (RadioButton) rootView.findViewById(R.id.sad);
                ra4.setChecked(true);
                break;
            case 5:
                RadioButton ra5 = (RadioButton) rootView.findViewById(R.id.reallysad);
                ra5.setChecked(true);
                break;
        }

    }

    public int getIndexRating(){
        int rgid = rating.getCheckedRadioButtonId();

        int index = 0;
        if (rgid == R.id.reallyhappy) {
            index = 1;
        }

        if (rgid == R.id.happy) {
            index = 2;
        }

        if (rgid == R.id.natural) {
            index = 3;
        }

        if (rgid == R.id.sad) {
            index = 4;
        }

        if (rgid == R.id.reallysad) {
            index = 5;
        }

        return index;
    }
}
