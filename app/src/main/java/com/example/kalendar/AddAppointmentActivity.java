package com.example.kalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.time.Year;
import java.util.Calendar;

public class AddAppointmentActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText appointmentNameEditText;
    private TimePicker appointmentTimePicker;
    private EditText appointmentPlaceEditText;
    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private Button saveAppointmentButton;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();

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

        appointmentTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay > 23){
                    appointmentTimePicker.setHour(0);
                }
            }
        });

        dayPicker = findViewById(R.id.day_picker2);
        monthPicker = findViewById(R.id.month_picker2);
        yearPicker = findViewById(R.id.year_picker2);

        saveAppointmentButton = (Button) findViewById(R.id.save_appointment_button);

        //Set max and min values
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(Year.now().getValue() + 100);
        dayPicker.setValue(DateSingleton.getInstance().getDate()[0]);
        monthPicker.setValue(DateSingleton.getInstance().getDate()[1]);
        yearPicker.setValue(DateSingleton.getInstance().getDate()[2]);

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

        // Set initial values for the appointment time and place
        appointmentTimePicker.setIs24HourView(true);
        appointmentTimePicker.setCurrentHour(0);
        appointmentTimePicker.setCurrentMinute(0);

        // Add a click listener to the save appointment button
        saveAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the appointment name, date, time, and place from the UI elements
                String appointmentName = appointmentNameEditText.getText().toString();
                int hour = appointmentTimePicker.getCurrentHour();
                int minute = appointmentTimePicker.getCurrentMinute();
                String appointmentTime = hour + ":" + minute;
                String appointmentPlace = appointmentPlaceEditText.getText().toString();
                String appointmentDate = String.valueOf(yearPicker.getValue()) + "-" + String.valueOf(monthPicker.getValue()) + "-" + String.valueOf(dayPicker.getValue());

                // TODO: Save the appointment to the database
                AppointmentDatabase database = AppointmentSingleton.getInstance().getDatabase();
                database.addToDatabase(new Appointment(appointmentName, appointmentPlace, appointmentTime, appointmentDate));

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",true);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                // Finish the activity
                finish();
            }
        });
    }
}


