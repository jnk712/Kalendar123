package com.example.kalendar;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private FirebaseDatabase database;
    private String userID;

    public CardAdapter(String userID) {
        this.userID = userID;
        appointments = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        getAppointments();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.mName.setText(appointment.getName());
        holder.mPlace.setText(appointment.getPlace());
        holder.mDate.setText(appointment.getDate());
        holder.mTime.setText(appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    private void getAppointments() {
        DatabaseReference reference = database.getReference("users").child(userID).child("appointments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String place = snapshot.child("place").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);
                    appointments.add(new Appointment(name, place, date, time));
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO Write result to cancelation
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName, mPlace, mDate, mTime;
        private CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.appointment_name);
            mPlace = (TextView) itemView.findViewById(R.id.appointment_place);
            mDate = (TextView) itemView.findViewById(R.id.appointment_date);
            mTime = (TextView) itemView.findViewById(R.id.appointment_time);
        }
    }
}
