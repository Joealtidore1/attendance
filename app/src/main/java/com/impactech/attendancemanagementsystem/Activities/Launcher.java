package com.impactech.attendancemanagementsystem.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.impactech.attendancemanagementsystem.R;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

       checkCoarseLocationPermission();
    }

    private void checkCoarseLocationPermission() {
        if(ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    11);
        }else{
            checkFineLocationPermission();
        }
    }

    private void checkFineLocationPermission() {
        if(ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    12);
        }else{
            new Handler().postDelayed(() -> {
                startActivity(new Intent(Launcher.this, Login.class));
                finish();
            }, 1500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkFineLocationPermission();
            } else {
                showToast("access denied! Exiting application...");
            }
        }
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(Launcher.this, Login.class));
                    finish();
                }, 3000);
            } else {
                showToast("access denied! Exiting application...");
            }
        }
    }

    private void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}