package com.example.ninemenout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.view.View;

public class AddFriendsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth myauth;
    private static final String TAG = "AddFriendsActivity";
 //   private UserAdapter adapter;
   // FirebaseUser lonely = myauth.getCurrentUser();
   // String uEmail = lonely.getEmail();
  //  private CollectionReference budRef = db.collection("user").document(uEmail).collection("friends");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

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
       // QuerySnapshot query = usersRef.whereEqualTo("name", username).get().getResult();
        /*if(query.isEmpty())
        {
        Context context = getApplicationContext();
        CharSequence reusername = "No user with that username was foung";
        int duration_one = Toast.LENGTH_SHORT;
        Toast toast_2 = Toast.makeText(context, reusername, duration_one);
        toast_2.show();

        }
    else
        {

*/            usersRef.whereEqualTo("name", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    }

//}
