package com.example.ninemenout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int pointDisplay = 0;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView textViewToChange = findViewById(R.id.pointTotal);
        // getting user information
        String name = user.getEmail();
        if(name != null){
            DocumentReference docRef = userRef.document(name);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Log.d("googy", "DocumentSnapshot data: " + document.getData());
                            pointDisplay = ((Long) document.get("points")).intValue();
                            textViewToChange.setText(((Integer) pointDisplay).toString());

                        } else {
                            Log.d("googy", "No such document");
                        }
                    } else {
                        Log.d("googy", "get failed with ", task.getException());
                    }
                }
            });
        }

        button =(Button) findViewById(R.id.profileButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfileActivity();
            }
        });
        button =(Button) findViewById(R.id.createBetsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateBetsActivity();
            }
        });
        button =(Button) findViewById(R.id.suggestedBetsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuggestedBetsActivity();
            }
        });
        button =(Button) findViewById(R.id.searchBetsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchBetsActivity();
            }
        });
        button =(Button) findViewById(R.id.leaderboardsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeaderboardActivity();
            }
        });
        getPointsHere();
    }

    public void getPointsHere(){
        Log.d("googy", ((Integer) pointDisplay).toString());
    }

    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void openCreateBetsActivity() {
        Intent intent = new Intent(this, CreateBetsActivity.class);
        startActivity(intent);
    }

    public void openSearchBetsActivity() {
        Intent intent = new Intent(this, SearchBetsBufferActivity.class);
        startActivity(intent);
    }

    public void openSuggestedBetsActivity() {
        Intent intent = new Intent(this, SuggestedBetsActivity.class);
        startActivity(intent);
    }

    public void openLeaderboardActivity() {
        Intent intent = new Intent(this,LeaderboardActivity.class);
        startActivity(intent);
    }

}
