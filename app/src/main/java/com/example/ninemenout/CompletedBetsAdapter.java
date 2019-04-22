package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CompletedBetsAdapter extends FirestoreRecyclerAdapter<Bets, CompletedBetsAdapter.BetHolder> {

    public CompletedBetsAdapter(@NonNull FirestoreRecyclerOptions<Bets> options) {super(options);}

    private String email = PersonalBetActivity.getEmail();

    @Override
    protected void onBindViewHolder(@NonNull CompletedBetsAdapter.BetHolder holder, int position, @NonNull Bets model) { //model functions come from Bets.java
        holder.textViewTitle.setText("" + model.getHome() + " vs. " + model.getAway()); //displays game
        if (email.equals(model.getWinner())) { //if the user is the winner of the bet and the amount
            holder.textViewResult.setText("Result: Won");
            holder.textViewAmount.setText("Reward: " + model.getAmount() + " pts");
        } else {
            holder.textViewResult.setText("Result: Lost");
            holder.textViewAmount.setText("Cost: " + model.getAmount() + " pts");
        }

        if (email.equals(model.getBetOnFavorite())) { //displays what user picked
            if(model.getType().equals("over under")) {
                holder.textViewPriority.setText("Pick: Over");
            }
            else {
                holder.textViewPriority.setText("Pick: " + model.getFavorite());
            }
        } else if (email.equals(model.getBetOnUnderdog())) {
            if(model.getType().equals("over under")) {
                holder.textViewPriority.setText("Pick: Under");
            }
            else {
                if (model.getHome().equals(model.getFavorite())) {
                    holder.textViewPriority.setText("Pick: " + model.getAway());
                } else if (model.getAway().equals(model.getFavorite())) {
                    holder.textViewPriority.setText("Pick: " + model.getHome());
                }
            }
        }
    }

    @NonNull
    @Override
    public BetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.completed_bet_item, viewGroup, false);
        return new BetHolder(v);
    }

    class BetHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewResult, textViewPriority, textViewAmount;

        public BetHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.betTitle);
            textViewResult = itemView.findViewById(R.id.textViewResult);
            textViewPriority = itemView.findViewById(R.id.oddsText);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }


}
