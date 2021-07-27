package com.impactech.attendancemanagementsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.impactech.attendancemanagementsystem.Model.Bluetooth;
import com.impactech.attendancemanagementsystem.R;

import java.util.List;

public class BluetoothViewAdapter extends ArrayAdapter<Bluetooth> {

    public BluetoothViewAdapter(@NonNull Context context, List<Bluetooth> bluetooths) {
        super(context, 0, bluetooths);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.custom_bt, parent, false);
        }
        TextView address = view.findViewById(R.id.address);
        TextView name = view.findViewById(R.id.name);

        Bluetooth item = getItem(position);
        if(item != null){
            address.setText(item.getAddress());
            if(item.getName() != null)
                name.setText(item.getName());
        }
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
