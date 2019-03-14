package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.TextWatcher;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static java.lang.Character.toUpperCase;

public class SearchBetsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference betRef = db.collection("bets");
    private BetsAdapter adapter;
    String[] arr = new String[3];
    String[] readHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bets);

        Bundle b = this.getIntent().getExtras();
        if(b != null)
            readHolder = b.getStringArray("terms");
        if(readHolder != null)
            setUpRecyclerV(readHolder);
        else {
            arr[0] = "Alabama";
            arr[1] = "spread";
            arr[2] = "odds";
            setUpRecyclerV(arr);
        }

    }

    private void setUpRecyclerV(String[] searchTerms) {
        String teamName = toCamelcase(searchTerms[0]);
        String betType = searchTerms[1];
        String sortType = searchTerms[2];

        Query query = betRef.whereEqualTo("active", 0)
                .whereEqualTo("type", betType)
                .whereEqualTo("home", teamName)
                .orderBy(sortType, Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Bets> options = new FirestoreRecyclerOptions.Builder<Bets>()
                .setQuery(query, Bets.class)
                .build();

        adapter = new BetsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerSearch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BetsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Toast.makeText(SearchBetsActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // for string compares to the database
    public String toCamelcase(String team){
        String temp = team;
        if(team == null)
            {
                return team;
            }

        temp = temp.toLowerCase();
        String ret = temp.substring(0, 1).toUpperCase() + temp.substring(1);
        return ret;
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
