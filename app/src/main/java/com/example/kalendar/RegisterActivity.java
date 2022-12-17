package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.content.SharedPreferences;
import android.content.Context;

import java.sql.Date;
import java.time.Year;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private EditText name_input;
    private EditText email_input;
    private EditText password_input;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();

        setContentView(R.layout.activity_register);

        name_input = (EditText) findViewById(R.id.name_input);
        email_input = (EditText) findViewById(R.id.email_input);
        password_input = (EditText) findViewById(R.id.password_input);

        dayPicker = findViewById(R.id.day_picker);
        monthPicker = findViewById(R.id.month_picker);
        yearPicker = findViewById(R.id.year_picker);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  //TODO: Maximum Amount of Days equal to Days in Month
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(Year.now().getValue());
        dayPicker.setValue(1);
        monthPicker.setValue(1);
        yearPicker.setValue(2000);

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Update the maximum value of the day picker based on the month and year selected
                calendar.set(yearPicker.getValue(), newVal - 1, 1);
                dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        });

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Update the maximum value of the day picker based on the month and year selected
                calendar.set(newVal, monthPicker.getValue() - 1, 1);
                dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        });

        // Find the confirm button
        Button confirmButton = findViewById(R.id.confirm_button);
        // Set an OnClickListener on the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the confirm button is clicked, start the MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);

                //Save the Account of the User in an Object
                new User(name_input.getText().toString(), email_input.getText().toString(), Date.valueOf(String.valueOf(yearPicker.getValue()) + "-" + String.valueOf(monthPicker.getValue()) + "-" + String.valueOf(dayPicker.getValue())), password_input.getText().toString());
                //TODO: Fix inputs being null(How get inputted String)
                //TODO: User Object in Database

                Log.d("Test: ", new User(name_input.getText().toString(), email_input.getText().toString(), Date.valueOf(String.valueOf(yearPicker.getValue()) + "-" + String.valueOf(monthPicker.getValue()) + "-" + String.valueOf(dayPicker.getValue())), password_input.getText().toString()).toString());

                //Set loggedIn to true
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("LoggedIn", true);
                editor.apply();

                // Finish the RegisterActivity so the user cannot go back to it
                finish();
            }
        });
    }
}