package com.example.ninemenout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BetsViewerActivity extends AppCompatActivity {

    TextView homeTeam, awayTeam, odds, points, unclaimedTeam, favorite;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference betsRef = db.collection("bets");
    private CollectionReference userRef = db.collection("users");
    private String documentID, unclaimed;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bets_viewer);

        // establish variable connections to various text and buttons
        homeTeam = findViewById(R.id.homeText);
        awayTeam = findViewById(R.id.awayText);
        odds = findViewById(R.id.oddsText);
        points = findViewById(R.id.pointsText);
        unclaimedTeam = findViewById(R.id.unclaimedTeamText);
        favorite = findViewById(R.id.favoriteText);

        // betsViewer should be passed the document ID as a string (reduces querying overall)
        Bundle b = this.getIntent().getExtras();
        // only query if the document ID exists
        if(b != null){
            String docID = b.getString("documentID");
            documentID = docID;
            DocumentReference docRef = betsRef.document(docID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            // set the text in the display to what was dynamically gained from the DB
                            // based on the passed (via the intent) document ID
                            // cast the LONG / NUMBER values first to avoid casting problems in line
                            String teamComparison = getOpenTeam(((String) document.get("betOnFavorite")), ((String) document.get("betOnUnderdog")));
                            String home = ((String) document.get("home"));
                            String away = ((String) document.get("away"));
                            String fav = ((String) document.get("favorite"));
                            if(teamComparison.equals("underdog")){
                                if(home.equals(fav)){
                                    teamComparison = "away";
                                }
                                else
                                    teamComparison = "home";
                            }
                            unclaimed = teamComparison;
                            String pointsConverter = String.valueOf(document.getLong("amount"));
                            homeTeam.setText((String) document.get("home"));
                            awayTeam.setText((String) document.get("away"));
                            odds.setText((String) document.get("odds"));
                            points.setText(pointsConverter);
                            unclaimedTeam.setText((String) document.get(teamComparison));
                            favorite.setText((String) document.get("favorite"));
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

    // returns which team has NOT been bet on based on user selection in the DB
    public String getOpenTeam(String favorite, String underdog){
        if(favorite.equals(""))
            return "favorite";
        else
            return "underdog";
    }

    // accepts the bet and returns to the home page
    public void betAccepted(View view){
        DocumentReference docRef = betsRef.document(documentID);
        DocumentReference emailRef = userRef.document(user.getEmail());
        CollectionReference userBetsRef = emailRef.collection("bets");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        // add copy to the users bets
                        Map<String, Object> userBet = new HashMap<String, Object>();
                        docRef.update("active", 1);
                        if(unclaimed.equals("favorite")) {
                            docRef.update("betOnFavorite", user.getEmail());
                            userBet.put("betOnFavorite", user.getEmail());
                            userBet.put("betOnUnderdog", ((String) document.get("betOnUnderdog")));
                            db.collection("users").document((String) document.get("betOnUnderdog"))
                                    .collection("bets").document(documentID).update(
                                    "active", 1,
                                    "betOnFavorite", user.getEmail()
                            );
                        } else {
                            docRef.update("betOnUnderdog", user.getEmail());
                            userBet.put("betOnUnderdog", user.getEmail());
                            userBet.put("betOnFavorite", ((String) document.get("betOnFavorite")));
                            db.collection("users").document((String) document.get("betOnFavorite"))
                                    .collection("bets").document(documentID).update(
                                    "active", 1,
                                    "betOnUnderdog", user.getEmail()
                            );
                        }

                        // active
                        userBet.put("active", 1);
                        // amount
                        userBet.put("amount", document.getLong("amount"));
                        // away
                        userBet.put("away", ((String) document.get("away")));
                        // date expirex
                        userBet.put("date_expires", ((String) document.get("date_expires")));
                        // favorite
                        userBet.put("favorite", ((String) document.get("favorite")));
                        // home
                        userBet.put("home", ((String) document.get("home")));
                        // odds
                        userBet.put("odds", ((String) document.get("odds")));
                        // type
                        userBet.put("type", ((String) document.get("type")));

                        userBetsRef.document(documentID).set(userBet);
//                        userBetsRef.add(userBet);

                        // update bet creators bets to have new data
                    } else {
                        Log.d("googy", "No such document");
                    }
                } else {
                    Log.d("googy", "get failed with ", task.getException());
                }
            }
        });
        Context context = getApplicationContext();
        CharSequence toastMessage = "Bet Accepted!";
        int toastDuration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastMessage, toastDuration);
        toast.show();

        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

}
