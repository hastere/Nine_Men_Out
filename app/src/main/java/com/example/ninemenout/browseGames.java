package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class browseGames extends AppCompatActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference gameRef = db.collection("games");
    private String source;
    private gamesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);
        setUpRecyclerView();
        Intent mintent = getIntent();
        source =mintent.getStringExtra("FROM_ACTIVITY");

    }
    //gets all games, sorts by overunder
    //TODO replace overunder sort with game time sort
    private void setUpRecyclerView() {
        Query query = gameRef.orderBy("event_date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<games> options = new FirestoreRecyclerOptions.Builder<games>()
                .setQuery(query, games.class)
                .build();

        adapter = new gamesAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerSearch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new gamesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Bundle placeBet = new Bundle();
                placeBet.putString("documentID", id);
                Intent intent = new Intent(getApplicationContext(), placeBetActivity.class);
                intent.putExtras(placeBet);
                intent.putExtra("FROM_ACTIVITY", source);
                startActivity(intent);
            }
        });

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
