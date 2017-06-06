package com.example.strost.logopedist.controller.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.admin.CaregiverActivity;
import com.example.strost.logopedist.controller.activities.admin.CaregiverListActivity;
import com.example.strost.logopedist.model.entities.Caregiver;

import java.util.ArrayList;

import static weborb.util.ThreadContext.context;

/**
 * Created by strost on 15-5-2017.
 */

public class CaregiverDataAdapter extends RecyclerView.Adapter<CaregiverDataAdapter.ViewHolder>{

    private ArrayList<Caregiver> mCaregiverList;
    private Caregiver mCaregiver;
    private final String CAREGIVER_KEY = "Caregiver";
    private final String CAREGIVER_USER_KEY = "CaregiverUser";
    public CaregiverDataAdapter(ArrayList<Caregiver> androidList, Caregiver caregiver){
        mCaregiverList = androidList;
        mCaregiver = caregiver;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitle, mDescription;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById( R.id.tvTitle);
            mDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCaregiverList.get(getPosition()).getFirstName();
            goToCaregiverPage(v);
        }

        public void goToCaregiverPage(View v){
            Intent detailIntent = new Intent(v.getContext(), CaregiverActivity.class);
            detailIntent.putExtra(CAREGIVER_KEY, mCaregiverList.get(getPosition()));
            detailIntent.putExtra(CAREGIVER_USER_KEY, mCaregiver);

            v.getContext().startActivity(detailIntent);
            ((CaregiverListActivity)v.getContext()).finish();

        }
    }


    @Override
    public CaregiverDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_person, parent, false);

        return new CaregiverDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CaregiverDataAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mCaregiverList.get(position).getFirstName()+ " " + mCaregiverList.get(position).getLastName());
        holder.mDescription.setText(mCaregiverList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return mCaregiverList != null ? mCaregiverList.size() : 0;
    }


}