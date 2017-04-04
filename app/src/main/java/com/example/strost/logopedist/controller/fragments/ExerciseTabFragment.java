package com.example.strost.logopedist.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.strost.logopedist.NetworkImageView;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Exercise;


/**
 * Created by strost on 23-3-2017.
 */

public class ExerciseTabFragment extends Fragment {

    Exercise rightExercise;
    private NetworkImageView fullScreenImage;
    EditText setId;
    EditText setName;
    EditText description;
    TextView help;
    NetworkImageView appImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercisetab, container, false);
        setId = (EditText) rootView.findViewById(R.id.exerciseId);
        setName = (EditText) rootView.findViewById(R.id.exerciseName);
        description = (EditText) rootView.findViewById(R.id.notesText);
        help = (TextView) rootView.findViewById(R.id.needhelp);
        appImageView = (NetworkImageView) rootView.findViewById(R.id.image);
        fullScreenImage = (NetworkImageView) rootView.findViewById(R.id.fullImage);
        final NestedScrollView nsv = (NestedScrollView) rootView.findViewById(R.id.scrollview);


        setId.setText(rightExercise.getId()+"");
        setName.setText(rightExercise.getTitle());
        description.setText(rightExercise.getDescription());

        if(rightExercise.getHelp() == true){
            help.setText("Ja");
        }
        else{
            help.setText("nee");
        }

        appImageView.setUrl(rightExercise.getPicture());

        return rootView;
    }

    public void setExercise(Exercise exercise) {
        rightExercise = exercise;
    }


}
