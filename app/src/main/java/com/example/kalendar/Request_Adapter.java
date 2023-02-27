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

public class Request_Adapter extends RecyclerView.Adapter<Request_Adapter.ViewHolder> {
    private List<User> requestList;
    private FirebaseDatabase database;
    private String userID;
    private String itemUserId;

    public Request_Adapter(String userID) {
        this.userID = userID;
        requestList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        Log.d("Adapter", "Adapter constructor called");
        getRequests();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView requestorName, requestorMail;
        private CardView mCardView;
        private Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_requests);
            requestorName = (TextView) itemView.findViewById(R.id.name_request);
            requestorMail = (TextView) itemView.findViewById(R.id.mail_request);
            acceptButton = itemView.findViewById(R.id.accept_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_requests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = requestList.get(position);
        Log.d("Request_Adapter", "User name: " + user.getName() + ", email: " + user.getEmail());

        holder.requestorName.setText(user.getName());
        holder.requestorMail.setText(user.getEmail());
        //TODO richtiges Onclick
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String senderId = requestList.get(holder.getAdapterPosition()).getId();
                // Add the sender ID to the "friends" node of the current user
                DatabaseReference friendsReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("friends").child(senderId);
                friendsReference.setValue(true);

                // Add the current user ID to the "friends" node of the sender
                DatabaseReference senderFriendsRef = FirebaseDatabase.getInstance().getReference().child("users").child(senderId).child("friends").child(userID);
                senderFriendsRef.setValue(true);

                // Remove the friend request from the database
                DatabaseReference friendRequestRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("receivedRequests").child(senderId);
                friendRequestRef.removeValue();
                DatabaseReference friendRequestRefSend = FirebaseDatabase.getInstance().getReference().child("users").child(senderId).child("sentRequests").child(userID);
                friendRequestRefSend.removeValue();

                // Update the UI to remove the friend request from the list
                requestList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("Size: ", "Test");
        return requestList != null ? requestList.size() : 0;
    }

    private void getRequests() {
        DatabaseReference reference = database.getReference("users").child(userID).child("receivedRequests");
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
                            requestList.add(user);
                            Log.d("Email in Liste: ", requestList.get(0).getEmail());
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
