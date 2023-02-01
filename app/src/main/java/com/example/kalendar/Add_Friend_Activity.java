package com.example.kalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ui.firestore.FirebaseRecyclerOptions;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<User, Search_Adapter> adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends);

        searchView = findViewById(R.id.friend_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the data in the adapter based on the search query
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        recyclerView = findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("your_database_node_name");

        FirebaseRecyclerOptions<YourDataModelClass> options = new FirebaseRecyclerOptions.Builder<YourDataModelClass>()
                .setQuery(databaseReference, YourDataModelClass.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<YourDataModelClass, YourViewHolderClass>(options) {
            @Override
            protected void onBindViewHolder(@NonNull YourViewHolderClass holder, int position, @NonNull YourDataModelClass model) {
                // Bind data from the model to the view holder
            }

            @NonNull
            @Override
            public YourViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the layout for the view holder
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
}