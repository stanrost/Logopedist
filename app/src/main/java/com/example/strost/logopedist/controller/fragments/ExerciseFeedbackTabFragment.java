package com.example.strost.logopedist.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.IndividualFeedbackActivity;
import com.example.strost.logopedist.controller.adapter.FeedbackDataAdapter;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Feedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by strost on 5-5-2017.
 */

public class ExerciseFeedbackTabFragment extends Fragment {


    Exercise mExercise;
    private List<Feedback> mFeedbackList = new ArrayList<Feedback>();
    private final String EXERCISE_KEY = "Exercise";
    private final String FEEDBACK_KEY = "Feedback";
    private RecyclerView mRecyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercisefeedbacktab, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.lvFeedback);


        mFeedbackList = mExercise.getFeedback();
        Collections.sort(mFeedbackList, new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                return feedback2.getFeedbackDate().compareToIgnoreCase(feedback1.getFeedbackDate());
            }
        });
        
        ArrayAdapter exercisesAdapter = new ArrayAdapter(rootView.getContext(), android.R.layout.simple_list_item_1, mFeedbackList);

        listView.setAdapter(exercisesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToFeedback(rootView, (Feedback) parent.getItemAtPosition(position));
            }
        });
        initRecyclerView(rootView);
        return rootView;
    }

    private void initRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvFeedback);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        setFeedbackRecyclerView();
    }

    public void setFeedbackRecyclerView(){

        FeedbackDataAdapter mAdapter = new FeedbackDataAdapter(mExercise.getFeedback(), mExercise);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void goToFeedback(View rootView, Feedback feedback){
            Intent detailIntent = new Intent(rootView.getContext(), IndividualFeedbackActivity.class);
            detailIntent.putExtra(EXERCISE_KEY, mExercise);
            detailIntent.putExtra(FEEDBACK_KEY, feedback);
            startActivity(detailIntent);

    }
    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }
}
