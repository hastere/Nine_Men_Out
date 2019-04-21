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
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.sql.Types.NULL;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int pointDisplay = 0, activePointDisplay = 0, inactivePointDisplay = 0;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView textViewToChange = findViewById(R.id.pointTotal);
        TextView activePointText = findViewById(R.id.activePointDisplay);
        TextView inactivePointText = findViewById(R.id.inactivePointDisplay);
        // getting user information - user has to be stored in their own document by ID
        String name = user.getEmail();
        if(name != null){
            DocumentReference docRef = userRef.document(name);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            // update home page display to represent user data
                            pointDisplay = ((Long) document.get("points")).intValue();
                            if(document.contains("activePoints"))
                                {
                                    activePointDisplay = ((Long) document.get("activePoints")).intValue();
                                }
                            else {
                               activePointDisplay = 0;
                            }
                            inactivePointDisplay = pointDisplay - activePointDisplay;
                            textViewToChange.setText(((Integer) pointDisplay).toString());
                            activePointText.setText(((Integer) activePointDisplay).toString());
                            inactivePointText.setText(((Integer) inactivePointDisplay).toString());
                        } else {
                            Log.d("document error", "No such document");
                        }
                    } else {
                        Log.d("task error", "get failed with ", task.getException());
                    }
                }
            });
        }

        //// CONNECT BUTTONS TO FUNCTIONS THAT LINK TO OTHER PAGES ////

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
        button =(Button) findViewById(R.id.viewStatsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewStatsActivity();
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
    }

    //// FUNCTIONS TO CONTROL CHANGING TO A NEW ACTIVITY BY BUTTON PRESS ////

    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void openCreateBetsActivity() {
        Intent intent = new Intent(this, browseGames.class);
        intent.putExtra("FROM_ACTIVITY", "HP");
        startActivity(intent);
    }

    public void openSearchBetsActivity() {
        Intent intent = new Intent(this, SearchBetsBufferActivity.class);
        startActivity(intent);
    }

    public void openViewStatsActivity() {
        Intent intent = new Intent(this, ViewStatsActivity.class);
        startActivity(intent);
    }

    public void openLeaderboardActivity() {
        Intent intent = new Intent(this,LeaderboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
