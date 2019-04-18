package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

public class FriendBetsViewerActivity extends AppCompatActivity {
    private static final String TAG = "PersonalBetActivity";
    private FriendsBetAdapater adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    static private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference betRef;
    private FirebaseUser acceptor;
    private FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_bets_viewer);
        myAuth = FirebaseAuth.getInstance();
        acceptor = myAuth.getCurrentUser();
        betRef = db.collection("users").document(acceptor.getEmail()).collection("betReq");
        setFriendBetView();
    }



    public void setFriendBetView(){
        Query query = betRef.orderBy("amount", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Bets> options = new FirestoreRecyclerOptions.Builder<Bets>()
                .setQuery(query, Bets.class)
                .build();

        adapter = new FriendsBetAdapater(options);
        RecyclerView recyclerView = findViewById(R.id.Friend_Bets);
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


    public void profilePage(View view)
    {
        Intent intent = new Intent(this,UserProfileActivity.class);
        startActivity(intent);
    }


    public void seeFriends(View view){
        Intent intent = new Intent(this, ViewRequestsActivity.class);
        startActivity(intent);
    }
}
