package com.example.kalendar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.ViewHolder> {
    private List<User> userList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.friend_name);
            emailTextView = itemView.findViewById(R.id.friend_mail);
        }
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public void searchForUsers(String searchQuery) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userAuth = auth.getCurrentUser();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.orderByChild("name").startAt(searchQuery).endAt(searchQuery + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    String name = childSnapshot.child("name").getValue(String.class);
                    User user = new User(userId, name, userAuth.getEmail());
                    userList.add(user);
                }
                setUserList(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Search_Adapter", "Failed to search for users.", databaseError.toException());
            }
        });
    }
    public void clear() {
        userList.clear();
        notifyDataSetChanged();
    }
}

