package com.example.ninemenout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BetHistoryActivity extends AppCompatActivity {

    private static final String TAG = "BetHistoryActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    static private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference betRef;

    private CompletedBetsAdapter adapter;

    static  String email = user.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_history);

        setUpSubCollection();

        setUpRecyclerView();
    }

    //sets up a recycler viewer that uses any document where there is a winner field
    private void setUpRecyclerView() {
        Query query = betRef.whereGreaterThan("winner", "").orderBy("winner", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Bets> options = new FirestoreRecyclerOptions.Builder<Bets>()
                .setQuery(query, Bets.class)
                .build();

        adapter = new CompletedBetsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.completedBetsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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

    private void setUpSubCollection() { //sets up subcollection to display the documents in it
        betRef = db.collection("users").document(email).collection("bets");
    }
}
