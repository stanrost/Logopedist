package com.example.strost.logopedist.controller.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.PatientActivity;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;
import java.util.ArrayList;

/**
 * Created by Bram on 30/01/2017.
 */

public class PatientDataAdapter extends RecyclerView.Adapter<PatientDataAdapter.ViewHolder>{

    private ArrayList<Patient> mPatientList;
    private Caregiver mCaregiver;
    private final String CAREGIVER_KEY = "Caregiver";
    private final String PATIENT_KEY = "Patient";

    public PatientDataAdapter(ArrayList<Patient> androidList, Caregiver caregiver){
        mPatientList = androidList;
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
            mPatientList.get(getPosition()).getFirstName();
            goToPatientPage(v);
        }

        public void goToPatientPage(View v){
            Intent detailIntent = new Intent(v.getContext(), PatientActivity.class);
            detailIntent.putExtra(CAREGIVER_KEY, mCaregiver);
            detailIntent.putExtra(PATIENT_KEY, mPatientList.get(getPosition()));
            v.getContext().startActivity(detailIntent);


        }
    }


    @Override
    public PatientDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_person, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientDataAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mPatientList.get(position).getFirstName() + " " + mPatientList.get(position).getLastName());
        holder.mDescription.setText(mPatientList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return mPatientList != null ? mPatientList.size() : 0;
    }


}
