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

public class Riding extends AppCompatActivity {

    Button btnDate, btnTotal, btnRemovePerson, btnAddPerson, btnCourse, btnReserve, btnShowAll;
    TextView txtViewPeople, txtViewDate,txtViewCourse, txtViewTotal;
    public int people, dayOfWeek;
    public DGW gw;
    public Double cost;
    public String selectedDate, course;
    public String[] courses = {"Course 1","Course 2"};
    public Boolean isCostCalculated = false;
    public Boolean isTotalCalculated = false;
    public static ArrayList<RidingReservation> allReservations = new ArrayList<>();
    public RidingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        btnDate=findViewById(R.id.btnDate);
        btnTotal=findViewById(R.id.btnTotal);
        btnRemovePerson = findViewById(R.id.btnRemovePerson);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        btnReserve = findViewById(R.id.btnReserve);
        btnCourse = findViewById(R.id.btnCourse);
        txtViewPeople = findViewById(R.id.txtViewPeople);
        txtViewDate =findViewById(R.id.txtViewDate);
        txtViewCourse = findViewById(R.id.txtViewCourse);
        txtViewTotal = findViewById(R.id.txtViewTotal);
        btnShowAll = findViewById(R.id.btnShowAllReservations);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(Riding.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        selectedDate = (year + "/" + month + "/" + day).toString();
                        txtViewDate.setText(selectedDate);

                        c.set(year, month -1, day);
                        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                        if ((dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) && course == ("Course 1")) {
                            cost = 18.25;
                        } else if ((dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) && course == ("Course 2")) {
                            cost = 25d;
                        } else if (dayOfWeek > Calendar.SUNDAY && dayOfWeek < Calendar.SATURDAY && course == ("Course 1")) {
                            cost = 15.25;
                        } else if (dayOfWeek > Calendar.SUNDAY && dayOfWeek < Calendar.SATURDAY && course == ("Course 2")) {
                            cost = 22.75;
                        } else {
                            Toast.makeText(Riding.this, "Please choose a course first", Toast.LENGTH_SHORT).show();
                        }
                    }
                },year,month,day);

                dp.create();
                dp.show();
            }
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
        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Riding.this);
                builder.setTitle("Pick a course");
                builder.setSingleChoiceItems(courses, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtViewCourse.setText(courses[i]);
                        course = courses[i];
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
                if(selectedDate == null || course == null || people == 0 || cost == 0){
                    Toast.makeText(Riding.this,"Information missing",Toast.LENGTH_SHORT).show();
                    isCostCalculated = false;
                }
                else{
                    if(!isCostCalculated){
                        calculateCost();
                        isCostCalculated = true;
                        isTotalCalculated = true;
                    }
                }
            }
        });
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedDate == null || course == null || people == 0 || cost == 0 || isTotalCalculated == false){
                    Toast.makeText(Riding.this,"Information missing",Toast.LENGTH_SHORT).show();
                }
                else{
                    gw = new DGW(Riding.this, "Reservations", null, 1);
                    gw.openDB();
                    gw.addRidingReservation(selectedDate, cost, course);
                    gw.closeDB();
                    Toast.makeText(Riding.this,"Reservation successful",Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });
        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Riding.this);
                builder.setIcon(R.drawable.baseline_bookmarks_24);
                builder.setTitle("All Riding reservations");
                builder.setMessage("Hold down on the reservation you want to delete");
                gw = new DGW(Riding.this,"Reservations",null,1);
                gw.openDB();
                allReservations = gw.getAllRidingReservations();
                adapter = new RidingAdapter(Riding.this,R.layout.riding_reservation_lstview,allReservations);
                ListView lstView = new ListView(Riding.this);
                lstView.setAdapter(adapter);
                gw.closeDB();
                builder.setView(lstView);

                lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        gw = new DGW(Riding.this,"Reservations",null,1);
                        gw.openDB();
                        int resIdToDelete = allReservations.get(i).id;
                        gw.deleteRidingReservation(resIdToDelete);
                        gw.close();
                        allReservations.remove(i);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Riding.this,"Reservation deleted",Toast.LENGTH_SHORT).show();
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
        cost = (cost * people) * 1.14;
        BigDecimal roundedCost = BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP);
        txtViewTotal.setText(roundedCost + "$");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem m1 = menu.add(Menu.NONE, Menu.FIRST,0,"Main Page");
        MenuItem m2 = menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Camping");
        MenuItem m3 = menu.add(Menu.NONE, Menu.FIRST + 2, 2, "Kayaking");
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
