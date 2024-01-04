package com.example.campingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Camping extends AppCompatActivity {
    public Button btnDate, btnDay, btnReserve, btnAddPerson, btnRemovePerson, btnTotal, btnShowAll;
    public TextView txtViewDate, txtViewDay, txtViewPeople, txtViewTotal;
    public String[] totalDays = {"1 day","2 days","3 days","4 days","5 days"};
    public DGW gw;
    public String selectedDate;
    public Double cost ;
    public int days ;
    public int people ;
    public Boolean isCostCalculated = false;
    public Boolean isTotalDisplayed = false;
    public static ArrayList<CampingReservation> allReservations = new ArrayList<>();
    public CampingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camping);

        btnDate = findViewById(R.id.btnDate);
        btnDay = findViewById(R.id.btnDays);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        btnRemovePerson = findViewById(R.id.btnRemovePerson);
        btnTotal = findViewById(R.id.btnTotal);
        txtViewDate = findViewById(R.id.txtViewDate);
        txtViewDay = findViewById(R.id.txtViewDay);
        txtViewPeople = findViewById(R.id.txtViewPeople);
        txtViewTotal = findViewById(R.id.txtViewTotal);
        btnReserve = findViewById(R.id.btnReserve);
        btnShowAll = findViewById(R.id.btnShowAllReservations);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(Camping.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        selectedDate = year + "/" + month  + "/" + day;
                        txtViewDate.setText(selectedDate);

                        if(month < 6){
                            cost = 18.90;
                        } else if (month < 9) {
                            cost = 23.25;
                        }else cost = 20.25;
                    }
                },year,month,day);
                dp.create();
                dp.show();
            }
        });
        btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Camping.this);
                builder.setTitle("Please choose how many days");

                builder.setSingleChoiceItems(totalDays, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtViewDay.setText(totalDays[i]);
                        days = i + 1;
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
                if(selectedDate == null || days == 0 || people == 0 || cost == 0){
                    Toast.makeText(Camping.this,"Information missing",Toast.LENGTH_SHORT).show();
                    isCostCalculated = false;
                }
                else{
                    if(!isCostCalculated){
                        calculateCost();
                        isCostCalculated = true;
                    }
                    isTotalDisplayed = true;
            }   }
        });
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                people = people + 1;
                txtViewPeople.setText(String.valueOf(people));
            }
        });
        btnRemovePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(people > 0){
                    people = people - 1;
                    txtViewPeople.setText(String.valueOf(people));
                }

            }
        });
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedDate == null || days == 0 || people == 0 || cost == 0 || isTotalDisplayed == false){
                    Toast.makeText(Camping.this,"Information missing",Toast.LENGTH_SHORT).show();
                }
                else{
                    gw = new DGW(Camping.this, "Reservations", null, 1);
                    gw.openDB();
                    gw.addCampingReservation(selectedDate, cost, days);
                    gw.closeDB();
                    Toast.makeText(Camping.this,"Reservation successful",Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });
        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Camping.this);
                builder.setIcon(R.drawable.baseline_menu_book_24);
                builder.setTitle("All Camping reservations");
                builder.setMessage("Hold down on the reservation you need to delete");
                gw = new DGW(Camping.this,"Reservations",null,1);
                gw.openDB();
                allReservations = gw.getAllCampingReservations();
                adapter = new CampingAdapter(Camping.this,R.layout.camping_reservation_lstview,allReservations);
                ListView lstView = new ListView(Camping.this);
                lstView.setAdapter(adapter);
                gw.closeDB();
                builder.setView(lstView);

                lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        gw = new DGW(Camping.this,"Reservations",null,1);
                        gw.openDB();
//                        allReservations = gw.getAllCampingReservations();
                        int resIdToDelete = allReservations.get(i).id;
                        gw.deleteCampingReservation(resIdToDelete);
                        gw.close();
                        allReservations.remove(i);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Camping.this,"Reservation deleted",Toast.LENGTH_SHORT).show();
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
            cost = ((cost*people) * days) * 1.14;
            BigDecimal roundedCost = BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP);
            txtViewTotal.setText(roundedCost + "$");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem m1 = menu.add(Menu.NONE, Menu.FIRST,0,"Main Page");
        MenuItem m2 = menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Riding");
        MenuItem m3 = menu.add(Menu.NONE, Menu.FIRST + 2, 2, "Kayaking");
        MenuItem m4 = menu.add(Menu.NONE, Menu.FIRST + 3, 3, "Climbing");
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
                i = new Intent(this, Riding.class);
                startActivity(i);
                return true;
            case Menu.FIRST + 2:
                i = new Intent(this, Kayaking.class);
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