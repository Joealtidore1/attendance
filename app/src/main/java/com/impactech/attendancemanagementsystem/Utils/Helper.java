package com.impactech.attendancemanagementsystem.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences pref(Context context){

        if(sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("preferences", 0);

        return sharedPreferences;
    }

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH: mm: ss");
        String strDate= formatter.format(date);

        return  strDate;
    }
}
