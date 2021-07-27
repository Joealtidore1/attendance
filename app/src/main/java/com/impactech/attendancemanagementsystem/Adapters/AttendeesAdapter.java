package com.impactech.attendancemanagementsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.impactech.attendancemanagementsystem.Model.AttendanceDetails;
import com.impactech.attendancemanagementsystem.R;

import java.util.List;

public class AttendeesAdapter extends ArrayAdapter<AttendanceDetails> {
    public AttendeesAdapter(@NonNull Context context, List<AttendanceDetails> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {
        if(v == null)
            v = LayoutInflater.from(getContext()).inflate(R.layout.attendees_custom, parent, false);
        TextView name = v.findViewById(R.id.name);
        TextView regNum = v.findViewById(R.id.regNum);
        TextView noOfAtt = v.findViewById(R.id.noOfAtt);
        TextView perAtt = v.findViewById(R.id.perAtt);

        AttendanceDetails item = getItem(position);
        if (item != null) {
            name.setText(item.getName());
            regNum.setText(item.getRegNo());
            noOfAtt.setText(String.valueOf(item.getNumOfAtt()));
            perAtt.setText(item.getAttPer());
        }

        return v;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
