package com.impactech.attendancemanagementsystem.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.impactech.attendancemanagementsystem.Adapters.BluetoothViewAdapter;
import com.impactech.attendancemanagementsystem.Database.DBHelper;
import com.impactech.attendancemanagementsystem.Model.Bluetooth;
import com.impactech.attendancemanagementsystem.Model.Department;
import com.impactech.attendancemanagementsystem.Model.Student;
import com.impactech.attendancemanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;
import static com.impactech.attendancemanagementsystem.R.id.*;


public class AddStudent extends Fragment implements OnClickListener,  AdapterView.OnItemClickListener {

    TextInputEditText mSurname, mFirstName, mOtherNames, mRegNumber, mBtId;
    Spinner mDept;
    Button mScanBt, mRegister;
    ListView mScannedBt;
    ScrollView mScrollView;
    BluetoothAdapter bt;
    ProgressBar progressBar;
    List<String> btList, deptList;
    List<Bluetooth> bluetooths;
    List<Department> departments;
    Context context;
    private BluetoothViewAdapter adapter;
    DBHelper db;

    private final BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                boolean b = true;
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                for(String s : btList){
                    if(s.equals(device.getAddress())){
                        b = false;
                        break;
                    }
                }
                if (b) {
                    btList.add(device.getAddress());
                    bluetooths.add(new Bluetooth(device.getName(), device.getAddress()));
                    adapter.notifyDataSetChanged();
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals (intent.getAction())){
                bt.startDiscovery ();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_student, container, false);

        init(view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(deviceFoundReceiver);
    }

    public void init(View v){
        mBtId = v.findViewById(bluetoothId);
        mRegNumber = v.findViewById(regNumber);
        mOtherNames = v.findViewById(otherNames);
        mFirstName = v.findViewById(firstname);
        mSurname = v.findViewById(surname);
        mScanBt = v.findViewById(scanBt);
        mRegister = v.findViewById(register);
        mScannedBt = v.findViewById(bluetoothScan);
        mScrollView = v.findViewById(scrollView);
        progressBar = v.findViewById(R.id.progressBar);
        mDept = v.findViewById(courseSpinner);
        db = new DBHelper(context);

        btList = new ArrayList<>();
        deptList = new ArrayList<>();
        departments = db.getDepartments();
        for(Department d : departments){
            deptList.add(d.getName());
        }
        deptList.add(0, "-Select Department-");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, deptList);
        mDept.setAdapter(spinnerAdapter);



        mScanBt.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mScannedBt.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case scanBt:
                scanBluetoothDevice();
                break;
            case register:
                register();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //bt.cancelDiscovery();
        if(!db.isBluetooth(btList.get(position))) {
            mBtId.setText(btList.get(position));
            toggleVisibility(true);
        }else{
            showToast("Bluetooth address has been allocated already");
        }
    }

    private void register() {
        String surname, firstName, otherNames, regNo, btId, dept;
        surname = mSurname.getText().toString();
        if(surname == null || surname.isEmpty()){ showToast("Surname cannot be empty"); return;};
        firstName = mFirstName.getText().toString();
        if(firstName == null || firstName.isEmpty()){ showToast("First Name cannot be empty"); return;};
        otherNames = mOtherNames.getText().toString();
        regNo = mRegNumber.getText().toString();
        if(regNo == null || regNo.isEmpty()){ showToast("Registration Number cannot be empty"); return;};
        btId = mBtId.getText().toString();
        if(btId == null || btId.isEmpty()){ showToast("Bluetooth Address cannot be empty"); return;};
        dept = mDept.getSelectedItemPosition() != 0 ? departments.get(mDept.getSelectedItemPosition()-1).getCode() : null;
        if(dept == null){ showToast("Select A department"); return;};

        Student student = new Student(
                regNo, surname, firstName, otherNames, dept, btId
        );

        db.addStudent(student);
    }

    private void scanBluetoothDevice() {
        toggleVisibility(false);

        displayBtDevices();
    }

    public void toggleVisibility(boolean b){
        if(b){
            mScrollView.setVisibility(VISIBLE);
            mScannedBt.setVisibility(GONE);
            mScanBt.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        }else{
            mScrollView.setVisibility(GONE);
            mScannedBt.setVisibility(VISIBLE);
            mScanBt.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        }
        mScanBt.setText("Rescan Bluetooth");
    }

    public void displayBtDevices(){
        bt = BluetoothAdapter.getDefaultAdapter();
        bluetooths = new ArrayList<>();

        if(bt == null){
            getActivity().finish();
            return;
        }
        checkBluetoothState();
        bt.startDiscovery();

        new Thread2().start();

        adapter = new BluetoothViewAdapter(context, bluetooths);
        mScannedBt.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread2().start();
    }

    private void showToast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();

    }


    private void checkBluetoothState() {
        if (!bt.isEnabled()) {
            showToast ("Enable bluetooth");
            Intent intent = new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult (intent, 1000);

        }  //showToast ("Bluetooth is enabled");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if(requestCode == 1000){
            checkBluetoothState ();
        }
    }



    private class Thread2 extends Thread{
        public void run(){
            context.registerReceiver (deviceFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            context.registerReceiver (deviceFoundReceiver, new IntentFilter (BluetoothAdapter.ACTION_DISCOVERY_STARTED));
            context.registerReceiver (deviceFoundReceiver, new IntentFilter (BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
            try {
                sleep (1500);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

        }
    }

}