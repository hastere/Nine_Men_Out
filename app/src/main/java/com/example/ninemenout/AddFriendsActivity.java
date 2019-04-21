package com.example.ninemenout;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFriendsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myauth;
    private static final String TAG = "AddFriendsActivity";
    public static final String FNAME = "Name";
    public static final String WAIT = "Status";
    private UserAdapter adapter;
    private CollectionReference budRef;
    private Button button;
     private Button no;
     public String buddy, sucker;
     private int userSearch = 0;
     private String checku;
     private ArrayList friendsArray = new ArrayList();
     private ArrayList UpdateFriendsPoints = new ArrayList();
     private FirebaseUser lonely;
     private String uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        button = (Button) findViewById(R.id.sendRequest);
        no = (Button) findViewById(R.id.reject);
        button.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
        myauth = FirebaseAuth.getInstance();
        lonely = myauth.getCurrentUser();
        uEmail = lonely.getEmail();
        Log.d(TAG, "The user email is " +uEmail);
        budRef = db.collection("users").document(uEmail).collection("friends");
        DocumentReference u = db.collection("users").document(uEmail);
        u.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    checku = (String) doc.get("name");
                }
            }
        });
        //gets all the users friends and puts them in array to be checked against when the user searches for friends
        budRef.orderBy("name",Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String f = (String) document.get("name");
                        friendsArray.add(f);
                        String up = (String) document.get("email");
                        UpdateFriendsPoints.add(up);
                    }
                    if(UpdateFriendsPoints.size() != 0)
                    {updatePoints();}
                }
            }
        });
        Log.d(TAG, "Length of Friends is " + friendsArray.size());
        Log.d(TAG, "Lengtht of UpdateFriendsPoints is " + UpdateFriendsPoints.size());

        setUpFriendsView();
    }

        //sets up recyclerView
        private void setUpFriendsView() {
            Query query = budRef.orderBy("name", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                    .setQuery(query, Users.class)
                    .build();

            adapter = new UserAdapter(options);
            RecyclerView recyclerView = findViewById(R.id.Friends_View);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                }

                @Override
                public void onSendBetClick(DocumentSnapshot documentSnapshot, int position) {
                    String newFriend = (String) documentSnapshot.get("name");
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
                                    sucker = (String) document.get("email");
                                }
                            }

                            openCreateBetsActivity();
                        }
                    });

                }
            });
        }

    public void openCreateBetsActivity() {
        Intent intent = new Intent(this, browseGames.class);
        //added flags to tell where the bet is coming from and who the user wanted to send a bet to
        intent.putExtra("FROM_ACTIVITY", "F");
        intent.putExtra("FRIEND",sucker);
        startActivity(intent);
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





    //This function allows the user to search for friends by username
    public void searchFriends(View view) throws InterruptedException {
        //gets user input
        EditText UserName = (EditText) findViewById(R.id.Friends_Username);
        String username = UserName.getText().toString();
        myauth = FirebaseAuth.getInstance();
        FirebaseUser lonely = myauth.getCurrentUser();
        String uEmail = lonely.getEmail();
        //gets the user's username
        DocumentReference u = db.collection("users").document(uEmail);
        u.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    //users username
                    String check  = (String) doc.get("name");
                    //checks to see if what is being searched for is a friend of the user
                    Log.d(TAG, "Size is " + friendsArray.size());
                    Log.d(TAG, "Update size is" + UpdateFriendsPoints.size());
                    for(int i = 0; i < friendsArray.size(); i++) {
                        String fCheck = (String) friendsArray.get(i);
                        if (fCheck.equals(username)) {
                            Context context = getApplicationContext();
                            CharSequence reusername = "They're already your friend!";
                            int duration_one = Toast.LENGTH_SHORT;
                            Toast toast_2 = Toast.makeText(context, reusername, duration_one);
                            toast_2.show();
                            return;
                        }
                    }
                        //checks to see if the user if searching themselves
                        if (check.equals(username)) {
                        Context context = getApplicationContext();
                        CharSequence reusername = "Can't Search for yourself!!!!";
                        int duration_one = Toast.LENGTH_SHORT;
                        Toast toast_2 = Toast.makeText(context, reusername, duration_one);
                        toast_2.show();
                        userSearch = 1;
                    }
                    //actually find the other user by username
                    else {
                        CollectionReference usersRef = db.collection("users");
                        //creates a query looking for that userinput
                        usersRef.whereEqualTo("name", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().isEmpty())
                                    {
                                        //notifies the user that the search failed
                                        Context context = getApplicationContext();
                                        CharSequence reusername = "No user with that username was found";
                                        int duration_one = Toast.LENGTH_SHORT;
                                        Toast toast_2 = Toast.makeText(context, reusername, duration_one);
                                        toast_2.show();
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //gets search result email for future querying
                                        //makes the send request and not send request buttons visible
                                        buddy = (String) document.get("email");
                                        button.setVisibility(View.VISIBLE);
                                        no.setVisibility(View.VISIBLE);
                                        //notifies user of succesful search
                                        Context contex = getApplicationContext();
                                        CharSequence hip = "Hooray you found a friend";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(contex, hip, duration);
                                        toast.show();

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }

                        });
                        userSearch = 0;
                        }
                } else {
                    Log.d(TAG, "Error getting username");
                }
            }
        });

        if(userSearch > 0)
        {
            return;
        }

        }

    public void sendRequest(View view){
        //gets user information to add to the request for the potential friend
        FirebaseUser user = myauth.getCurrentUser();
        String email = user.getEmail();
        //creates data for request document
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(FNAME, checku);
        //adds request document to potential new friend
        db.collection("users").document(buddy).collection("Requests").document(email).set(dataToSave)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //makes send request and not send invisible again
                        button.setVisibility(View.INVISIBLE);
                        no.setVisibility(View.INVISIBLE);
                        //sends toast notifying user request was sent
                        Context contex = getApplicationContext();
                        CharSequence hip = "Friend request sent";
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

    }

    //does not send request, and makes send and not send invisible again
    public void nah(View view){
        button.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
    }


    public void updatePoints(){
        CollectionReference userRef = db.collection("users");
        CollectionReference userFriends = db.collection("users").document(uEmail).collection("friends");

        for(int i = 0; i < UpdateFriendsPoints.size(); i++)
        {
            String oldActive = (String) UpdateFriendsPoints.get(i);
            DocumentReference update = userRef.document(oldActive);
            update.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if(task.isSuccessful()){
                       DocumentSnapshot fresh = task.getResult();
                       if (fresh.exists())
                       {
                           long freshpoints = (long) fresh.get("points");
                           DocumentReference old = userFriends.document(oldActive);
                           old.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   if(task.isSuccessful()){
                                    DocumentSnapshot outdated = task.getResult();
                                        if(outdated.exists())
                                        {
                                        old.update("points", freshpoints);
                                        }
                                   }
                               }
                           });
                       }
                   }
                }
            });
        }
    }
    }