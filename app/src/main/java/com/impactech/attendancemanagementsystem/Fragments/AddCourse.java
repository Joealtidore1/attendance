package com.impactech.attendancemanagementsystem.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Course;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AddCourse extends Fragment implements View.OnClickListener {

    TextInputEditText mCode, mTitle, mCrHr;
    Spinner mSem, mDept;
    Button mUploadCourse;
    private DBHelper db;
    private List<Department> departments;
    private Course course;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        mCode = v.findViewById(R.id.code);
        mTitle = v.findViewById(R.id.courseTitle);
        mCrHr = v.findViewById(R.id.creditHours);
        mSem = v.findViewById(R.id.semester);
        mDept = v.findViewById(R.id.department);
        mUploadCourse = v.findViewById(R.id.uploadCourse);

        mUploadCourse.setOnClickListener(this);

        db = new DBHelper(getContext());
        List<String> deptList = new ArrayList<>();
        departments = new ArrayList<>();
        departments = db.getDepartments();
        for(Department d : departments){
            deptList.add(d.getName());
        }
        Collections.sort(deptList);
        deptList.add(0, "-Select Department-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, deptList);
        mDept.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        addCourse();
    }

    public void addCourse(){
        if(getData()) db.addCourse(course);
    }

    public boolean getData(){
        String code, title, crHr, semester, dept;
        code = mCode.getText().toString();
        if(code == null || code.isEmpty()){ showToast("Course code cannot be empty"); return false;};
        title = mTitle.getText().toString();
        if(title == null || title.isEmpty()){ showToast("Course title cannot be empty"); return false;};
        crHr = mCrHr.getText().toString();
        if(crHr == null || crHr.isEmpty()){ showToast("Credit unit cannot be empty"); return false;};

        dept = mDept.getSelectedItemPosition() != 0 ? departments.get(mDept.getSelectedItemPosition()-1).getCode() : null;
        if(dept == null){ showToast("Select A department"); return false;};

        semester = mSem.getSelectedItemPosition() != 0 ? mSem.getSelectedItem().toString() : null;
        if(dept == null){ showToast("Select A Semester"); return false;};

        course = new Course(code, title, semester, dept, crHr);

        return true;
    }

    private void showToast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();

    }
}