package com.example.campingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Kayaking extends AppCompatActivity {

    public Button btnChoose, btnDate, btnTotal, btnReserve ,btnShowAll;
    public KayakAdapter adapter;
    public ArrayList<Kayak> allKayaks;
    public String selectedDate, color;
    public Double cost;
    public TextView txtViewChoose, txtViewDate, txtViewTotal;
    public DGW gw;
    public static ArrayList<KayakingReservation> allReservations = new ArrayList<>();
    public Boolean isCostCalculated = false;
    public Boolean isTotalDisplayed = false;
    public KayakingAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayaking);

        Kayak k1 = new Kayak(R.drawable.kayak1,"Orange");
        Kayak k2 = new Kayak(R.drawable.kayak2,"Blue");
        Kayak k3 = new Kayak(R.drawable.kayak3,"Green");
        Kayak k4 = new Kayak(R.drawable.kayak4,"Funky");

        allKayaks = new ArrayList<>();
        allKayaks.add(k1); allKayaks.add(k2);allKayaks.add(k3);allKayaks.add(k4);

        btnChoose = findViewById(R.id.btnChoose);
        btnDate = findViewById(R.id.btnDate);
        btnTotal = findViewById(R.id.btnTotal);
        btnReserve = findViewById(R.id.btnReserve);
        txtViewChoose = findViewById(R.id.txtViewChoose);
        txtViewDate = findViewById(R.id.txtViewDate);
        txtViewTotal = findViewById(R.id.txtViewTotal);
        btnShowAll = findViewById(R.id.btnShowAllReservations);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Kayaking.this);
                builder.setIcon(R.drawable.baseline_kayaking_24);
                builder.setTitle("            Choose a Kayak");
                adapter = new KayakAdapter(Kayaking.this,R.layout.kayaks,allKayaks);
                ListView lstView = new ListView(Kayaking.this);
                lstView.setAdapter(adapter);
                builder.setView(lstView);

                lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       Kayak k = allKayaks.get(i);
                       txtViewChoose.setText(k.description);
                       color = k.description;
                       Toast.makeText(Kayaking.this,"You chose the " + k.description + " kayak.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(Kayaking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        selectedDate = year + "/" + month  + "/" + day;
                        txtViewDate.setText(selectedDate);

                        c.set(year, month - 1, day);
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                            cost = 29.55 * 1.14;
                        }  else {
                            cost = 22.35 * 1.14;
                        }
                    }
                },year,month,day);
                dp.create();
                dp.show();
            }
        });
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedDate == null || color == null || cost == 0){
                    Toast.makeText(Kayaking.this,"Information missing",Toast.LENGTH_SHORT).show();
                    isCostCalculated = false;
                }
                else{
                    if(!isCostCalculated){
                        calculateCost();
                        isTotalDisplayed = true;
                        isCostCalculated = true;
                    }
                }
            }
        });
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedDate == null || cost == 0 || color == null || !isTotalDisplayed){
                    Toast.makeText(Kayaking.this,"Missing information",Toast.LENGTH_SHORT).show();
                }
                else{
                    gw = new DGW(Kayaking.this,"Reservations",null,1);
                    gw.openDB();
                    gw.addKayakingReservation(selectedDate, cost,color);
                    gw.closeDB();
                    Toast.makeText(Kayaking.this,"Reservation successful", Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });
        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Kayaking.this);
                builder.setIcon(R.drawable.baseline_menu_book_24);
                builder.setTitle("All Kayaking reservations");
                builder.setMessage("Hold down on the reservation you need to delete");
                gw = new DGW(Kayaking.this,"Reservations",null,1);
                gw.openDB();
                allReservations = gw.getAllKayakingReservations();
                adapter2 = new KayakingAdapter(Kayaking.this,R.layout.kayaking_reservation_lstview,allReservations);
                ListView lstView = new ListView(Kayaking.this);
                lstView.setAdapter(adapter2);
                gw.closeDB();
                builder.setView(lstView);

                lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        gw = new DGW(Kayaking.this,"Reservations",null,1);
                        gw.openDB();
                        int resIdToDelete = allReservations.get(i).id;
                        gw.deleteKayakingReservation(resIdToDelete);
                        gw.close();
                        allReservations.remove(i);
                        adapter2.notifyDataSetChanged();
                        Toast.makeText(Kayaking.this,"Reservation deleted",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    public void calculateCost(){
        cost = cost  * 1.14;
        BigDecimal roundedCost = BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP);
        txtViewTotal.setText(roundedCost + "$");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem m1 = menu.add(Menu.NONE, Menu.FIRST,0,"Main Page");
        MenuItem m2 = menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Camping");
        MenuItem m3 = menu.add(Menu.NONE, Menu.FIRST + 2, 2, "Riding");
        MenuItem m4 = menu.add(Menu.NONE, Menu.FIRST + 3, 3, "Hiking");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent i;
        switch (item.getItemId()) {
            case Menu.FIRST:
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 1:
                i = new Intent(this, Camping.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 2:
                i = new Intent(this, Riding.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 3:
                i = new Intent(this, Climbing.class);
                startActivity(i);
                return true;
        }
        return true;
    }

}