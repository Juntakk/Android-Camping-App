package com.example.campingapp;

public class RidingReservation {

    public int id;

    public String date;
    public Double cost;
    public String course;

    public RidingReservation(int id, String date, Double cost, String course) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.course = course;
    }
}
