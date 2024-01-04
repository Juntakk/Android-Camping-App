package com.example.campingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KayakingAdapter extends ArrayAdapter<KayakingReservation> {
    public Context ctx;
    public int res;
    public KayakingAdapter(@NonNull Context context, int resource, @NonNull List<KayakingReservation> objects) {
        super(context, resource, objects);
        ctx = context;
        res = resource;
        Kayaking.allReservations = (ArrayList<KayakingReservation>) objects;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        KayakingReservation k = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(res, parent,false);
        }
        if(k != null){
            TextView txtViewDate = convertView.findViewById(R.id.txtView1);
            TextView txtViewCost = convertView.findViewById(R.id.txtView2);
            TextView txtViewDays = convertView.findViewById(R.id.txtView3);

            txtViewDate.setText(k.date);
            String formattedCost = String.format("Cost: %.2f$", k.cost);
            txtViewCost.setText(formattedCost);
            txtViewDays.setText(k.color);
        }

        return convertView;
    }
}
