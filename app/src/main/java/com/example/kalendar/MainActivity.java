package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.ui.NavigationUI;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    // Declare UI elements
    private TextView currentMonthTextView;
    private GridView calendarGridView;
    private Button addAppointmentButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean("LoggedIn", false);
        //For Developing Purposes Remove ! to Reverse sharedPreferences
        // Add this line to check if loggedIn is false
        if (loggedIn) {
            // If loggedIn is false, start the RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            // Finish the MainActivity so the user cannot go back to it
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        currentMonthTextView = (TextView) findViewById(R.id.current_month_text_view);
        calendarGridView = (GridView) findViewById(R.id.calendar_grid_view);
        addAppointmentButton = (Button) findViewById(R.id.add_appointment_button);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Set initial text for current month TextView
        currentMonthTextView.setText("December 2022");

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

// Create an array of the days in the current month
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] days = new String[numDays];
        for (int i = 0; i < numDays; i++) {
            days[i] = String.valueOf(i + 1);
        }

// Create a new adapter to provide the data for the GridView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, days);

// Set the adapter for the GridView
        GridView gridView = findViewById(R.id.calendar_grid_view);
        gridView.setAdapter(adapter);
        addAppointmentButton.setVisibility(View.INVISIBLE);

        // Set an OnClickListener for the add appointment Button
        addAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch add appointment activity
                Intent intent = new Intent(MainActivity.this, AddAppointmentActivity.class);
                startActivity(intent);
            }
        });

        // Set the adapter for the GridView
        gridView.setAdapter(adapter);

// Add a click listener to the GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When a date is selected, show the "Add Appointment" button
                addAppointmentButton.setVisibility(View.VISIBLE);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.end_app_menu_item:
                        // End the app
                        finish();
                        return true;
                    case R.id.add_friends_menu_item:
                        // Add a friend
                        // TODO: Database Search
                        return true;
                    case R.id.toggle_dark_mode_menu_item:
                        // Toggle dark mode
                        // ...
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}