package com.example.kalendar;

import java.util.ArrayList;
import java.sql.Date;
import java.util.Arrays;

public class DatabaseLokal {
    private  ArrayList<User> database = new ArrayList<User>(Arrays.asList(new User("Jannik", "jannik2005.kruger@gmail.com", Date.valueOf("2005-3-14"), "Passwort")));

    public boolean searchDatabase(String email, String password){
        for(int i = 0; i<database.size(); i++){
            if(database.get(i).getEmail().equals(email) && database.get(i).getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public void addToDatabase(User u){
        database.add(u);
    }

    @Override
    public String toString() {
        return "DatabaseLokal{" +
                "database=" + database +
                '}';
    }
}
