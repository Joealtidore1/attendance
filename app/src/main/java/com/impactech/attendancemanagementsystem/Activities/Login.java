package com.impactech.attendancemanagementsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Lecturer;
import com.impactech.attendancemanagementsystem.R;
import com.impactech.attendancemanagementsystem.Utils.Helper;

import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.DEPARTMENT;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.ROLE;
import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STAFF_ID;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText mStaffId, mPassword;
    Button mLogin;

    String staffId, password;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mStaffId = findViewById(R.id.staffId);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);

        db = new DBHelper(getApplicationContext());

        /*if (!Helper.pref(getApplicationContext()).getString(STAFF_ID,"").equalsIgnoreCase("")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }*/

        mLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(getData()){
            doLogin();
        }
    }

    public void doLogin (){
        SharedPreferences pref = Helper.pref(getApplicationContext());
        final Lecturer login = db.login(staffId, password);
        if(login != null){
            pref.edit().putString(ROLE, login.getRole()).apply();
            pref.edit().putString(DEPARTMENT, login.getDepartment()).apply();
            pref.edit().putString(STAFF_ID, login.getStaffId()).apply();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }else
            showToast("incorrect staff id or password");
    }

    public boolean getData(){
        boolean b = false;
        if(mStaffId.getEditableText().length() > 1 && mPassword.getEditableText().length() > 1){
            b = true;
            staffId = mStaffId.getText().toString();
            password = mPassword.getText().toString();
            mStaffId.setText("");
            mPassword.setText("");
        }else{
            showToast("Staff Id and password length should be greater than 3");
        }
        return b;
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}