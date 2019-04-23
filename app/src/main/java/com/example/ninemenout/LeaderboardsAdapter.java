package com.example.ninemenout;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;



public class LeaderboardsAdapter extends FirestoreRecyclerAdapter<Users, LeaderboardsAdapter.LeaderboardsUserHolder> {

    private FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
    Context context;

    public LeaderboardsAdapter(@NonNull FirestoreRecyclerOptions<Users> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull LeaderboardsUserHolder holder, int position, @NonNull Users model) {
        // positions start at zero
        Integer pos = position + 1;
        Integer points = model.getPoints();
        TextView tv;
        holder.textViewRank.setText(pos.toString());
        holder.textViewUser.setText(model.getName());
        holder.textViewPoints.setText(points.toString());
        // update 'rank' text at top of page if current user is found
        if(model.getEmail().equals(fbUser.getEmail())) {
            tv = (TextView) ((Activity) this.context).findViewById(R.id.rankText);
            tv.setText("Rank: " + pos.toString());
        }
    }

    @NonNull
    @Override
    public LeaderboardsUserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_item, viewGroup, false);
        return new LeaderboardsUserHolder(v);
    }

    class LeaderboardsUserHolder extends RecyclerView.ViewHolder {
        TextView textViewRank, textViewUser, textViewPoints;

        public LeaderboardsUserHolder(@NonNull View itemView) {
            super(itemView);
            textViewRank = itemView.findViewById(R.id.text_view_rank);
            textViewUser = itemView.findViewById(R.id.text_view_user);
            textViewPoints = itemView.findViewById(R.id.text_view_points);

            // dont think I need an on click listener for this one
        };
    }

}

