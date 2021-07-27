package com.impactech.attendancemanagementsystem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.impactech.attendancemanagementsystem.Adapters.BluetoothViewAdapter;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Fragments.AddStudent;
import com.impactech.attendancemanagementsystem.Model.Attendance;
import com.impactech.attendancemanagementsystem.Model.Bluetooth;
import com.impactech.attendancemanagementsystem.Model.Enrollment;
import com.impactech.attendancemanagementsystem.Model.Student;
import com.impactech.attendancemanagementsystem.R;
import com.impactech.attendancemanagementsystem.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

import static com.impactech.attendancemanagementsystem.Database.Constants.DBConstant.STAFF_ID;

public class ClassAttendance extends AppCompatActivity implements View.OnClickListener {

    ListView mAttendees;
    TextView mEmptyScreen;
    Button mSubmit;
    DBHelper db;

    List<Enrollment> enrollments;
    List<Student> students;
    List<Attendance> attendance;
    String courseCode;
    String date;

    private final BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //showToast("Listening");
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                boolean b = true;
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("DEVICE", device.getAddress());
                Log.d("LENGTH", attendees.size() + "");
                for(String s : btList){
                    if(s.equals(device.getAddress())){
                        b = false;
                        break;
                    }
                }
                if (b) {
                    btList.add(device.getAddress());
                    for(Student s : students){
                        if(s.getBluetoothId().equals(device.getAddress())){

                            attendees.add(new Bluetooth(s.getSurname() + " " + s.getFirstName(), s.getRegNo()));
                            attendance.add(new Attendance(
                                                courseCode,
                                                Helper.pref(getApplicationContext()).getString(STAFF_ID, ""),
                                                s.getRegNo(),
                                                "2018/2019",
                                                date

                                    ));
                            mSubmit.setVisibility(View.VISIBLE);
                            mEmptyScreen.setVisibility(View.GONE);
                            break;
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals (intent.getAction())){
                bt.startDiscovery ();
            }
        }
    };
    private List<String> btList;
    private List<Bluetooth> attendees;
    BluetoothViewAdapter adapter;
    private BluetoothAdapter bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_attendance);

        init();
    }

    private void init() {
        mAttendees = findViewById(R.id.attendees);
        mSubmit = findViewById(R.id.submit);
        mEmptyScreen = findViewById(R.id.emptyScreen);
        db = new DBHelper(getApplicationContext());
        students = new ArrayList<>();
        btList = new ArrayList<>();
        attendees = new ArrayList<>();
        attendance = new ArrayList<>();

        date = Helper.getDate();
        
        //adapter = new BluetoothViewAdapter(getApplicationContext(), attendees);

        courseCode = getIntent().getStringExtra("courseCode");


        new Thread2().run((stud, enr) -> {
            students = stud;
            enrollments = enr;

            showToast(students.size()!= 0?students.get(0).getDepartment():"Na wao");
        });

        displayBtDevices();

        mSubmit.setOnClickListener(this);
    }

    public void displayBtDevices(){
        bt = BluetoothAdapter.getDefaultAdapter();
        attendees = new ArrayList<>();

        if(bt == null){
            showToast("Device no supported");
            finish();
            return;
        }
        checkBluetoothState();
        bt.startDiscovery();

        new Thread3().start();

        adapter = new BluetoothViewAdapter(getApplicationContext(), attendees);
        mAttendees.setAdapter(adapter);
    }

    private void checkBluetoothState() {
        if (!bt.isEnabled()) {
            showToast ("Enable bluetooth");
            Intent intent = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult (intent, 1000);

        }  //showToast ("Bluetooth is enabled");

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread3().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(deviceFoundReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if(requestCode == 1000){
            checkBluetoothState ();
        }
    }

    public void getInitialData(){
        enrollments = db.getEnrolledStudent(courseCode, "2018/2019");
        for(Enrollment e : enrollments){
            students.add(db.getStudentByCode(e.getRegNo()));
        }
        Log.d("VALUES", students.get(0).getDepartment());
    }
    private void showToast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(ClassAttendance.this)
                .setMessage("Do you want to submit attendance?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("Submitting");
                        submit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }

    public void submit(){
        if(attendance.size()>0){
            db.addAttendance(attendance.get(0));
            attendance.remove(0);
            submit();
        }
        finish();
    }

    public class Thread2 extends Thread{

        public void run(CallBack c) {
            super.run();
            getInitialData();
            c.onFinish(students, enrollments);
        }


    }

    private class Thread3 extends Thread{
        public void run(){
            registerReceiver (deviceFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            registerReceiver (deviceFoundReceiver, new IntentFilter (BluetoothAdapter.ACTION_DISCOVERY_STARTED));
            registerReceiver (deviceFoundReceiver, new IntentFilter (BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
            try {
                sleep (1500);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

        }
    }

    public interface CallBack{
        void onFinish(List<Student> stud, List<Enrollment> enr);
    }
}