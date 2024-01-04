package com.example.campingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class RidingAdapter extends ArrayAdapter<RidingReservation>{
    public Context ctx;
    public int res;

    public RidingAdapter(@NonNull Context context, int resource, @NonNull List<RidingReservation> objects) {
        super(context, resource, objects);
        ctx = context;
        res = resource;
        Riding.allReservations = (ArrayList<RidingReservation>) objects;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RidingReservation r = getItem(position);

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(res, parent, false);
        }

        if(r != null){
            TextView txtViewDate = convertView.findViewById(R.id.txtView1);
            TextView txtViewCourse = convertView.findViewById(R.id.txtView2);
            TextView txtViewCost = convertView.findViewById(R.id.txtView3);


            txtViewDate.setText(r.date);
            txtViewCourse.setText(r.course);
            String formattedCost = String.format("Cost: %.2f$", r.cost);
            txtViewCost.setText(formattedCost);
        }

        return convertView;
    }
    }
