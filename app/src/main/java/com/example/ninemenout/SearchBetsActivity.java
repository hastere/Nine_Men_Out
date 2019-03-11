package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.TextWatcher;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchBetsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference betRef = db.collection("bets");
    private BetsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bets);

        Bundle b = this.getIntent().getExtras();
        String[] arr = b.getStringArray("terms");

        setUpRecyclerV(arr);


    }

    private void setUpRecyclerV(String[] searchTerms) {
        String teamName = searchTerms[0];
        String betType = searchTerms[1];

        Query query = betRef.whereEqualTo("active", 0)
                .whereEqualTo("type", betType)
                .whereEqualTo("home", teamName)
                .orderBy("amount", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Bets> options = new FirestoreRecyclerOptions.Builder<Bets>()
                .setQuery(query, Bets.class)
                .build();

        adapter = new BetsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerSearch);
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
