package com.example.strost.logopedist.controller.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Patient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NumberOfTimesOverviewActivity extends AppCompatActivity {

    private Patient mPatient;
    private ArrayList<Integer> mNumberOfTimesList = new ArrayList<>();
    private ArrayList<String> mNumerList = new ArrayList<>();
    private List<Exercise> mExercises = new ArrayList<Exercise>();
    private final String PATIENT_KEY = "Patient";
    private RadioGroup mNumberOfExcercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numberoftimesoverview);

        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        mExercises = mPatient.getExercises();
        RadioButton mFiveExercises = (RadioButton) findViewById(R.id.rbFiveExercises);
        RadioButton mTenExercises = (RadioButton) findViewById(R.id.rbTenExercises);
        RadioButton mFifteenExercises = (RadioButton) findViewById(R.id.rbFifteenExercises);
        mNumberOfExcercises = (RadioGroup) findViewById(R.id.rgNumberOfExercises);


        Collections.sort(mExercises, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise exercise1, Exercise exercise2) {
                int exerciseId1 = exercise1.getId();
                int exercieseId2 = exercise2.getId();
                return exercieseId2 > exerciseId1 ? +1 : exercieseId2 < exerciseId1 ? -1 : 0;
            }
        });
        setChart(getNumberOfExercises());

        if (mExercises.size() < 5) {
            mFiveExercises.setVisibility(View.GONE);
            mTenExercises.setVisibility(View.GONE);
            mFifteenExercises.setVisibility(View.GONE);
        } else if (mExercises.size() < 10) {
            mTenExercises.setVisibility(View.GONE);
            mFifteenExercises.setVisibility(View.GONE);
        } else if (mExercises.size() < 15) {
            mFifteenExercises.setVisibility(View.GONE);
        }

        mNumberOfExcercises.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                setChart(getNumberOfExercises());
            }
        });

    }

    private void setChart(int numberOfExercises) {

        if (mPatient.getExercises().size() < numberOfExercises) {
            numberOfExercises = mPatient.getExercises().size();
        }

        BarChart chart = (BarChart) findViewById(R.id.chart);
        mNumerList.clear();
        mNumberOfTimesList.clear();
        for (int i = 0; i < numberOfExercises; i++) {
            mNumerList.add(i + 1 + "");
        }

        for (int i = 0; i < numberOfExercises; i++) {
            mNumberOfTimesList.add(mExercises.get(i).getFeedback().size());
        }

        BarData data = new BarData(getXAxisValues(), getDataSet());
        switch (numberOfExercises) {
            case 5:
                chart.getXAxis().setTextSize(14);
                break;
            case 10:
                chart.getXAxis().setTextSize(10);
                break;
            case 15:
                chart.getXAxis().setTextSize(8);
                break;
        }
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisMaxValue(6);
        chart.getAxisLeft().setAxisMinValue(0);
        chart.getAxisLeft().setDrawTopYLabelEntry(false);
        chart.getAxisLeft().setTextSize(12);
        chart.getLegend().setTextSize(14);


        ValueFormatter d = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat mFormat = new DecimalFormat("###");
                return mFormat.format(value);
            }
        };
        chart.getAxisLeft().setValueFormatter(d);
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        for (int i = 0; i < mNumerList.size(); i++) {
            int f = mNumberOfTimesList.get(i);
            BarEntry v1e1 = new BarEntry(f, i);
            valueSet1.add(v1e1);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.number_of_times));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barDataSet1.setColor(getColor(R.color.colorPrimaryDark));
        }

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = mNumerList;
        return xAxis;
    }


    public int getNumberOfExercises() {
        int rgid = mNumberOfExcercises.getCheckedRadioButtonId();

        int numberOf = 0;
        if (rgid == R.id.rbFiveExercises) {
            numberOf = 5;
        }

        if (rgid == R.id.rbTenExercises) {
            numberOf = 10;
        }

        if (rgid == R.id.rbFifteenExercises) {
            numberOf = 15;
        }
        return numberOf;
    }
}
