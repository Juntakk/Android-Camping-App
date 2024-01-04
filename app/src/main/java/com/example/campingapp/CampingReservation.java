package com.example.campingapp;

public class CampingReservation {

    public int id;

    public String date;
    public Double cost;
    public Integer days;

    public CampingReservation(int id, String date, Double cost, Integer days) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.days = days;
    }

    public CampingReservation(String date, Double cost, Integer days) {
        this.date = date;
        this.cost = cost;
        this.days = days;
    }
}
