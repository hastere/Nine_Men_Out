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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PersonalBetActivity extends AppCompatActivity {

    private static final String TAG = "PersonalBetActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    static private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference betRef;

    private PersonalBetsAdapter adapter;

   static  String email = user.getEmail();

    static public String getEmail() { //for the personal bets adapter to determine what to show
        return email;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_bet);

        setUpSubCollections();

        setUpRecyclerView();
    }

    private void setUpSubCollections() { //sets up subcollection to display the documents in it
        betRef = db.collection("users").document(email).collection("bets");
    }

    private void setUpRecyclerView() {
        betRef.whereGreaterThanOrEqualTo("active", 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //gets all documents where active is above 0
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                if(task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) { //for each document, checks them against the bets collection to see if it is complete
                        if (document.exists()) {
                            DocumentReference doc2 = db.collection("users").document(email).collection("bets").document(document.getId());
                            DocumentReference doc1 = db.collection("bets").document(document.getId());
                            doc1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if(((Long) doc.get("active")).intValue() == -1) { //updates fields in users bets
                                            doc2.update("active", -1);
                                            doc2.update("winner", doc.get("winner"));
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.d("document error", "No such document");
                        }
                    }
                } else {
                    Log.d("task error", "get failed with ", task.getException());
                }
            }
        });

        Query query = betRef.whereGreaterThanOrEqualTo("active", 1).orderBy("active", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Bets> options = new FirestoreRecyclerOptions.Builder<Bets>()
                .setQuery(query, Bets.class)
                .build();

        adapter = new PersonalBetsAdapter(options);

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
