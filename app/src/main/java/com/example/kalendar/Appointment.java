package com.example.kalendar;

public class Appointment {
    private String name;
    private String place;
    private String time;
    private String date;

    public Appointment(String name, String place, String time, String date) {
        this.name = name;
        this.place = place;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
