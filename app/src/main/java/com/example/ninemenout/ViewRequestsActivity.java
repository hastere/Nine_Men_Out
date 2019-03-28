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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewRequestsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myauth;
    public static String TAG = "ViewRequestsActivity";
    private CollectionReference reqRef;

    private RequestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        myauth = FirebaseAuth.getInstance();
        FirebaseUser lonely = myauth.getCurrentUser();
        String uEmail = lonely.getEmail();
        Log.d(TAG, "The user email is " + uEmail);
        reqRef = db.collection("user").document(uEmail).collection("Requests");

        setUpRequestView();


    }


    private void setUpRequestView() {
        Query query = reqRef.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Requests> options = new FirestoreRecyclerOptions.Builder<Requests>()
                .setQuery(query, Requests.class)
                .build();

        adapter = new RequestAdapter(options);
        Log.d(TAG, "in SetUp Friends");
        RecyclerView recyclerView = findViewById(R.id.Friends_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
             documentSnapshot.get(position);

            }




    }
}