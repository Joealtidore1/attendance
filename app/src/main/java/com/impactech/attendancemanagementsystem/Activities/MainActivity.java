package com.impactech.attendancemanagementsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.impactech.attendancemanagementsystem.Fragments.AddClass;
import com.impactech.attendancemanagementsystem.Fragments.AddCourse;
import com.impactech.attendancemanagementsystem.Fragments.AddDepartment;
import com.impactech.attendancemanagementsystem.Fragments.AssignCourse;
import com.impactech.attendancemanagementsystem.Fragments.AddStudent;
import com.impactech.attendancemanagementsystem.Fragments.Enroll;
import com.impactech.attendancemanagementsystem.Fragments.Home;
import com.impactech.attendancemanagementsystem.Fragments.Report;
import com.impactech.attendancemanagementsystem.R;

import static android.view.View.GONE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.ROLE;
import static com.impactech.attendancemanagementsystem.Utils.Helper.pref;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        role = pref(getApplicationContext()).getString(ROLE, "");
        bnv = findViewById(R.id.bottomNavView);
        bnv.setOnNavigationItemSelectedListener(this.navListener);
        bnv.setSelectedItemId(R.id.addClass);


        if(!role.equalsIgnoreCase("admin")){
            findViewById(R.id.addDept).setVisibility(GONE);


            bnv.getMenu().findItem(R.id.addStudent).setTitle("Enroll");
            bnv.getMenu().findItem(R.id.addLecturer).setTitle("Report");
            bnv.getMenu().findItem(R.id.addDept).setTitle("View Attendance");
            bnv.getMenu().findItem(R.id.addLecturer).setVisible(false);
        }else{
            bnv.getMenu().findItem(R.id.addClass).setTitle("Add Course");
        }
    }

    public void logout(View v){
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment frag=new Home();
            switch (item.getItemId()){
               /* case R.id.home:
                    frag = new Home();
                    break;*/
                case R.id.addClass:
                    if(role.equalsIgnoreCase("admin"))
                        frag = new AddCourse();
                    else
                        frag = new AddClass();
                    break;
                case R.id.addStudent:
                    if(role.equalsIgnoreCase("admin")){
                        frag = new AddStudent();
                    }else{
                        frag = new Enroll();
                    }

                    break;
                case R.id.addLecturer:
                    if(role.equalsIgnoreCase("admin"))
                        frag = new AssignCourse();
                    else  frag = new Report();
                    break;
                case R.id.addDept:
                    frag = new AddDepartment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, frag).commit();
            return true;
        }
    };
}