package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");
    private LeaderboardsAdapter adapter, adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setUpGlobalRecyclerView();
    }

    private void setUpGlobalRecyclerView() {
        // list users from biggest point total to smallest
        Query query = userRef.orderBy("points", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        adapter = new LeaderboardsAdapter(options, this);

        RecyclerView recyclerView = findViewById(R.id.globalLeaderboardRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setUpFriendsRecyclerView() {
        // list users from biggest point total to smallest
        Query query = userRef.orderBy("points", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        adapter1 = new LeaderboardsAdapter(options, this);

        RecyclerView recyclerView = findViewById(R.id.friendsLeaderboardRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter1);
    }

    public void toggleGlobal(View view){
        setContentView(R.layout.activity_leaderboard);
        setUpGlobalRecyclerView();
        adapter.startListening();
    }

    public void toggleFriends(View view){
        setContentView(R.layout.activity_leaderboard_friends);
        setUpFriendsRecyclerView();
        adapter1.startListening();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



}
