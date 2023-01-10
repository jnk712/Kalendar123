package com.example.kalendar;

public class DateSingleton {
    private static DateSingleton instance;
    private int[] date;

    private DateSingleton() {
        // Private constructor to prevent other classes from creating instances
    }

    public static DateSingleton getInstance() {
        if (instance == null) {
            instance = new DateSingleton();
        }
        return instance;
    }

    public void setDate(int day, int month, int year) {
        this.date = new int[]{day, month, year};
    }

    public int[] getDate() {
        if(this.date==null){
            date = new int[]{1, 1, 2000};
            return date;
        }
        return date;
    }
}