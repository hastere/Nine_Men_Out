package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BetsAdapter extends FirestoreRecyclerAdapter<Bets, BetsAdapter.BetHolder> {

    public BetsAdapter(@NonNull FirestoreRecyclerOptions<Bets> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BetHolder holder, int position, @NonNull Bets model) {
        holder.textViewTitle.setText(model.getHome() + " vs. " + model.getAway());
        holder.textViewDescription.setText("Expires " + model.getDate_expires() + " | Favorite: " + model.getFavorite());
        holder.textViewPriority.setText("Odds: " + model.getOdds());
    }

    @NonNull
    @Override
    public BetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bet_item, viewGroup, false);
        return new BetHolder(v);
    }

    class BetHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDescription, textViewPriority;

        public BetHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }




}
