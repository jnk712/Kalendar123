package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Friend_Activity extends AppCompatActivity {
    private RecyclerView friendsRecyclerView;
    private Friends_Adapter friendsAdapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);

        auth = FirebaseAuth.getInstance();

        friendsRecyclerView = findViewById(R.id.friends_list);
        friendsAdapter = new Friends_Adapter(auth.getUid());
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(Friend_Activity.this));
        friendsRecyclerView.setAdapter(friendsAdapter);

    }
}