package com.example.kalendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Search_Adapter extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView date;
    private TextView mail;
    private CardView cardView;

    public Search_Adapter(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_view_friends);
        name = itemView.findViewById(R.id.friend_name);
        date = itemView.findViewById(R.id.friend_date);
        mail = itemView.findViewById(R.id.friend_mail);
    }

    public void bind(User model) {
        name.setText(model.getName());
        mail.setText(model.getEmail());
        date.setText(model.getBirthDate().toString());
    }
}
