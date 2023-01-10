package com.example.kalendar;

import java.util.ArrayList;

public class AppointmentDatabase {
    private ArrayList<Appointment> database = new ArrayList<>();

    public void addToDatabase(Appointment a){
        this.database.add(a);
    }

    public ArrayList<Appointment> getDatabase() {
        return database;
    }
}
