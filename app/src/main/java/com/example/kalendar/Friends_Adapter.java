package com.example.kalendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

    public class Friends_Adapter extends RecyclerView.Adapter<com.example.kalendar.Friends_Adapter.ViewHolder> {
        private List<User> requestList;
        private FirebaseDatabase database;
        private String userID;
        private String itemUserId;

        public Friends_Adapter(String userID) {
            this.userID = userID;
            requestList = new ArrayList<>();
            database = FirebaseDatabase.getInstance();
            getFriends();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView friendName, friendMail;
            private CardView mCardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mCardView = (CardView) itemView.findViewById(R.id.card_view_friends);
                friendName = (TextView) itemView.findViewById(R.id.friend_name);
                friendMail = (TextView) itemView.findViewById(R.id.friend_mail);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item_friends, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.kalendar.Friends_Adapter.ViewHolder holder, int position) {
            User user = requestList.get(position);

            holder.friendName.setText(user.getName());
            holder.friendMail.setText(user.getEmail());
        }

        @Override
        public int getItemCount() {
            return requestList != null ? requestList.size() : 0;
        }

        private void getFriends() {
            DatabaseReference reference = database.getReference("users").child(userID).child("friends");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestList.clear(); // clear the list before adding new data
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        itemUserId = childSnapshot.getKey(); // get the uID of the user who sent the request
                        DatabaseReference userRef = database.getReference("users").child(itemUserId); // reference to the user data
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() { // retrieve the user data
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("name").getValue(String.class); // get the name of the user
                                String mail = dataSnapshot.child("mail").getValue(String.class); // get the email of the user
                                Log.d("Name: ", name);
                                User user = new User(itemUserId, name, mail);

                                Log.d("Name", mail);
                                requestList.add(user);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //TODO Write result to cancelation
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //TODO Write result to cancelation
                }
            });
        }
    }
