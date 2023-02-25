package com.example.kalendar;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
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
import java.util.Arrays;
import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.ViewHolder> {
    private List<User> userList;
    private int position;
    private String itemUserId;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getItemUserId() {
        return itemUserId;
    }

    public void setItemUserId(String itemUserId) {
        this.itemUserId = itemUserId;
    }

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
        holder.emailTextView.setText(user.getEmail());

        // Set the OnCreateContextMenuListener for the itemView
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                // Inflate the menu from a resource file
                MenuInflater inflater = new MenuInflater(v.getContext());
                inflater.inflate(R.menu.friend_optionsmenu, menu);

                // Set the title for the menu
                menu.setHeaderTitle(String.valueOf(userList.get(holder.getAdapterPosition()).getName()));
                // Save the position of the RecyclerView item in the variable
                setPosition(holder.getAdapterPosition());
                //Save the User ID of the selected Item
                setItemUserId(userList.get(getPosition()).getId());
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            private float x;
            private float y;
            private long startClickTime;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //Check if Scroll or Touch
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startClickTime = System.currentTimeMillis();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                        // Touch was a simple tap
                        x = event.getX();
                        y = event.getY();
                        //Show menu
                        holder.itemView.showContextMenu(x, y);
                    }
                    else {
                        // Touch was a not a simple tap
                    }
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public void searchForUsers(String searchQuery) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userAuth = auth.getCurrentUser();
        String currentUserID = userAuth.getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.orderByChild("name").startAt(searchQuery).endAt(searchQuery + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    if(!currentUserID.equals(userId)) {
                        String name = childSnapshot.child("name").getValue(String.class);
                        String mail = childSnapshot.child("mail").getValue(String.class);
                        User user = new User(userId, name, mail);
                        userList.add(user);
                    }
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
        if(userList!=null) {
            userList.clear();
            notifyDataSetChanged();
        }
    }
}

