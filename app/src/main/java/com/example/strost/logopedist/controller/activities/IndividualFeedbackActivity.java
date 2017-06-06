package com.example.strost.logopedist.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Feedback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class IndividualFeedbackActivity extends AppCompatActivity {

    private RadioGroup mRating;
    private Exercise mExercise;
    private TextView mNotes, mDate;
    private Switch mHelp;
    private int mCount;
    ImageView mImageView;
    private Feedback mFeedback;
    private final String EXERCISE_KEY = "Exercise";
    private final String FEEDBACK_KEY = "Feedback";
    private final String PICTURE_KEY = "picture";
    private final String OFFLINE_PICTURE_KEY = "offlinePicture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individualfeedback);


        mDate = (TextView) this.findViewById(R.id.tvDate);
        mHelp = (Switch) this.findViewById(R.id.swtchGotHelp);
        mImageView = (ImageView) this.findViewById(R.id.ivPreviewImage);
        mNotes = (TextView) this.findViewById(R.id.tvDescription);
        final NestedScrollView nsv = (NestedScrollView) this.findViewById(R.id.scrollview);
        final ProgressBar mProgress = (ProgressBar) this.findViewById(R.id.pbLoadingImage);
        final Button mPlay = (Button) this.findViewById(R.id.btnPlayAudio);

        mFeedback = (Feedback) getIntent().getSerializableExtra(FEEDBACK_KEY);
        mExercise = (Exercise) getIntent().getSerializableExtra(EXERCISE_KEY);

        mRating = (RadioGroup) this.findViewById(R.id.rgRating);
        setIndexRating(mFeedback.getPatientRating());
        mHelp.setChecked(mFeedback.getPatientHelp());
        mNotes.setText(mFeedback.getPatientNotes());
        showPicture(mProgress);

        mDate.setText(getString(R.string.date)+" " + mFeedback.getFeedbackDate());

        nsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nsv.clearFocus();
                return false;
            }
        });

        try {
            Picasso.with(this).load(mFeedback.getPatientPicture()).into(mImageView);
        } catch (NullPointerException e) {
            mImageView.setVisibility(View.GONE);
        }

        try {
            if (!mFeedback.getPatientRecord().equals("")) {
                mPlay.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
        }

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayer();
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotofullimage();
            }
        });

    }

    public void audioPlayer() {
        //set up MediaPlayer
        MediaPlayer mp = new MediaPlayer();
        String patientRec = "";
        try {
            mp.setDataSource(mFeedback.getPatientRecord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

    }


    public void gotofullimage() {
        Intent detailIntent = new Intent(this, ViewImageActivity.class);
        detailIntent.putExtra(PICTURE_KEY, mFeedback.getPatientPicture());
        detailIntent.putExtra(OFFLINE_PICTURE_KEY, "");
        startActivity(detailIntent);
    }


    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    private void showPicture(final ProgressBar progress) {
        try {
            if (!mFeedback.getPatientPicture().equals(null)) {
                progress.setVisibility(View.VISIBLE);
                Picasso.with(this).load(mFeedback.getPatientPicture()).into(mImageView, new Callback() {
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

    }

    public void setIndexRating(int index) {
        switch (index) {
            case 1:
                RadioButton test = (RadioButton) this.findViewById(R.id.rbReallyHappy);
                test.setChecked(true);
                break;
            case 2:
                RadioButton ra2 = (RadioButton) this.findViewById(R.id.rbHappy);
                ra2.setChecked(true);
                break;
            case 3:
                RadioButton ra3 = (RadioButton) this.findViewById(R.id.rbNatural);
                ra3.setChecked(true);
                break;
            case 4:
                RadioButton ra4 = (RadioButton) this.findViewById(R.id.rbSad);
                ra4.setChecked(true);
                break;
            case 5:
                RadioButton ra5 = (RadioButton) this.findViewById(R.id.rbReallySad);
                ra5.setChecked(true);
                break;
        }

    }
}
