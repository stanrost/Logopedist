package com.example.strost.logopedist.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.example.strost.logopedist.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by strost on 30-3-2017.
 */

public class ViewImageActivity extends AppCompatActivity {
    private ImageView mFullScreenImageView;
    private final String PICTURE_KEY = "picture";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);

        String mPicture = getIntent().getStringExtra(PICTURE_KEY);
        mFullScreenImageView = (ImageView) findViewById(R.id.ivFullScreenImage);
        ImageView close = (ImageView) findViewById(R.id.ivClose);
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.pbLoadingImage);
        mProgressBar.bringToFront();

        Picasso.with(this).load(mPicture).fit().centerInside().into(mFullScreenImageView, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
            }
        });

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(mFullScreenImageView);
        pAttacher.update();

        close.bringToFront();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFullImage();
            }
        });
    }

    public void closeFullImage() {
        finish();
    }
}
