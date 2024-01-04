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

public class CampingAdapter extends ArrayAdapter<CampingReservation> {

    private Context ctx;
    private int res;
    public CampingAdapter(@NonNull Context context, int resource, @NonNull List<CampingReservation> objects) {
        super(context, resource, objects);
        ctx = context;
        res = resource;
        Camping.allReservations = (ArrayList<CampingReservation>) objects;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        CampingReservation c = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(res, parent,false);
        }
        if(c != null){
            TextView txtViewDate = convertView.findViewById(R.id.txtView1);
            TextView txtViewCost = convertView.findViewById(R.id.txtView2);
            TextView txtViewDays = convertView.findViewById(R.id.txtView3);

            txtViewDate.setText(c.date);
            String formattedCost = String.format("Cost: %.2f$", c.cost);
            txtViewCost.setText(formattedCost);
            txtViewDays.setText(c.days + " days");
        }

        return convertView;
    }
}
