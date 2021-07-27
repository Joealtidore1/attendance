package com.impactech.attendancemanagementsystem.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.impactech.attendancemanagementsystem.Activities.ClassAttendance;
import com.impactech.attendancemanagementsystem.Adapters.AttendeesAdapter;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Attendance;
import com.impactech.attendancemanagementsystem.Model.AttendanceDetails;
import com.impactech.attendancemanagementsystem.Model.Course;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.Model.Enrollment;
import com.impactech.attendancemanagementsystem.Model.Student;
import com.impactech.attendancemanagementsystem.R;
import com.impactech.attendancemanagementsystem.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.ROLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STAFF_ID;
import static com.impactech.attendancemanagementsystem.Utils.Helper.pref;

public class AddDepartment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextInputEditText deptId, deptName;
    Button addDept;
    ListView mAttendees;
    Spinner mCourseSpinner;

    DBHelper db;
    private List<Course> courses;
    List<Attendance> attendance;
    List<AttendanceDetails> attendanceDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_department, container, false);
        init(view);
        return view;
    }

    private void init(View v){
        deptId = v.findViewById(R.id.deptId);
        deptName = v.findViewById(R.id.deptName);
        addDept = v.findViewById(R.id.addDept);
        mAttendees = v.findViewById(R.id.attendees);
        mCourseSpinner = v.findViewById(R.id.courseSpinner);
        LinearLayout lecturerView = v.findViewById(R.id.lecturerView);
        LinearLayout adminView = v.findViewById(R.id.adminView);

        db = new DBHelper(getContext());
        if(Helper.pref(getContext()).getString(ROLE, "").equalsIgnoreCase("admin")){
            adminView.setVisibility(View.VISIBLE);
            lecturerView.setVisibility(View.GONE);
        }else{
            adminView.setVisibility(View.GONE);
            lecturerView.setVisibility(View.VISIBLE);
            populateSpinner();
        }





        addDept.setOnClickListener(this);
    }

    public void populateSpinner(){
        mCourseSpinner.setOnItemSelectedListener(this);
        List<String> courseList = new ArrayList<>();
        attendance = new ArrayList<>();
        attendanceDetails = new ArrayList<>();
        courses = new ArrayList<>();
        String stfId = pref(getContext()).getString(STAFF_ID, "");

        courses = db.getAssignedCourses(stfId, "2018/2019");
        for(Course d : courses){
            courseList.add(d.getTitle());
        }
        courseList.add(0, "-Select Course-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
        mCourseSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        addDept();
    }

    private void addDept() {
        if(deptId.getEditableText().length() < 2 || deptName.getEditableText().length() < 3){
            showToast("No fields must be empty");
            return;
        }
        Department dept = new Department(deptId.getText().toString(), deptName.getText().toString());
        db.addDepartment(dept);
        deptId.setText(null);
        deptName.setText(null);
    }


    @SuppressLint("DefaultLocale")
    public void getInitialData(String courseCode){
        attendance = db.getAttendance(courseCode, "2018/2019");
        attendanceDetails = new ArrayList<>();
        for(Attendance e : attendance){
            final Student studentByCode = db.getStudentByCode(e.getRegNo());
            String name = studentByCode.getFirstName() + " " + studentByCode.getSurname();

            int Tn = db.getTotalAttendance(courseCode, "2018/2019");
            int Sn = db.getNoOfAttendaceById(courseCode, e.getRegNo(), "2018/2019");

            double per = Tn != 0?(Sn * 100.0) / Tn : 0;

            attendanceDetails.add(new AttendanceDetails(studentByCode.getRegNo(), name, String.format("%.2f %c", per, '%'), Sn));
        }
    }
    private void showToast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0){
            new Thread2(courses.get(position-1).getCode()).run((c)->{
                attendanceDetails = c;
                final AttendeesAdapter adapter =  new AttendeesAdapter(getContext(), attendanceDetails);
                mAttendees.setAdapter(adapter);

            });
        }else{
            mAttendees.setAdapter(null);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class Thread2 extends Thread{
        String course;
        public Thread2(String course){
            this.course = course;
        }
        public void run(CallBack c) {
            super.run();
            getInitialData(course);
            c.onFinish(attendanceDetails);
        }


    }

    public interface CallBack{
        void onFinish(List<AttendanceDetails> attDet);
    }

}