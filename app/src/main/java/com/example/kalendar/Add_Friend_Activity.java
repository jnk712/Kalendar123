package com.example.kalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Add_Friend_Activity extends AppCompatActivity {
    private RecyclerView searchRecyclerView;
    private Search_Adapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends);

        SearchView searchView = findViewById(R.id.friend_search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Search Bar
                searchView.setIconified(false);
            }
        });

        searchRecyclerView = findViewById(R.id.user_list);
        searchAdapter = new Search_Adapter();
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Toast.makeText(Add_Friend_Activity.this, "Position: " + rv.getChildItemId(rv.getFocusedChild()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.searchForUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchAdapter.clear();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuFriend) {
        getMenuInflater().inflate(R.menu.friend_optionsmenu, menuFriend);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_request:
                // send friend request logic here
                this.sendRequest(searchAdapter.getItemUserId());
                return true;
            case R.id.action_block:
                // block logic here
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void sendRequest(String receiverId){
        // Get a reference to the users node
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        // Get the current user id
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersRef.child(currentUserId).child("sentRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(receiverId)) {
                    // Check if the sender and receiver are not already friends
                    usersRef.child(currentUserId).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(receiverId)) {
                                // TODO Send a push notification to the receiver

                                // Update the sentRequests node of the sender
                                usersRef.child(currentUserId).child("sentRequests").child(receiverId).setValue(true);
                                // Update the receivedRequests node of the receiver
                                usersRef.child(receiverId).child("receivedRequests").child(currentUserId).setValue(true);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void blockUser(String uId){

    }
}