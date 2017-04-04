package com.example.strost.logopedist.controller.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.controller.activities.MainPageActivity;
import com.example.strost.logopedist.controller.activities.PatientActivity;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.entities.Patient;
import java.util.ArrayList;

/**
 * Created by Bram on 30/01/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    private ArrayList<Patient> mPatientList;
    private Caregiver caregiver;

    public DataAdapter(ArrayList<Patient> androidList, Caregiver z1){
        mPatientList = androidList;
        caregiver = z1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitle, mDescription;

        ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById( R.id.tv_title);
            mDescription = (TextView) itemView.findViewById(R.id.tv_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
            mPatientList.get(getPosition()).getName();
            goToPatientPage(v);
        }

        public void goToPatientPage(View v){
            Intent detailIntent = new Intent(v.getContext(), PatientActivity.class);
            detailIntent.putExtra("caregiverId", caregiver.getId());
            detailIntent.putExtra("patientid", mPatientList.get(getPosition()).getId());
            v.getContext().startActivity(detailIntent);


        }
    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText(mPatientList.get(position).getName());
        holder.mDescription.setText(mPatientList.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return mPatientList != null ? mPatientList.size() : 0;
    }


}
