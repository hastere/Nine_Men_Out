package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FriendsAdapter extends FirestoreRecyclerAdapter<Friends, FriendsAdpater.FriendHolder> {
    private OnItemClickListener listener;
    public FriendsAdapter(@NonNull FirestoreRecyclerOptions<Friends> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder holder, int position, @NonNull Bets model) {
        holder.textViewTitle.setText(model.getHome() + " vs. " + model.getAway());
        holder.textViewDescription.setText("Expires " + model.getDate_expires() + " | Favorite: " + model.getFavorite());
        holder.textViewPriority.setText("Odds: " + model.getOdds());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bet_item, viewGroup, false);
        return new FriendHolder(v);
    }

    class FriendHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDescription, textViewPriority;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

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
