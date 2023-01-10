package com.example.kalendar;

public class AppointmentSingleton {
    private static AppointmentSingleton instance;
    private AppointmentDatabase database;

    private AppointmentSingleton() {
        // Private constructor to prevent other classes from creating instances
    }

    public static AppointmentSingleton getInstance() {
        if (instance == null) {
            instance = new AppointmentSingleton();
        }
        return instance;
    }

    public void setDatabase(AppointmentDatabase database) {
        this.database = database;
    }

    public AppointmentDatabase getDatabase() {
        if(this.database==null){
            database = new AppointmentDatabase();
            return database;
        }
        return database;
    }
}