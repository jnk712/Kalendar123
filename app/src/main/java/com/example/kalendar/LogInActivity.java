package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Date;

public class LogInActivity extends AppCompatActivity {

    private EditText email_input;
    private EditText password_input;
    private DatabaseLokal database = new DatabaseLokal();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email_input = (EditText) findViewById(R.id.email_input);
        password_input = (EditText) findViewById(R.id.password_input);

        // Find the LogIn button
        Button logInButton = findViewById(R.id.confirm_Login);
        // Set an OnClickListener on the confirm button
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the confirm button is clicked and the user exists, start the MainActivity
                Log.d("Values: ", database.toString());
                if(database.searchDatabase(email_input.getText().toString(), password_input.getText().toString())){
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);

                    //Set loggedIn to true
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("LoggedIn", true);
                    editor.apply();

                    // Finish the LogInActivity so the user cannot go back to it
                    finish();
                }
                else{
                    //Set loggedIn to false
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("LoggedIn", false);
                    editor.apply();
                }
            }
        });
    }

    public void addToDatabase(User u){
        this.database.addToDatabase(u);
    }

}