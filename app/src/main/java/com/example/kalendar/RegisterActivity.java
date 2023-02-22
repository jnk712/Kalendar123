package com.example.kalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private EditText name_input;
    private EditText email_input;
    private EditText password_input;
    private Calendar calendar;

    private LogInActivity logInActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        calendar = Calendar.getInstance();

        setContentView(R.layout.activity_register);

        name_input = (EditText) findViewById(R.id.name_input);
        email_input = (EditText) findViewById(R.id.email_input);
        password_input = (EditText) findViewById(R.id.password_input);

        dayPicker = findViewById(R.id.day_picker);
        monthPicker = findViewById(R.id.month_picker);
        yearPicker = findViewById(R.id.year_picker);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(Year.now().getValue());
        dayPicker.setValue(1);
        monthPicker.setValue(1);
        yearPicker.setValue(2000);

        //internet check
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Log.d("Internet", "Connected");
        } else {
            Log.d("Internet", "Not Connected");
        }

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
                auth.createUserWithEmailAndPassword(email_input.getText().toString(), password_input.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = task.getResult().getUser();
                                    String userId = user.getUid();
                                    DatabaseReference userRef = database.getReference("users").child(userId);
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("name", name_input.getText().toString());
                                    updates.put("birthdate", String.valueOf(yearPicker.getValue()) + "-" + String.valueOf(monthPicker.getValue()) + "-" + String.valueOf(dayPicker.getValue()));
                                    updates.put("mail", email_input.getText().toString());
                                    userRef.updateChildren(updates);

                                    // When the confirm button is clicked, start the MainActivity
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    // Finish the RegisterActivity so the user cannot go back to it
                                    finish();
                                } else {
                                    // error creating user
                                    Log.d("Firebase Auth", "Sign-in failed", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Unable to create user.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //TODO: Check if the Inputs are valid
            }
        });


        // Find the LogIn button
        Button logInButton = findViewById(R.id.account_button);
        // Set an OnClickListener on the confirm button
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the LogIn Button is pressed start the Login Activity
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(intent);
                // Finish the RegisterActivity so the user cannot go back to it
                finish();
            }
        });
    }
}

