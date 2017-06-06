package com.example.strost.logopedist.controller.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.IndividualFeedbackActivity;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bram on 30/01/2017.
 */

public class FeedbackDataAdapter extends RecyclerView.Adapter<FeedbackDataAdapter.ViewHolder>{

    private ArrayList<Feedback> mFeedbackList;
    Exercise mExercise;
    private final String FEEDBACK_KEY = "Feedback";
    private final String EXERCISE_KEY = "Exercise";

    public FeedbackDataAdapter(List<Feedback> androidList, Exercise excercise){
        mFeedbackList = (ArrayList<Feedback>) androidList;
        mExercise = excercise;
    }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitle, mDescription;
        private ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById( R.id.tvDate);
            mDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            mImageView = (ImageView) itemView.findViewById(R.id.ivStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFeedbackList.get(getPosition()).getFeedbackDate();
            goToPatientPage(v);
        }

        public void goToPatientPage(View v){
            Intent detailIntent = new Intent(v.getContext(), IndividualFeedbackActivity.class);
            detailIntent.putExtra(EXERCISE_KEY, mExercise);
            detailIntent.putExtra(FEEDBACK_KEY,  mFeedbackList.get(getPosition()));
            v.getContext().startActivity(detailIntent);

        }
    }


    @Override
    public FeedbackDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feedback, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackDataAdapter.ViewHolder holder, int position) {

        switch(mFeedbackList.get(position).getPatientRating()) {
            case 1:
                holder.mImageView.setImageResource(R.drawable.pic_reallysad);
                break;
            case 2:
                holder.mImageView.setImageResource(R.drawable.pic_sad);
                break;
            case 3:
                holder.mImageView.setImageResource(R.drawable.pic_natural);
                break;
            case 4:
                holder.mImageView.setImageResource(R.drawable.pic_happy);
                break;
            case 5:
                holder.mImageView.setImageResource(R.drawable.pic_reallyhappy);
                break;
        }

        holder.mTitle.setText(mFeedbackList.get(position).getFeedbackDate());
        holder.mDescription.setText(mFeedbackList.get(position).getPatientNotes().substring(0, Math.min(mFeedbackList.get(position).getPatientNotes().length(), 35))+"...");
    }

    @Override
    public int getItemCount() {
        return mFeedbackList != null ? mFeedbackList.size() : 0;
    }


}
