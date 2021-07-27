package com.impactech.attendancemanagementsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.Model.Lecturer;
import com.impactech.attendancemanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AddLecturer extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    
    /*Spinner mDept, mCourse, mStaff;
    Button mAssignCourse;

    private DBHelper db;
    private List<Department> departments;
    private List<Course> allCourses;
    private List<Lecturer> lecturerByDept; */

    TextInputEditText mStaffId, mSurname, mFirstName, mOtherNames, mDiscipline, mPassword;
    Spinner mDesignation, mDept, mRole;
    Button mAddLecturer;
    private DBHelper db;
    private List<Department> departments;
    private Lecturer lecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);
        
        init();
    }

    private void init() {
        mStaffId = findViewById(R.id.staffId);
        mSurname = findViewById(R.id.surname);
        mFirstName = findViewById(R.id.firstname);
        mOtherNames = findViewById(R.id.otherNames);
        mDesignation = findViewById(R.id.designation);
        mDiscipline = findViewById(R.id.discipline);
        mDept = findViewById(R.id.department);
        mRole = findViewById(R.id.role);
        mPassword = findViewById(R.id.password);
        mAddLecturer = findViewById(R.id.addLecturer);

        mAddLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLecturers();
            }
        });

        populateSpinner();
    }

    public void populateSpinner(){
        db = new DBHelper(getApplicationContext());
        List<String> deptList = new ArrayList<>();
        departments = new ArrayList<>();
        departments = db.getDepartments();
        for(Department d : departments){
            deptList.add(d.getName());
        }
        deptList.add(0, "-Select Department-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, deptList);
        mDept.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        //addLecturer();
    }

    public void addLecturers(){
        if(getData()) db.addLecturer(lecturer);
        showToast("Clicked");
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
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    
    /*public void init(){
        mDept = findViewById(R.id.department);
        mCourse = findViewById(R.id.courseSpinner);
        mStaff = findViewById(R.id.staffSpinner);
        mAssignCourse = findViewById(R.id.assignCourse);
        
        populateSpinner();
        
        mDept.setOnItemSelectedListener(this);
        mAssignCourse.setOnClickListener(this);
    }

    public void populateSpinner(){
        db = new DBHelper(getApplicationContext());
        List<String> deptList = new ArrayList<>();
        departments = new ArrayList<>();
        departments = db.getDepartments();
        for(Department d : departments){
            deptList.add(d.getName());
        }
        deptList.add(0, "-Select Department-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, deptList);
        mDept.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.department:
                if(position > 0){ 
                    allCourses = db.getAllCourses(departments.get(position).getCode());
                    populateCourseSpinner();
                }else{
                    mCourse.setVisibility(View.GONE);
                }
                break;
            case R.id.courseSpinner:
                if(position > 0){
                    lecturerByDept = db.getLecturerByDept(departments.get(position).getCode());
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
    
    public void populateCourseSpinner(){
        List<String> courseList = new ArrayList<>();
        for(Course d : allCourses){
            courseList.add(d.getTitle());
        }
        courseList.add(0, "-Select Course-");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
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
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, staffList);
        mStaff.setAdapter(spinnerAdapter);
        mStaff.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

    @Override
    public void onClick(View v) {
        assignCourse();
    }
    
    public void assignCourse(){
        String code, staffId, session;
        code = allCourses.get(mCourse.getSelectedItemPosition()-1).getCode();
        staffId = lecturerByDept.get(mStaff.getSelectedItemPosition()-1).getStaffId();
        session = "2018/2019";
        
        db.assignCourse(new CourseAssignment(code, staffId, session));
    }*/
}