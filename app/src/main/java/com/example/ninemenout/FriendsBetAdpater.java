package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FriendsBetAdapater extends FirestoreRecyclerAdapter<Bets, FriendsBetAdapater.FriendBetHolder>
{


    private FriendsBetAdapater.OnItemClickListener listener;
    public FriendsBetAdapater(@NonNull FirestoreRecyclerOptions<Bets> options) {
        super(options);
    }

        // overrides the view holder to display each position in the specific way
        @Override
        protected void onBindViewHolder(@NonNull FriendBetHolder holder, int position, @NonNull Bets model) {
        String untaken = "Alabama";
        boolean ouFlag = false;
        if(model.getType().equals("over under"))
            ouFlag = true;
        if(model.getBetOnFavorite().equals("")) {
            if(ouFlag)
                untaken = "Over";
            else
                untaken = model.getFavorite();

        } else {
            if(ouFlag)
                untaken = "Under";
            else
            if(model.getHome().equals(model.getFavorite())){
                untaken = model.getAway();
            } else {
                untaken = model.getHome();
            }
        }
        holder.textViewTitle.setText(model.getHome() + " vs." + "\n" + model.getAway());
        holder.textViewDescription.setText("Favorite: " + model.getFavorite() + "\n" + "Unclaimed: " + untaken + "\n" + "Type: " + model.getType());
        holder.textViewPriority.setText(model.getOdds() + " / " + ((Integer) model.getAmount()).toString());
    }

        // overrides the view holder creation to use the bet item layout
        @NonNull
        @Override
        public FriendsBetAdapater.FriendBetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_bet_item, viewGroup, false);
        return new FriendsBetAdapater.FriendBetHolder(v);
    }

        class FriendBetHolder extends RecyclerView.ViewHolder {

            TextView textViewTitle, textViewDescription, textViewPriority;

            public FriendBetHolder(@NonNull View itemView) {
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

        public void setOnItemClickListener(FriendsBetAdapater.OnItemClickListener listener){
        this.listener = listener;
    }
}
