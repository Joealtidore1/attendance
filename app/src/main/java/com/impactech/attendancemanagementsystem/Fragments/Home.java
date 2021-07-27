package com.impactech.attendancemanagementsystem.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.impactech.attendancemanagementsystem.R;

public class Home extends Fragment implements View.OnClickListener {

    RelativeLayout mAssignCourses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ///init (view);
        return view;
    }

    private void init(View v) {
        mAssignCourses = v.findViewById(R.id.assignCourse);

        mAssignCourses.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}