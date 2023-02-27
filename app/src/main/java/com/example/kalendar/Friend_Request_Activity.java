package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Friend_Request_Activity extends AppCompatActivity {
    private RecyclerView requestsRecyclerView;
    private Request_Adapter requestAdapter;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_requests);

        auth = FirebaseAuth.getInstance();

        requestsRecyclerView = findViewById(R.id.request_list);

        requestAdapter = new Request_Adapter(auth.getUid());
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(Friend_Request_Activity.this));
        requestsRecyclerView.setAdapter(requestAdapter);
    }
}