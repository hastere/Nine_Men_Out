package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference userRef = db.collection("users");
    private CollectionReference friendsRef = userRef
            .document(fbUser.getEmail())
            .collection("friends");
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

        friendsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task){
               if(task.isSuccessful()){
                   for(DocumentSnapshot document : task.getResult()){
                       String friendEmail = (String) document.get("email");
                       DocumentReference doc1 = userRef.document(friendEmail);
                       doc1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               DocumentSnapshot doc2 = task.getResult();
                               if(doc2.exists()){
                                   Integer pts = doc2.getLong("points").intValue();
                                   Map<String, Object> friendEditor = new HashMap<String, Object>();
                                   friendEditor.put("points", pts);
                                   friendEditor.put("name", document.get("name"));
                                   friendEditor.put("email", friendEmail);
                                   friendsRef.document(friendEmail).set(friendEditor);
                               }
                           }
                       });
                   }
               }
           }
        });

        Query query = friendsRef.orderBy("points", Query.Direction.DESCENDING);

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
