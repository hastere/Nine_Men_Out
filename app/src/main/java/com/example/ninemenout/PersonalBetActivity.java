package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PersonalBetActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference betRef;

    private BetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_bet);

        setUpSubCollection();

        setUpRecyclerView();
    }

    private void setUpSubCollection() {
        betRef = db.collection("users").document("TWVpc5J0UUspHnmYUhfq").collection("bets");
    }

    private void setUpRecyclerView() {
        Query query = betRef.orderBy("odds", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Bets> options = new FirestoreRecyclerOptions.Builder<Bets>()
                .setQuery(query, Bets.class)
                .build();

        adapter = new BetsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.personalBetsList);
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
}
