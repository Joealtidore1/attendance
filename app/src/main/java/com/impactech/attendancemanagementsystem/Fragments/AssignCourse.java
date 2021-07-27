package com.impactech.attendancemanagementsystem.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.impactech.attendancemanagementsystem.Activities.AddLecturer;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Course;
import com.impactech.attendancemanagementsystem.Model.CourseAssignment;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.Model.Lecturer;
import com.impactech.attendancemanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AssignCourse extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    /*TextInputEditText mStaffId, mSurname, mFirstName, mOtherNames, mDiscipline, mPassword;
    Spinner mDesignation, mDept, mRole;
    Button mAddLecturer;
    private DBHelper db;
    private List<Department> departments;
    private Lecturer lecturer;*/

    Spinner mDept, mCourse, mStaff;
    Button mAssignCourse;
    FloatingActionButton mAddLecturer;

    private DBHelper db;
    private List<Department> departments;
    private List<Course> allCourses;
    private List<Lecturer> lecturerByDept;

    public AssignCourse() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_assign_course, container, false);

        init(view);

        return view;
    }

    public void init(View v){
        mDept = v.findViewById(R.id.department);
        mCourse = v.findViewById(R.id.courseSpinner);
        mStaff = v.findViewById(R.id.staffSpinner);
        mAssignCourse = v.findViewById(R.id.assignCourse);
        mAddLecturer = v.findViewById(R.id.assignLecturer);
        db = new DBHelper(getContext());
        populateSpinner();

        mDept.setOnItemSelectedListener(this);
        mCourse.setOnItemSelectedListener(this);
        mStaff.setOnItemSelectedListener(this);
        mAssignCourse.setOnClickListener(this);
        mAddLecturer.setOnClickListener(this);
    }

    public void populateSpinner(){
        List<String> deptList = new ArrayList<>();
        departments = new ArrayList<>();
        departments = db.getDepartments();
        for(Department d : departments){
            deptList.add(d.getName());
        }
        deptList.add(0, "-Select Department-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, deptList);
        mDept.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.department:
                if(position > 0){
                    showToast(departments.get(position-1).getCode());
                    allCourses = db.getAllCourses(departments.get(position-1).getCode());
                    populateCourseSpinner();
                }else{
                    mCourse.setVisibility(View.GONE);
                }
                break;
            case R.id.courseSpinner:
                if(position > 0){
                    lecturerByDept = db.getLecturerByDept(departments.get(position-1).getCode());
                    showToast(String.valueOf(lecturerByDept.size()));
                    populateStaffSpinner();
                }else{
                    mStaff.setVisibility(View.GONE);
                }
                break;
            case R.id.staffSpinner:
                if(position>0){
                    mAssignCourse.setVisibility(View.VISIBLE);
                }else{
                    mAssignCourse.setVisibility(View.GONE);
                }
        }
    }

    private void showToast(String code) {
        Toast.makeText(getContext(), code, Toast.LENGTH_SHORT).show();
    }

    public void populateCourseSpinner(){
        List<String> courseList = new ArrayList<>();
        for(Course d : allCourses){
            courseList.add(d.getTitle());
        }
        courseList.add(0, "-Select Course-");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
        mCourse.setAdapter(spinnerAdapter);
        mCourse.setVisibility(View.VISIBLE);
        mAssignCourse.setVisibility(View.GONE);
    }

    public void populateStaffSpinner(){

        List<String> staffList = new ArrayList<>();
        for(Lecturer d : lecturerByDept){
            staffList.add(d.getDesignation()+ " " + d.getFirstName().charAt(0) + ". " + d.getSurname() + "\n" +
                    d.getStaffId());
        }
        staffList.add(0, "-Select staff-");
        showToast(String.valueOf(staffList.size()));
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, staffList);
        mStaff.setAdapter(spinnerAdapter);
        mStaff.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.assignCourse:
                assignCourse();
                break;
            case R.id.assignLecturer:
                showToast("clicked");
                startActivity(new Intent(getContext(), AddLecturer.class));
        }

    }

    public void assignCourse(){
        String code, staffId, session;
        code = allCourses.get(mCourse.getSelectedItemPosition()-1).getCode();
        staffId = lecturerByDept.get(mStaff.getSelectedItemPosition()-1).getStaffId();
        session = "2018/2019";

        db.assignCourse(new CourseAssignment(code, staffId, session));
    }

    /*private void init(View v) {
        mStaffId = v.findViewById(R.id.staffId);
        mSurname = v.findViewById(R.id.surname);
        mFirstName = v.findViewById(R.id.firstname);
        mOtherNames = v.findViewById(R.id.otherNames);
        mDesignation = v.findViewById(R.id.designation);
        mDiscipline = v.findViewById(R.id.discipline);
        mDept = v.findViewById(R.id.department);
        mRole = v.findViewById(R.id.role);
        mPassword = v.findViewById(R.id.password);
        mAddLecturer = v.findViewById(R.id.addLecturer);

        mAddLecturer.setOnClickListener(this);

        populateSpinner();
    }

    public void populateSpinner(){
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
        addLecturer();
    }

    public void addLecturer(){
        if(getData()) db.addLecturer(lecturer);
    }

    public boolean getData(){
        String staffId, surname, firstName, otherNames, designation, discipline, role, password, dept;

        staffId = mStaffId.getText().toString();
        if(staffId == null || staffId.isEmpty()){ showToast("StaffId cannot be empty"); return false;};

        surname = mSurname.getText().toString();
        if(surname == null || surname.isEmpty()){ showToast("Surname cannot be empty"); return false;};

        firstName = mFirstName.getText().toString();
        if(firstName == null || firstName.isEmpty()){ showToast("First Name cannot be empty"); return false;};

        otherNames = mOtherNames.getText().toString();
        //if(otherNames == null || otherNames.isEmpty()){ showToast("First Name cannot be empty"); return false;};

        discipline = mDiscipline.getText().toString();
        if(discipline == null || discipline.isEmpty()){ showToast("First Name cannot be empty"); return false;};

        password = mPassword.getText().toString();
        if(password == null || password.isEmpty()){ showToast("First Name cannot be empty"); return false;};

        dept = mDept.getSelectedItemPosition() != 0 ? departments.get(mDept.getSelectedItemPosition()-1).getCode() : null;
        if(dept == null){ showToast("Select A department"); return false;};

        role = mRole.getSelectedItemPosition() != 0 ? mRole.getSelectedItem().toString() : null;
        if(role == null){ showToast("Select A department"); return false;};

        designation = mDesignation.getSelectedItemPosition() != 0 ? mDesignation.getSelectedItem().toString() : null;
        if(dept == null){ showToast("Select A Semester"); return false;};

        lecturer = new Lecturer(staffId, surname, firstName, otherNames, designation, discipline, "", dept, role, password );

        return true;
    }

    private void showToast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();

    }*/
}