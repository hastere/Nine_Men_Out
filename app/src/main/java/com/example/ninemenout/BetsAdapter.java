package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BetsAdapter extends FirestoreRecyclerAdapter<Bets, BetsAdapter.BetHolder> {

    private OnItemClickListener listener;
    public BetsAdapter(@NonNull FirestoreRecyclerOptions<Bets> options) {
        super(options);
    }

    // overrides the view holder to display each position in the specific way
    @Override
    protected void onBindViewHolder(@NonNull BetHolder holder, int position, @NonNull Bets model) {
        String untaken = "Alabama";

        if(model.getBetOnFavorite().equals(""))
            untaken = model.getFavorite();
        else {
            if(model.getHome().equals(model.getFavorite())){
                untaken = model.getAway();
            } else {
                untaken = model.getHome();
            }
        }
        holder.textViewTitle.setText(model.getHome() + " vs. " + model.getAway());
        holder.textViewDescription.setText("Favorite: " + model.getFavorite() + " | Unclaimed: " + untaken);
        holder.textViewPriority.setText("Odds: " + model.getOdds());
    }

    // overrides the view holder creation to use the bet item layout
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
            textViewTitle = itemView.findViewById(R.id.betTitle);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.oddsText);

            // so users can select bets to view them / accept them
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
