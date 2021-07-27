package com.impactech.attendancemanagementsystem.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.impactech.attendancemanagementsystem.Activities.ClassAttendance;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Course;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.Model.Lecturer;
import com.impactech.attendancemanagementsystem.R;
import com.impactech.attendancemanagementsystem.Utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DEPARTMENT;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STAFF_ID;
import static com.impactech.attendancemanagementsystem.Utils.Helper.pref;
import static java.lang.String.format;

public class AddClass extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner courseSpinner;
    private LinearLayout courseDetails;
    private Button startClass;

    private TextView mCourseCode;
    private TextView courseTitle;
    private TextView creditHours;
    private TextView enrollments;
    private TextView noOfClasses;
    private TextView averageAttendance;
    private TextView noOfAssignedLecturers;
    private TextView coordinator;
    private TextView otherLecturers;

    ScrollView mScrollView;


    DBHelper db;

    String courseCode;
    private List<Course> courses;

    public AddClass() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        init(view);

        return view;
    }

    private void init(View v){
        courseSpinner = v.findViewById(R.id.courseSpinner);
        courseDetails = v.findViewById(R.id.courseDetailsLayout);
        startClass = v.findViewById(R.id.startClass);

        mCourseCode = v.findViewById(R.id.courseCode);
        courseTitle = v.findViewById(R.id.courseTitle);
        creditHours = v.findViewById(R.id.courseUnits);

        enrollments = v.findViewById(R.id.enrollments);
        noOfClasses = v.findViewById(R.id.noOfClasses);
        averageAttendance = v.findViewById(R.id.averageAttendance);
        noOfAssignedLecturers = v.findViewById(R.id.noOfLecturers);

        coordinator = v.findViewById(R.id.coordinator);
        otherLecturers = v.findViewById(R.id.others);

        mScrollView = v.findViewById(R.id.scrollView);

        startClass.setOnClickListener(this);
        courseSpinner.setOnItemSelectedListener(this);

        List<String> courseList = new ArrayList<>();
        courses = new ArrayList<>();

        db = new DBHelper(getContext());

        populateSpinner();

        /*for(Department d : courses){
            courseList.add(d.getName());
        }*/



        //int numOfEnrolls = db.getNumberOfEnrollments(courses.get())
    }

    public void populateSpinner(){
        List<String> courseList = new ArrayList<>();
        courses = new ArrayList<>();
        String stfId = pref(getContext()).getString(STAFF_ID, "");

        courses = db.getAssignedCourses(stfId, "2018/2019");
        for(Course d : courses){
            courseList.add(d.getTitle());
        }
        courseList.add(0, "-Select Course-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, courseList);
        courseSpinner.setAdapter(spinnerAdapter);
    }
    @Override
    public void onClick(View v) {

        startActivity(
                new Intent(getContext(),
                        ClassAttendance.class).putExtra("courseCode", courseCode)
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0){
            courseCode = courses.get(position-1).getCode();
            if (db.getNumberOfEnrollments(courseCode, "2018/2019")>0) {
                mScrollView.setVisibility(View.VISIBLE);
                getData(position);
            }else{
                showToast("No Student has enrolled yet!!!");
            }

        }else{
            mScrollView.setVisibility(GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("DefaultLocale")
    public void getData(int pos){
        int n = db.getNumberOfClasses(courses.get(pos - 1).getCode(), "2018/2019");
        int s = n;
        noOfClasses.setText(format("%s %d", noOfClasses.getText(), n));

        n = db.getNumberOfEnrollments(courses.get(pos - 1).getCode(), "2018/2019");
        enrollments.setText(format("%s %d", enrollments.getText(), n));

        n = db.getTotalAttendance(courses.get(pos - 1).getCode(), "2018/2019");
        if(s == 0)
            s = 0;
        double av = (double)  n/s;
        showToast(String.valueOf(n));
        averageAttendance.setText(format("%s %.2f", averageAttendance.getText(), av));

        n = db.getNoOfAssignedLecturers(courses.get(pos - 1).getCode(), "2018/2019");
        noOfAssignedLecturers.setText(format("%s %d", noOfAssignedLecturers.getText(), n));

        mCourseCode.setText(String.format("%s %s", mCourseCode.getText(), courses.get(pos-1).getCode()));
        courseTitle.setText(String.format("%s %s", courseTitle.getText(), courses.get(pos-1).getTitle()));
        creditHours.setText(String.format("%s %s", creditHours.getText(), courses.get(pos-1).getCreditUnits()));

        //Lecturer lect = db.getCoordinator(courses.get(pos - 1).getCode(), "2018/2019");
        //coordinator.setText(String.format("%s %s %c. %s", coordinator.getText(), lect.getDesignation(), lect.getFirstName().charAt(0), lect.getSurname()));

        final List<Lecturer> otherLecturer = db.getOtherLecturers(courses.get(pos - 1).getCode(), "2018/2019");
        for(Lecturer l : otherLecturer){
            otherLecturers.setText(String.format("%s\n %s %c. %s", otherLecturers.getText(), l.getDesignation(), l.getFirstName().charAt(0), l.getSurname()));
        }

    }

    private void showToast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();

    }


}