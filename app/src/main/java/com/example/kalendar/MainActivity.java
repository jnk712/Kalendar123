package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
    private ImageButton priorMonthButton;
    private ImageButton nextMonthButton;

    private boolean loggedIn;

    // Get the current date and time
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Login Pref
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean("LoggedIn", false);
        //For Developing Purposes Remove ! to Reverse sharedPreferences
        // Add this line to check if loggedIn is false
        if (!loggedIn) {
            // If loggedIn is false, start the RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            // Finish the MainActivity so the user cannot go back to it
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        //Darkmode Pref
        SharedPreferences sharedDarkmode = getSharedPreferences("DarkmodePref", Context.MODE_PRIVATE);

        boolean darkmode = sharedDarkmode.getBoolean("Darkmode", false);


        // Initialize UI elements
        currentMonthTextView = (TextView) findViewById(R.id.current_month_text_view);
        calendarGridView = (GridView) findViewById(R.id.calendar_grid_view);
        addAppointmentButton = (Button) findViewById(R.id.add_appointment_button);
        priorMonthButton = (ImageButton) findViewById(R.id.prior_month_button);
        nextMonthButton = (ImageButton) findViewById(R.id.next_month_button);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Set initial text for current month TextView
        currentMonthTextView.setText("December 2022");

        //Set Calender as the first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of the week for the first day of the month
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate the number of leading empty elements needed(Day!=Sunday)
        int numLeadingEmptyElements;

        if(firstDayOfWeek != 1){
            numLeadingEmptyElements = firstDayOfWeek - Calendar.MONDAY;
        }
        else{
            numLeadingEmptyElements = firstDayOfWeek + 5;
        }


        // Create an array of the days in the current month
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] days = new String[numDays + numLeadingEmptyElements];

        //Fill empty Fields
        for (int i = 0; i < numLeadingEmptyElements; i++) {
            days[i] = "";
        }
        //Fill the rest
        for (int i = numLeadingEmptyElements; i < numDays + numLeadingEmptyElements; i++) {
            days[i] = String.valueOf(i - numLeadingEmptyElements + 1);
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



        priorMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement the month and update the calendar
                calendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the month and update the calendar
                calendar.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Configuration config = getResources().getConfiguration();
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
                        if ((config.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                            // App is in dark mode
                            //Set App to Light Mode
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            // Restart the activity to apply the light theme
                            recreate();
                            //Tell other Activities to activate Lightmode
                            SharedPreferences sharedDarkmode = getSharedPreferences("DarkmodePref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedDarkmode.edit();
                            editor.putBoolean("Darkmode", false);
                            editor.apply();
                        } else {
                            // App is not in dark mode
                            //Set App to Darkmode
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            // Restart the activity to apply the dark theme
                            recreate();
                            //Tell other Activities to activate Lightmode
                            SharedPreferences sharedDarkmode = getSharedPreferences("DarkmodePref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedDarkmode.edit();
                            editor.putBoolean("Darkmode", true);
                            editor.apply();
                        }
                        return true;
                    case R.id.logOut:
                        //Logout of the App and return to the Registration Page
                        loggedIn = sharedPreferences.getBoolean("LoggedIn", false);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("LoggedIn", false);
                        editor.apply();
                        //Open Registration Page
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        // Finish the MainActivity so the user cannot go back to it
                        finish();
                    default:
                        return false;
                }
            }
        });
    }

    private void updateCalendar() {
        // Get the current month and year

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int numLeadingEmptyElements;

        // Set the current month TextView
        currentMonthTextView.setText(DateFormat.format("MMMM yyyy", calendar));

        //Set Calender as the first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of the week for the first day of the month
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate the number of leading empty elements needed(Day!=Sunday)
        if(firstDayOfWeek != 1){
            numLeadingEmptyElements = firstDayOfWeek - Calendar.MONDAY;
        }
        else{
            numLeadingEmptyElements = firstDayOfWeek + 5;
        }


        // Create an array of the days in the current month
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] days = new String[numDays + numLeadingEmptyElements];
        //Fill empty Fields
        for (int i = 0; i < numLeadingEmptyElements; i++) {
            days[i] = "";
        }
        //Fill the rest
        for (int i = numLeadingEmptyElements; i < numDays + numLeadingEmptyElements; i++) {
            days[i] = String.valueOf(i - numLeadingEmptyElements + 1);
        }

        // Create a new adapter to provide the data for the GridView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, days);

        // Set the adapter for the GridView
        calendarGridView.setAdapter(adapter);
    }
}