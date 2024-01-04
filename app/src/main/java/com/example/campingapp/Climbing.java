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

public class Climbing extends AppCompatActivity {

    public Button btnTotal, btnRemovePerson, btnAddPerson, btnReserve, btnDate, btnMountain, btnShowAll;
    TextView txtViewHours, txtViewTotal, txtViewDate, txtViewMountain;
    public int hours = 0;
    public Double cost;
    public String selectedDate, mountain;
    public String[] mountains = {"Everest","Kilimanjaro","St-Bruno"};
    public DGW gw;
    public Boolean isTotalDisplayed = false;
    public ClimbingAdapter adapter;
    public static ArrayList<ClimbingReservation> allReservations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climbing);

        btnTotal = findViewById(R.id.btnTotal);
        btnRemovePerson = findViewById(R.id.btnRemovePerson);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        btnReserve = findViewById(R.id.btnReserve);
        txtViewHours = findViewById(R.id.txtViewHours);
        txtViewDate = findViewById(R.id.txtViewDate);
        txtViewTotal = findViewById(R.id.txtViewTotal);
        btnMountain = findViewById(R.id.btnMountain);
        txtViewMountain = findViewById(R.id.txtViewMountain);
        btnDate = findViewById(R.id.btnDate);
        btnShowAll = findViewById(R.id.btnShowAllReservations);

        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours = hours + 1;
                txtViewHours.setText(String.valueOf(hours));
            }
        });
        btnRemovePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hours < 1){
                    hours = 0;
                }
                else{
                    hours = hours -1;
                    txtViewHours.setText(String.valueOf(hours));
                }
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(Climbing.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        selectedDate = year + "/" + month + "/" + day;
                        txtViewDate.setText(selectedDate);
                    }
                },year,month,day);
                dp.create();
                dp.show();
            }
        });
        btnMountain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Climbing.this);
                builder.setTitle("Pick a mountain");
                builder.setSingleChoiceItems(mountains, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtViewMountain.setText(mountains[i]);
                        mountain = mountains[i];
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

        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateCost();
                isTotalDisplayed = true;
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedDate == null || hours == 0 || mountain == null || !isTotalDisplayed){
                    Toast.makeText(Climbing.this,"Missing information",Toast.LENGTH_SHORT).show();
                }
                else{
                    gw = new DGW(Climbing.this, "Reservations",null,1);
                    gw.openDB();
                    gw.addClimbingReservation(selectedDate, cost, mountain);
                    gw.closeDB();
                    Toast.makeText(Climbing.this,"Reservation successful",Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });

        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Climbing.this);
                builder.setTitle("All climbing reservations");
                builder.setIcon(R.drawable.baseline_bookmarks_24);
                builder.setMessage("Hold down on the reservation you need to delete");

                gw = new DGW(Climbing.this,"Reservations",null,1);
                gw.openDB();
                allReservations = gw.getAllClimbingReservations();

                ListView lstView = new ListView(Climbing.this);
                adapter = new ClimbingAdapter(Climbing.this, R.layout.climbing_reservation_lstview, allReservations);
                lstView.setAdapter(adapter);

                gw.closeDB();
                builder.setView(lstView);

                lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        gw = new DGW(Climbing.this,"Reservations",null,1);
                        gw.openDB();
                        int id = gw.getAllClimbingReservations().get(i).id;
                        gw.deleteClimbingReservation(id);
                        allReservations.remove(i);
                        gw.closeDB();
                        adapter.notifyDataSetChanged();
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
        int equipmentPrice = 10;
        cost = (equipmentPrice * hours) * 1.14;
        BigDecimal roundedCost = BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP);
        txtViewTotal.setText(roundedCost + "$");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem m1 = menu.add(Menu.NONE, Menu.FIRST,0,"Main Page");
        MenuItem m2 = menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Camping");
        MenuItem m3 = menu.add(Menu.NONE, Menu.FIRST + 2, 2, "Riding");
        MenuItem m4 = menu.add(Menu.NONE, Menu.FIRST + 3, 3, "Kayaking");
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
                i = new Intent(this, Kayaking.class);
                startActivity(i);
                return true;
        }
        return true;
    }
}