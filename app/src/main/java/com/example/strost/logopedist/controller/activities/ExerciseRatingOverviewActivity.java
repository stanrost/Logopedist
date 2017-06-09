package com.example.strost.logopedist.controller.activities;

import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Exercise;
import com.example.strost.logopedist.model.entities.Feedback;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.setDefault;

public class ExerciseRatingOverviewActivity extends ActionBarActivity {

    Exercise mExercise;
    private final String EXERCISE_KEY = "Exercise";
    private ArrayList<String> mRatingList = new ArrayList<>();
    private ArrayList<String> mDateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exerciseratingoverview);
        mExercise = (Exercise) getIntent().getSerializableExtra(EXERCISE_KEY);

        TextView nRatingOverview = (TextView) findViewById(R.id.tvRatingOverview);
        nRatingOverview.setText(nRatingOverview.getText() + " " + getString(R.string.from_enddate) + " " + mExercise.getEndDate().replace("/", "-"));
        Collections.sort(mExercise.getFeedback(), new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                return feedback1.getFeedbackDate().compareToIgnoreCase(feedback2.getFeedbackDate());
            }
        });
        for (Feedback f : mExercise.getFeedback()) {
            DateFormat format = new SimpleDateFormat("dd/MM/yy");
            Date date = null;
            try {
                date = format.parse(f.getFeedbackDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String day = new SimpleDateFormat("EEE", new Locale("nl")).format(date);
            mDateList.add(day);
        }

        for (Feedback f : mExercise.getFeedback()) {
            mRatingList.add(f.getPatientRating() + "");
        }
        
        createChart();
    }

    public void createChart(){
        BarChart chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisMaxValue(6);
        chart.getAxisLeft().setAxisMinValue(0);
        chart.getAxisLeft().setDrawTopYLabelEntry(false);
        chart.getAxisLeft().setTextSize(14);
        chart.getXAxis().setTextSize(14);
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
        for (int i = 0; i < mDateList.size(); i++) {
            int f = Integer.parseInt(mRatingList.get(i));
            BarEntry v1e1 = new BarEntry(f, i);
            valueSet1.add(v1e1);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.rating));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barDataSet1.setColor(getColor(R.color.colorPrimaryDark));
        }

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = mDateList;
        return xAxis;
    }

}
