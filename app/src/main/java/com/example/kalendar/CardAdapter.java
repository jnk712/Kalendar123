package com.example.kalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private ArrayList<Appointment> mData;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mname;
        public TextView mplace;
        public TextView mdate;
        public TextView mtime;


        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mname = (TextView) v.findViewById(R.id.appointment_name);
            mplace = (TextView) v.findViewById(R.id.appointment_place);
            mdate = (TextView) v.findViewById(R.id.appointment_date);
            mtime = (TextView) v.findViewById(R.id.appointment_time);
        }
    }

    public CardAdapter(ArrayList<Appointment> data) {
        mData = data;
    }

    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mname.setText(mData.get(position).getName());
        holder.mplace.setText(mData.get(position).getPlace());
        holder.mtime.setText(mData.get(position).getTime());
        holder.mdate.setText(mData.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
