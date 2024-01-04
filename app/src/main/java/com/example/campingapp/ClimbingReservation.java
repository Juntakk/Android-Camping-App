package com.example.campingapp;

public class ClimbingReservation {

    public int id;
    public String date;
    public Double cost;
    public String mountain;

    public ClimbingReservation(int id, String date, Double cost, String mountain) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.mountain = mountain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getMountain() {
        return mountain;
    }

    public void setMountain(String mountain) {
        this.mountain = mountain;
    }
}
