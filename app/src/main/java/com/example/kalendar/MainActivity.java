package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GestureDetectorCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;


public class MainActivity extends AppCompatActivity {//TODO Absturz wegen offline Setzung --> richtige Stelle finden
    //Thresholds for SwipeDetector
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    // Declare UI elements
    private TextView currentMonthTextView;
    private GridView calendarGridView;
    private Button addAppointmentButton;
    private ImageButton priorMonthButton;
    private ImageButton nextMonthButton;
    private DrawerLayout drawerLayout;
    private SearchView searchView;
    private NavigationView navigationView;
    private boolean darkmode;

    //GestureDetector
    private GestureDetectorCompat mGestureDetector;

    //Empty Days of Month
    int numLeadingEmptyElements;

    //For resultIntent
    private int LAUNCH_SECOND_ACTIVITY = 1;

    //initilize user variable
    private FirebaseUser user;

    // Get the current date and time
    Calendar calendar = Calendar.getInstance();

    // Get the current date and time for colored Bg
    Calendar cal = Calendar.getInstance();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initilise database
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //initialise gesture listener for swipes
        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());

        //Check if a user is logged in
        if (auth.getCurrentUser() == null) {
            //Enable offline
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            // If user is empty, start the RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            // Finish the MainActivity so the user cannot go back to it
            finish();
            return;
        }else {
            //Enable offline
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            user = auth.getCurrentUser();
        }
        setContentView(R.layout.activity_main);

        //Darkmode Pref
        SharedPreferences sharedDarkmode = getSharedPreferences("DarkmodePref", Context.MODE_PRIVATE);
        //set Darkmode based on the current boolean state
        if(sharedDarkmode.getBoolean("Darkmode",false)){
            darkmode = true;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            darkmode = false;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        // Initialize UI elements
        currentMonthTextView = (TextView) findViewById(R.id.current_month_text_view);
        calendarGridView = (GridView) findViewById(R.id.calendar_grid_view);
        addAppointmentButton = (Button) findViewById(R.id.add_appointment_button);
        priorMonthButton = (ImageButton) findViewById(R.id.prior_month_button);
        nextMonthButton = (ImageButton) findViewById(R.id.next_month_button);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("Search for Appointment (Name)");
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Search Bar
                searchView.setIconified(false);
            }
        });
        //Set Search Design
        //this.searchDesign();


        //Send Touch event to GestureDetector from Grid
        calendarGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Pass the touch event to the gesture detector
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });


        // Set initial text for current month TextView
        currentMonthTextView.setText(DateFormat.format("MMMM yyyy", calendar));

        //Set Calender as the first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of the week for the first day of the month
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate the number of leading empty elements needed(Day!=Sunday)
        if (firstDayOfWeek != 1) {
            numLeadingEmptyElements = firstDayOfWeek - Calendar.MONDAY;
        } else {
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (position == cal.get(Calendar.DAY_OF_MONTH) + numLeadingEmptyElements - 1) {
                    if (darkmode) {
                        view.setBackgroundColor(Color.parseColor("#6F2DA8"));
                    } else {
                        view.setBackgroundColor(Color.parseColor("#00BFFF"));
                    }
                }

                return view;
            }
        };

        // Set the adapter for the GridView
        GridView gridView = findViewById(R.id.calendar_grid_view);
        gridView.setAdapter(adapter);

        addAppointmentButton.setVisibility(View.INVISIBLE);

        // Set an OnClickListener for the add appointment Button
        addAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch add appointment activity
                Intent i = new Intent(MainActivity.this, AddAppointmentActivity.class);
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
            }
        });

        // Set the adapter for the GridView
        gridView.setAdapter(adapter);

        // Add a click listener to the GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long day = id - numLeadingEmptyElements + 1;
                // When a date is selected, show the "Add Appointment" button
                addAppointmentButton.setVisibility(View.VISIBLE);
                Log.d("Value", String.valueOf(id));
                Log.d("Leer", String.valueOf(numLeadingEmptyElements));
                //Add the current date to the DateSingleton Class to set the date in the AddAppointmentActivity
                DateSingleton.getInstance().setDate(Integer.parseInt(String.valueOf(day)) , calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            }
        });


        priorMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement the month and update the calendar
                calendar.add(Calendar.MONTH, -1);
                //reset Visibility of Appointment button
                addAppointmentButton.setVisibility(View.INVISIBLE);
                updateCalendar();
            }
        });
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the month and update the calendar
                calendar.add(Calendar.MONTH, 1);
                //reset Visibility of Appointment button
                addAppointmentButton.setVisibility(View.INVISIBLE);
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
                            darkmode = false;
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
                            darkmode = true;
                        }
                        return true;
                    case R.id.logOut:
                        //Logout of the App
                        auth.signOut();
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

        //CardViews for Appointments
        if (AppointmentSingleton.getInstance().getDatabase() != null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // Pass the touch event to the gesture detector
                    mGestureDetector.onTouchEvent(motionEvent);
                    return false;
                }
            });

            CardAdapter cardAdapter = new CardAdapter(user.getUid());
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(cardAdapter);
        }

    }

    private void updateCalendar() {
        // Get the current month and year

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Set the current month TextView
        currentMonthTextView.setText(DateFormat.format("MMMM yyyy", calendar));

        //Set Calender as the first day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of the week for the first day of the month
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate the number of leading empty elements needed(Day!=Sunday)
        if (firstDayOfWeek != 1) {
            numLeadingEmptyElements = firstDayOfWeek - Calendar.MONDAY;
        } else {
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                SharedPreferences sharedDarkmode = getSharedPreferences("DarkmodePref", Context.MODE_PRIVATE);

                boolean darkmode = sharedDarkmode.getBoolean("Darkmode", false);

                View view = super.getView(position, convertView, parent);
                if (cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year) {
                    if (position == cal.get(Calendar.DAY_OF_MONTH) + numLeadingEmptyElements - 1) {
                        if (darkmode) {
                            view.setBackgroundColor(Color.parseColor("#6F2DA8"));
                        } else {
                            view.setBackgroundColor(Color.parseColor("#00BFFF"));
                        }
                    }
                }


                return view;
            }
        };

        // Set the adapter for the GridView
        calendarGridView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                recreate();
                drawerLayout.close();
            }
        } //onActivityResult


    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = moveEvent.getY() - downEvent.getY();
                float diffX = moveEvent.getX() - downEvent.getX();
                //Check if horizontal
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Swipe to the right
                            onSwipeRight();
                        } else {
                            // Swipe to the left
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                // ... handle vertical swipes
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            searchView.setIconified(true);
            return super.onSingleTapUp(e);
        }
    }

    private void onSwipeLeft() {
    }

    private void onSwipeRight() {
        drawerLayout.open();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    ArrayList<Appointment> appointments = new ArrayList<>();



}