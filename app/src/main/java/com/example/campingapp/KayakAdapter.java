package com.example.campingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KayakAdapter extends ArrayAdapter<Kayak> {
    private Context ctx;
    private int res;
    public ArrayList<Kayak> allKayaks;
    public KayakAdapter(@NonNull Context context, int resource, @NonNull List<Kayak> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.res = resource;
        allKayaks = (ArrayList<Kayak>) objects;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Kayak k =getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.ctx);
            convertView = inflater.inflate(this.res, parent, false);
        }

        if(k != null){
            ImageView img = convertView.findViewById(R.id.imgKayak);
            TextView txtViewKayak = convertView.findViewById(R.id.txtViewKayakDescription);
            img.setImageResource(k.img);
            txtViewKayak.setText(k.description);

        }
        return convertView;
    }
}
