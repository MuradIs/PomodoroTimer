package com.muradismayilov.martiandeveloper.pomodorotimer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.muradismayilov.martiandeveloper.pomodorotimer.R;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    BarChart weekCB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        weekCB = view.findViewById(R.id.weekBC);
        ArrayList<BarEntry> NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(5, 0));
        NoOfEmp.add(new BarEntry(3, 1));
        NoOfEmp.add(new BarEntry(16, 2));
        NoOfEmp.add(new BarEntry(19, 3));
        NoOfEmp.add(new BarEntry(2, 4));
        NoOfEmp.add(new BarEntry(1, 5));
        NoOfEmp.add(new BarEntry(15, 6));

        ArrayList week = new ArrayList();

        week.add("S");
        week.add("Mon");
        week.add("Tue");
        week.add("Wed");
        week.add("Thu");
        week.add("Fri");
        week.add("Sat");

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
        weekCB.animateY(5000);
        BarData data = new BarData(week, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        weekCB.setData(data);

        return view;
    }
}
