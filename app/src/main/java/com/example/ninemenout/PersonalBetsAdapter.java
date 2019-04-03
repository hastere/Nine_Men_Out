package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PersonalBetsAdapter extends FirestoreRecyclerAdapter<Bets, PersonalBetsAdapter.BetHolder> {

    public PersonalBetsAdapter(@NonNull FirestoreRecyclerOptions<Bets> options) {super(options);}

    private String email = PersonalBetActivity.getEmail();

    @Override
    protected void onBindViewHolder(@NonNull PersonalBetsAdapter.BetHolder holder, int position, @NonNull Bets model) { //model functions come from Bets.java
        holder.textViewTitle.setText("" + model.getHome() + " vs. " + model.getAway());
        holder.textViewDescription.setText("Favorite: " + model.getFavorite() + " | Line: " + model.getOdds());
        holder.textViewAmount.setText("Amount: " + model.getAmount() + " pts");
        if (email.equals(model.getBetOnFavorite())) { //uses users email to determine what to display
            holder.textViewPriority.setText("Pick: " + model.getFavorite());
        } else if (email.equals(model.getBetOnUnderdog())) {
            if (model.getHome().equals(model.getFavorite())) {
                holder.textViewPriority.setText("Pick: " + model.getHome());
            } else if (model.getAway().equals(model.getFavorite())) {
                holder.textViewPriority.setText("Pick: " + model.getAway());
            }
        }
        holder.textViewExpiration.setText("Expiration Date: " + model.getDate_expires());
    }  

    @NonNull
    @Override
    public BetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personal_bet_item, viewGroup, false);
        return new BetHolder(v);
    }

    class BetHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDescription, textViewPriority, textViewExpiration, textViewAmount;

        public BetHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.betTitle);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.oddsText);
            textViewExpiration = itemView.findViewById(R.id.expirationDate);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }


}
