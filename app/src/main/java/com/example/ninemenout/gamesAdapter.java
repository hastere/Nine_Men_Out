package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
public class gamesAdapter extends FirestoreRecyclerAdapter<games, gamesAdapter.gameHolder> {

    public gamesAdapter(@NonNull FirestoreRecyclerOptions<games> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull gameHolder holder, int position, @NonNull games model) {
        holder.textViewTitle.setText(model.getHome_team() + " vs. " + model.getAway_team());
        holder.textViewDescription.setText("Over Under: " + Double.toString(model.getOver_under()) + " | Home Team Spread: " + Double.toString(model.getHome_spread()));
        holder.textViewPriority.setText("TBD" );
    }

    @NonNull
    @Override
    public gameHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_item, viewGroup, false);
        return new gameHolder(v);
    }

    class gameHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDescription, textViewPriority;

        public gameHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }

}
