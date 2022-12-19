package com.example.kalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AddAppointmentActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText appointmentNameEditText;
    private TimePicker appointmentTimePicker;
    private EditText appointmentPlaceEditText;
    private EditText appointmentDateEditText;
    private Button saveAppointmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        //activate darkmode?
        SharedPreferences sharedPreferences = getSharedPreferences("DarkmodePref", Context.MODE_PRIVATE);
        boolean darkmode = sharedPreferences.getBoolean("Darkmode", false);

        if (darkmode){
            //Turn on darkmode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            //Turn off darkmode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        // Initialize UI elements
        appointmentNameEditText = (EditText) findViewById(R.id.appointment_name_edit_text);
        appointmentTimePicker = (TimePicker) findViewById(R.id.appointment_time_picker);
        appointmentPlaceEditText = (EditText) findViewById(R.id.appointment_place_edit_text);
        appointmentDateEditText = (EditText) findViewById(R.id.appointment_date_edit_text);
        saveAppointmentButton = (Button) findViewById(R.id.save_appointment_button);

        // Set initial values for the appointment time and place
        appointmentTimePicker.setCurrentHour(12);
        appointmentTimePicker.setCurrentMinute(0);
        appointmentPlaceEditText.setText("Conference Room A");

        // Get the intent that started the activity
        Intent intent = getIntent();

        // Get the previously selected date from the intent extras, or use a default value
        String previouslySelectedDate = intent.getStringExtra("SELECTED_DATE") != null ?
                intent.getStringExtra("SELECTED_DATE") : "TODAY";

        // Set the initial value of the appointment date to the previously selected date
        appointmentDateEditText.setText(previouslySelectedDate);

        // Add a click listener to the save appointment button
        saveAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the appointment name, date, time, and place from the UI elements
                String appointmentName = appointmentNameEditText.getText().toString();
                String appointmentDate = appointmentDateEditText.getText().toString();
                int hour = appointmentTimePicker.getCurrentHour();
                int minute = appointmentTimePicker.getCurrentMinute();
                String appointmentTime = hour + ":" + minute;
                String appointmentPlace = appointmentPlaceEditText.getText().toString();

                // TODO: Save the appointment to the database

                // Finish the activity
                finish();
            }
        });
    }
}


