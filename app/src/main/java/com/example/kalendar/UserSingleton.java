package com.example.kalendar;

public class UserSingleton {
    private static UserSingleton instance;
    private DatabaseLokal database;

    private UserSingleton() {
        // Private constructor to prevent other classes from creating instances
    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public void setDatabase(DatabaseLokal database) {
        this.database = database;
    }

    public DatabaseLokal getDatabase() {
        return database;
    }
}