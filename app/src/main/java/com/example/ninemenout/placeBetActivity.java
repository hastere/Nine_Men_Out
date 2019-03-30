package com.example.ninemenout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class placeBetActivity extends AppCompatActivity {

    TextView gameTitle, gameTime, odds;
    String home, away, gameStart, favorite, favoriteSpread;
    double overUnder, homeSpread, awaySpread;

    private Button button;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference gamesRef = db.collection("games");
    private CollectionReference userCollectionRef = db.collection("users");
    private String documentID;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bet);

        // establish variable connections to various text and buttons
        gameTitle = findViewById(R.id.gameTitle);
        gameTime = findViewById(R.id.gameTime);
        odds = findViewById(R.id.odds);


        button =(Button) findViewById(R.id.placeBet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBet(v);
            }
        });
        // betsViewer should be passed the document ID as a string (reduces querying overall)
        Bundle b = this.getIntent().getExtras();
        // only query if the document ID exists
        if(b != null){
            String docID = b.getString("documentID");
            documentID = docID;
            DocumentReference docRef = gamesRef.document(docID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){

                            home = ((String) document.get("home_team"));
                            away = ((String) document.get("away_team"));
                            gameStart = ((String) document.get("event_date"));
                            favorite = "";
                            favoriteSpread = "";
                            overUnder = document.getDouble("over_under");
                            homeSpread =  document.getDouble("home_spread");
                            awaySpread =  document.getDouble("away_spread");

                            if (homeSpread < 0) {
                                favorite = home;
                                favoriteSpread = Double.toString(homeSpread);
                            }
                            else {
                                favorite = away;
                                favoriteSpread = Double.toString(awaySpread);
                            }

                            gameTitle.setText((home + " vs. " + away));
                            gameTime.setText(gameStart);
                            odds.setText(favorite + " by " + favoriteSpread + "; Over/Under at " + Double.toString(overUnder));
                        } else {
                            Log.d("oops", "No such document");
                        }
                    } else {
                        Log.d("oops", "get failed with ", task.getException());
                    }
                }
            });

        } else {
            // do nothing - this is an error
            Log.d("error", "bet viewer received no data");
        }


    }

    // accepts the bet and returns to the home page
    public void createBet(View view){

        String userCollectionBetID;

        DocumentReference docRef = gamesRef.document(documentID);
        DocumentReference userRef = userCollectionRef.document(user.getEmail());

        CollectionReference userBetsRef = userRef.collection("bets");
        CollectionReference betsCollectionRef = db.collection("bets");

        EditText betSize = (EditText) findViewById(R.id.betSize);
        //EditText betType = (EditText) findViewById(R.id.betType);
        long betValue = Long.parseLong(betSize.getText().toString());

        Map<String, Object> userBet = new HashMap<String, Object>();
        // active
        userBet.put("active", (int) 0);
        // amount
        userBet.put("amount", (int) betValue);
        // away
        userBet.put("away", away);
        // date expires
        userBet.put("date_expires", gameStart);
        // favorite
        userBet.put("favorite", favorite);
        // home
        userBet.put("home", home);
        // odds
        userBet.put("odds", favoriteSpread);
        // type
        userBet.put("type", "spread");
        //gameRefId
        //userBet.put("gameRef", documentID);

        userBet.put("betOnFavorite", user.getEmail().toString());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("UserCheck", "Got User");
                        long points = (long) document.get("points");
                        long activePoints = (long) document.get("activePoints");
                        if((points - activePoints) >= betValue && (betValue > 0)) {
                            Log.d("BalanceCheck", "Got User");
                            userRef.update("activePoints", (activePoints + betValue));

                        }
                        else {
                            Log.d("BalanceCheck", "no sufficient points");
                            Context context = getApplicationContext();
                            CharSequence toastMessage = "Not enough points! Try Again.";
                            int toastDuration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastMessage, toastDuration);
                            toast.show();
                        }
                    }
                    else {
                        Log.d("UserCheck", "No such document");
                    }
                }
            }
        });



        userBetsRef.add(userBet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) { Log.d("heyo", "DocumentSnapshot written with ID: " + documentReference.getId()); }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("userBetsCol", "Error adding document", e);
            }
        });


        betsCollectionRef.add(userBet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("heyo", "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("BetsColl", "Error adding document", e);
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
