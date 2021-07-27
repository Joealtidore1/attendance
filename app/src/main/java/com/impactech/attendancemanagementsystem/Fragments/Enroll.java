package com.impactech.attendancemanagementsystem.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Course;
import com.impactech.attendancemanagementsystem.Model.Enrollment;
import com.impactech.attendancemanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STAFF_ID;
import static com.impactech.attendancemanagementsystem.Utils.Helper.pref;

public class Enroll extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    TextInputEditText mRegNo;
    Spinner mCourseSpinner;
    Button mEnroll;
    private List<Course> courses;
    private DBHelper db;
    private Enrollment enroll;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_enroll, container, false);
        init(view);
        return view;
    }

    public void init(View v){
        mRegNo = v.findViewById(R.id.regNumber);
        mCourseSpinner = v.findViewById(R.id.courseSpinner);
        mEnroll = v.findViewById(R.id.enrol);

        mEnroll.setOnClickListener(this);
        mCourseSpinner.setOnItemSelectedListener(this);
        db = new DBHelper(getContext());

        populateSpinner();
    }

    public void populateSpinner(){
        List<String> courseList = new ArrayList<>();
        courses = new ArrayList<>();
        String stfId = pref(getContext()).getString(STAFF_ID, "");

        courses = db.getAssignedCourses(stfId, "2018/2019");
        for(Course d : courses){
            courseList.add(d.getTitle());
        }
        //Collections.sort(courseList);
        courseList.add(0, "-Select Course-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
        mCourseSpinner.setAdapter(spinnerAdapter);
    }

    public void enroll(){
        if(getData()){
            db.enrollStudent(enroll);
        }
    }

    public boolean getData(){
        if(mRegNo.getEditableText().length()<1){
            showToast("Enter Student reg number first");
            return false;
        }
        enroll = new Enrollment(courses.get(mCourseSpinner.getSelectedItemPosition()-1).getCode(), mRegNo.getText().toString(), "2018/2019");
        return true;
    }

    private void showToast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        enroll();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position>0){
            mRegNo.setVisibility(VISIBLE);
            mEnroll.setVisibility(VISIBLE);
        }else{
            mRegNo.setVisibility(GONE);
            mEnroll.setVisibility(GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}