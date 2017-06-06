package com.example.strost.logopedist.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.strost.logopedist.NetworkImageView;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.ExerciseActivity;
import com.example.strost.logopedist.controller.activities.ViewImageActivity;
import com.example.strost.logopedist.model.entities.Exercise;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by strost on 23-3-2017.
 */

public class ExerciseTabFragment extends Fragment {

    private Exercise mExercise;
    private EditText mSetId;
    private TextView mHelp, mPictureText, mSetDescription, mSetName;
    private NetworkImageView mImageView;
    private final String EXERCISE_KEY = "Exercise";
    private final String CAREGIVER_KEY = "Caregiver";
    private final String PICTURE_KEY = "picture";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercisetab, container, false);
        mSetId = (EditText) rootView.findViewById(R.id.etExerciseId);
        mSetName = (TextView) rootView.findViewById(R.id.tvExerciseName);
        mSetDescription = (TextView) rootView.findViewById(R.id.tvCaregiverNote);
        mHelp = (TextView) rootView.findViewById(R.id.tvNeedhelp);
        mImageView = (NetworkImageView) rootView.findViewById(R.id.ivCaregiverImage);
        final ProgressBar mProgressBar = (ProgressBar) rootView.findViewById(R.id.pbLoadingImage);
        final Button mPlay = (Button) rootView.findViewById(R.id.btnPlayAudio);

        mPictureText = (TextView) rootView.findViewById(R.id.tvPicture);

        showPicture(mProgressBar);

        mSetId.setText(mExercise.getId() + "");
        mSetName.setText(mExercise.getTitle());
        mSetDescription.setText(mExercise.getDescription());

        if (mExercise.getHelp() == true) {
            mHelp.setText(getString(R.string.yes));
        } else {
            mHelp.setText(getString(R.string.no));
        }

        try {
            if (!mExercise.getRecord().equals("")) {
                mPlay.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
        }

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExerciseActivity) getActivity()).audioPlayer(CAREGIVER_KEY);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotofullimage(rootView);
            }
        });
        return rootView;
    }

    public void gotofullimage(View rootview) {
        Intent detailIntent = new Intent(rootview.getContext(), ViewImageActivity.class);
        detailIntent.putExtra(EXERCISE_KEY, mExercise);
        detailIntent.putExtra(PICTURE_KEY, mExercise.getPicture());
        startActivity(detailIntent);
    }


    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    private void showPicture(final ProgressBar progress) {
        try {
            if (!mExercise.getPicture().equals(null)) {
                progress.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(mExercise.getPicture()).into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        noPicture(progress);
                    }
                });
                mImageView.bringToFront();
                mImageView.setUrl(mExercise.getPicture());
            } else {
                noPicture(progress);
            }
        } catch (NullPointerException e) {
            noPicture(progress);
        }
    }

    public void noPicture(ProgressBar progress) {
        progress.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        mPictureText.setVisibility(View.GONE);

    }

}
