package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalBetsAdapter extends FirestoreRecyclerAdapter<Bets, PersonalBetsAdapter.BetHolder> {

    public PersonalBetsAdapter(@NonNull FirestoreRecyclerOptions<Bets> options) {super(options);}

    private String email = PersonalBetActivity.getEmail();

    @Override
    protected void onBindViewHolder(@NonNull PersonalBetsAdapter.BetHolder holder, int position, @NonNull Bets model) {
        holder.textViewTitle.setText(model.getHome() + " vs. " + model.getAway());
        holder.textViewDescription.setText("Expires " + model.getDate_expires() + "| Favorite: " + model.getFavorite() + "| Odds: " + model.getOdds());
        if(email == model.getBetOnFavorite()) {
            holder.textViewPriority.setText("Pick: " + model.getBetOnFavorite());
        }
        if(email == model.getBetOnUnderdog)
        holder.textViewPriority.setText("Pick: " + model.getBetOnFavorite());
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
