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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewStatsActivity extends AppCompatActivity {

    private Button button;
    private static final String TAG = "ViewStatsActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String email = user.getEmail();

    private CollectionReference betRef = db.collection("users").document(email).collection("bets");
    private DocumentReference docRef = db.collection("users").document(email);

    private TextView points = findViewById(R.id.textViewPoints);
    private int pointsAmt;
    private TextView wins = findViewById(R.id.textViewWins);
    private int winsAmt = 0;
    private TextView losses = findViewById(R.id.textViewLosses);
    private int betAmt = 0;
    private int lossAmt = 0;
    private TextView winPer = findViewById(R.id.textViewWinPer);
    private float winPercent = 0;
    private TextView largeWin = findViewById(R.id.textViewLargeWin);
    private int lWin = 0;
    private TextView largeLoss = findViewById(R.id.textViewLargeLoss);
    private int lLoss = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stats);

        getPoints();
        getWins();
        getBets();

        lossAmt = betAmt - winsAmt;
        winPercent = ((float) winsAmt / (float) betAmt) * 100;

        button = findViewById(R.id.betHistoryBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBetHistoryActivity();
            }
        });

        points.setText("Points: " + pointsAmt);
        wins.setText("Wins: " + winsAmt);
        losses.setText("Losses: " + lossAmt);
        winPer.setText("Win Percentage: " + winPercent + "%");
        largeWin.setText("Largest Win: " + lWin);
        largeLoss.setText("Largest Loss: "+ lLoss);

    }

    private void getPoints() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        // update home page display to represent user data
                        pointsAmt = ((Long) document.get("points")).intValue();
                    } else {
                        Log.d("document error", "No such document");
                    }
                } else {
                    Log.d("task error", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getWins() {
        betRef.whereEqualTo("winner", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        winsAmt++;
                        if(((Long)document.get("amount")).intValue() > lWin) {
                            lWin = ((Long) document.get("amount")).intValue();
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getBets() {
        betRef.whereGreaterThan("winner", "").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    betAmt = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        betAmt++;
                        if(!(document.get("winner").equals(email))) {
                            if(((Long)document.get("amount")).intValue() > lLoss) {
                                lLoss = ((Long) document.get("amount")).intValue();
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void openBetHistoryActivity() {
        Intent intent = new Intent(this, BetHistoryActivity.class);
        startActivity(intent);
    }
}
