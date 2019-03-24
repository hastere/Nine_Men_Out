package com.example.ninemenout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BetsViewerActivity extends AppCompatActivity {

    TextView homeTeam, awayTeam, odds, points;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference betsRef = db.collection("bets");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bets_viewer);


        homeTeam = findViewById(R.id.homeText);
        awayTeam = findViewById(R.id.awayText);
        odds = findViewById(R.id.oddsText);
        points = findViewById(R.id.pointsText);


        Bundle b = this.getIntent().getExtras();
        if(b != null){
            String docID = b.getString("documentID");

            DocumentReference docRef = betsRef.document(docID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            homeTeam.setText((String) document.get("home"));
                            awayTeam.setText((String) document.get("away"));
                            odds.setText((String) document.get("odds"));
                            points.setText((String) document.get("amount"));
                        } else {
                            Log.d("googy", "No such document");
                        }
                    } else {
                        Log.d("googy", "get failed with ", task.getException());
                    }
                }
            });



        } else {
            // do nothing - this is an error
            Log.d("error", "bet viewer received no data");
        }


    }

}
