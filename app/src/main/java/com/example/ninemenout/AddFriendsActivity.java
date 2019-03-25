package com.example.ninemenout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

public class AddFriendsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myauth;
    private static final String TAG = "AddFriendsActivity";
    public static final String FNAME = "Name";
    public static final String WAIT = "Status";
 //   private UserAdapter adapter;
   // FirebaseUser lonely = myauth.getCurrentUser();
   // String uEmail = lonely.getEmail();
  //  private CollectionReference budRef = db.collection("user").document(uEmail).collection("friends");
     private Button button;
     private Button no;
     public String buddy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        button = (Button) findViewById(R.id.sendRequest);
        no = (Button) findViewById(R.id.reject);
        button.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
        myauth = FirebaseAuth.getInstance();
        /*Query check = budRef.orderBy("name");
        check.toEqual(NULL);*/
        //setUpFriendsView();
    }
        /*private void setUpFriendsView() {
            Query query = budRef.orderBy("name", Query.Direction.DESCENDING);


                FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();

                adapter = new UserAdapter(options);

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
*/




    //@Override
    public void searchFriends(View view) {
        EditText UserName = (EditText) findViewById(R.id.Friends_Username);
        String username = UserName.getText().toString();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("name", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                            if(task.getResult().isEmpty())
                            {
                                Context context = getApplicationContext();
                                CharSequence reusername = "No user with that username was foung";
                                int duration_one = Toast.LENGTH_SHORT;
                                Toast toast_2 = Toast.makeText(context, reusername, duration_one);
                                toast_2.show();
                            }
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            buddy = (String) document.get("email");
                            button.setVisibility(View.VISIBLE);
                            no.setVisibility(View.VISIBLE);
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
        }

    public void sendRequest(View view){
        FirebaseUser user = myauth.getCurrentUser();
        String email = user.getEmail();
        DocumentReference u = db.collection("user").document(email);
        String name =  "jjgospodarek";//u.get().getResult().getString("name");
        String status = "Pending";
        Log.d(TAG, "the user is " +user);
        Log.d(TAG, "the friend is " +buddy);
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(FNAME, name);
        dataToSave.put(WAIT, false);
        db.collection("users").document(buddy).collection("Requests").document(name).set(dataToSave)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        button.setVisibility(View.INVISIBLE);
                        no.setVisibility(View.INVISIBLE);
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
        }); ;

    }

    public void nah(View view){
        button.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
    }

    }


