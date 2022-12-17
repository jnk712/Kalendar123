package com.example.kalendar;

import java.sql.Date;

public class User {
   private String name;
   private String email;
   private Date birthDate;
   private String password;

    public User(String name, String email, Date birthDate, String password) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", password='" + password + '\'' +
                '}';
    }
}
