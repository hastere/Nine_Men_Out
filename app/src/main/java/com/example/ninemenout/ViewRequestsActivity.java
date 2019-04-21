package com.example.ninemenout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.view.View;

import java.util.HashMap;
import java.util.Map;


public class ViewRequestsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myauth;
    private static final String TAG = "ViewRequestActivity";
    public static final String FNAME = "Name";
    public static final String WAIT = "Status";
    private RequestAdapter adapter;
    private CollectionReference reqRef;
    String uEmail, uName;
    long uPoints;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        myauth = FirebaseAuth.getInstance();
        FirebaseUser lonely = myauth.getCurrentUser();
        uEmail = lonely.getEmail();
        reqRef = db.collection("users").document(uEmail).collection("Requests");
        //gets the users points and username
        DocumentReference u = db.collection("users").document(uEmail);
         Task<DocumentSnapshot> doccy = u.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task)
             {
             if(task.isSuccessful())
                {
                 DocumentSnapshot doccy = task.getResult();
                 uName = (String) doccy.get("name");
                 uPoints = (long) doccy.get("points");
                }
             }
         }
         );
        setUpRequestView();
    }

    private void setUpRequestView() {
        Query query = reqRef.orderBy("Name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Requests> options = new FirestoreRecyclerOptions.Builder<Requests>()
                .setQuery(query, Requests.class)
                .build();

        adapter = new RequestAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.Request_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
            }

            @Override
            public void onAcceptClick(DocumentSnapshot documentSnapshot, int position) {
                String newFriend = (String) documentSnapshot.get("Name");
                CollectionReference usersRef = db.collection("users");
                //creates a query looking for that userinput
                usersRef.whereEqualTo("name", newFriend).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                //notifies the user that the search failed
                                Context context = getApplicationContext();
                                CharSequence reusername = "Sorry that user no longer exists!";
                                int duration_one = Toast.LENGTH_SHORT;
                                Toast toast_2 = Toast.makeText(context, reusername, duration_one);
                                toast_2.show();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //gets the requester's information
                                //makes user and requester friends
                                String email = (String) document.get("email");
                                String Fname = (String) document.get("name");
                                Long points = (Long) document.get("points");
                                Map<String, Object> dataToSave = new HashMap<String, Object>();
                                dataToSave.put("email", email);
                                dataToSave.put("name", Fname);
                                dataToSave.put("points", points);
                                db.collection("users").document(uEmail).collection("friends").document(email).set(dataToSave)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //sends toast notifying user request was sent
                                                Context contex = getApplicationContext();
                                                CharSequence hip = "Friend Accepted";
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(contex, hip, duration);
                                                toast.show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "it failed");
                                        Context context = getApplicationContext();
                                        CharSequence reusername = "Request Failed";
                                        int duration_one = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, reusername, duration_one);
                                        toast.show();
                                    }
                                });
                                ;
                                Map<String, Object> dataToSave2 = new HashMap<String, Object>();
                                dataToSave2.put("email", uEmail);
                                dataToSave2.put("name", uName);
                                dataToSave2.put("points", uPoints);
                                db.collection("users").document(email).collection("friends").document(uEmail).set(dataToSave2);
                                db.collection("users").document(uEmail).collection("Requests").document(email).delete();


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });
            }

            @Override
            public void onDeclineClick(DocumentSnapshot documentSnapshot, int position) {
                String newFriend = (String) documentSnapshot.get("Name");
                CollectionReference usersRef = db.collection("users");
                //creates a query looking for that userinput
                usersRef.whereEqualTo("name", newFriend).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                //notifies the user that the search failed
                                Context context = getApplicationContext();
                                CharSequence reusername = "Sorry that user no longer exists!";
                                int duration_one = Toast.LENGTH_SHORT;
                                Toast toast_2 = Toast.makeText(context, reusername, duration_one);
                                toast_2.show();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //deletes the request from the db, notifies the user they rejected it
                                String email = (String) document.get("email");
                                Context contex = getApplicationContext();
                                CharSequence hip = "Friendship Rejected!!!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(contex, hip, duration);
                                toast.show();
                                db.collection("users").document(uEmail).collection("Requests").document(email).delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }

            ;
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


    public void seeBets(View view)
    {
        Intent intent = new Intent(this, FriendBetsViewerActivity.class);
        startActivity(intent);
    }

    public void userProfile(View view)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
