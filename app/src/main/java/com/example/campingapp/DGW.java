package com.example.campingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DGW extends SQLiteOpenHelper {
    public SQLiteDatabase db;
    public DGW(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String myQuery0 = "CREATE TABLE if not exists Customer(id integer primary key autoincrement, name text, email text);";
        String myQuery1 = "CREATE TABLE if not exists CampingReservation(id integer primary key autoincrement, date text, cost double, days int);";
        String myQuery2 = "CREATE TABLE if not exists RidingReservation(id integer primary key autoincrement, date text, cost double, course int);";
        String myQuery3 = "CREATE TABLE if not exists KayakingReservation(id integer primary key autoincrement, date text, cost double, color text);";
        String myQuery4 = "CREATE TABLE if not exists ClimbingReservation(id integer primary key autoincrement, date text, cost double, mountain text);";

        sqLiteDatabase.execSQL(myQuery0);
        sqLiteDatabase.execSQL(myQuery1);
        sqLiteDatabase.execSQL(myQuery2);
        sqLiteDatabase.execSQL(myQuery3);
        sqLiteDatabase.execSQL(myQuery4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public void openDB(){
        this.db = getWritableDatabase();
    }
    public void closeDB(){
        this.db.close();
    }
    public void addCampingReservation(String date, Double cost, int days){

        ContentValues cv = new ContentValues();
        cv.put("date", String.valueOf(date));
        cv.put("cost", String.valueOf(cost));
        cv.put("days",String.valueOf(days));

        db.insert("CampingReservation",null,cv);
    }
    public void addRidingReservation(String date, Double cost,String course){
        ContentValues cv = new ContentValues();
        cv.put("date", String.valueOf(date));
        cv.put("cost", String.valueOf(cost));
        cv.put("course",course);

        db.insert("RidingReservation",null,cv);
    }
    public void addKayakingReservation(String date, Double cost, String color){
        ContentValues cv = new ContentValues();
        cv.put("date", String.valueOf(date));
        cv.put("cost", String.valueOf(cost));
        cv.put("color",color);

        db.insert("KayakingReservation",null,cv);
    }
    public void addClimbingReservation(String date, Double cost, String mountain){
        ContentValues cv = new ContentValues();
        cv.put("date", String.valueOf(date));
        cv.put("cost", String.valueOf(cost));
        cv.put("mountain",mountain);

        db.insert("ClimbingReservation",null,cv);
    }
    public void addCustomer(String name, String email){
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("email",email);

        db.insert("Customer",null,cv);
    }
    public void deleteCampingReservation(int id){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        this.db.delete("CampingReservation", "id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteRidingReservation(int id){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        this.db.delete("RidingReservation", "id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteKayakingReservation(int id){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        this.db.delete("KayakingReservation", "id = ?", new String[]{String.valueOf(id)});
    }
    public void deleteClimbingReservation(int id){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        this.db.delete("ClimbingReservation", "id = ?", new String[]{String.valueOf(id)});
    }
    public ArrayList<CampingReservation> getAllCampingReservations() {

        ArrayList<CampingReservation> allReservations = new ArrayList<>();
        Cursor c ;

        c = this.db.rawQuery("select * from CampingReservation", null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int dateIndex = c.getColumnIndex("date");
            int costIndex = c.getColumnIndex("cost");
            int daysIndex = c.getColumnIndex("days");

            do {
                allReservations.add(
                        new CampingReservation(
                                c.getInt(idIndex),
                                c.getString(dateIndex),
                                c.getDouble(costIndex),
                                c.getInt(daysIndex)
                        )
                );
            } while (c.moveToNext());
        }
        return allReservations;
    }
    public ArrayList<RidingReservation> getAllRidingReservations() {

        ArrayList<RidingReservation> allReservations = new ArrayList<>();
        Cursor c ;

        c = this.db.rawQuery("select * from RidingReservation", null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int dateIndex = c.getColumnIndex("date");
            int costIndex = c.getColumnIndex("cost");
            int courseIndex = c.getColumnIndex("course");

            do {
                allReservations.add(
                        new RidingReservation(
                                c.getInt(idIndex),
                                c.getString(dateIndex),
                                c.getDouble(costIndex),
                                c.getString(courseIndex)
                        )
                );
            } while (c.moveToNext());
        }
        return allReservations;
    }
    public ArrayList<KayakingReservation> getAllKayakingReservations() {

        ArrayList<KayakingReservation> allReservations = new ArrayList<>();
        Cursor c ;

        c = this.db.rawQuery("select * from KayakingReservation", null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int dateIndex = c.getColumnIndex("date");
            int costIndex = c.getColumnIndex("cost");
            int colorIndex = c.getColumnIndex("color");

            do {
                allReservations.add(
                        new KayakingReservation(
                                c.getInt(idIndex),
                                c.getString(dateIndex),
                                c.getDouble(costIndex),
                                c.getString(colorIndex)
                        )
                );
            } while (c.moveToNext());
        }
        return allReservations;
    }
    public ArrayList<ClimbingReservation> getAllClimbingReservations() {

        ArrayList<ClimbingReservation> allReservations = new ArrayList<>();
        Cursor c ;

        c = this.db.rawQuery("select * from ClimbingReservation", null);

        if (c != null && c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int dateIndex = c.getColumnIndex("date");
            int costIndex = c.getColumnIndex("cost");
            int mountainIndex = c.getColumnIndex("mountain");

            do {
                allReservations.add(
                        new ClimbingReservation(
                                c.getInt(idIndex),
                                c.getString(dateIndex),
                                c.getDouble(costIndex),
                                c.getString(mountainIndex)
                        )
                );
            } while (c.moveToNext());
        }
        return allReservations;
    }
}


