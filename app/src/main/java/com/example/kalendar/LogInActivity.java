package com.example.kalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText email_input;
    private EditText password_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Enable offline
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        email_input = (EditText) findViewById(R.id.email_input);
        password_input = (EditText) findViewById(R.id.password_input);

        // Find the LogIn button
        Button logInButton = findViewById(R.id.confirm_Login);
        // Set an OnClickListener on the confirm button
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(email_input.getText().toString(), password_input.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    // When the confirm button is clicked, start the MainActivity
                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    // Finish the RegisterActivity so the user cannot go back to it
                                    finish();
                                } else {
                                    // error creating user
                                    Log.d("Firebase Auth", "Sign-in failed", task.getException());
                                    Toast.makeText(LogInActivity.this, "Unable to Log In.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //TODO: Check if the Inputs are valid
            }
        });
    }
}