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
    Long overUnder, homeSpread, awaySpread;
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
                            overUnder = ((long) document.get("over_under"));
                            homeSpread = ((long) document.get("home_spread"));
                            awaySpread = ((long) document.get("away_spread"));

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
                            odds.setText(favorite + " by " + favoriteSpread + "; Over/Under at " + Long.toString(overUnder));
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

        DocumentReference docRef = gamesRef.document(documentID);
        DocumentReference userRef = userCollectionRef.document(user.getEmail());

        CollectionReference userBetsRef = userRef.collection("bets");
        CollectionReference betsCollectionRef = db.collection("bets");

        EditText betSize = (EditText) findViewById(R.id.betSize);
        //EditText betType = (EditText) findViewById(R.id.betType);
        Long betValue = Long.parseLong(betSize.getText().toString());


        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        long points = (long) document.get("points");
                        if(points >= betValue && (betValue != null || betValue > 0)) {
                            Map<String, Object> userBet = new HashMap<String, Object>();
                            // active
                            userBet.put("active", 0);
                            // amount
                            userBet.put("amount", betValue);
                            // away
                            userBet.put("away", away);
                            // date expires
                            userBet.put("date_expires", gameTime);
                            // favorite
                            userBet.put("favorite", favorite);
                            // home
                            userBet.put("home", home);
                            // odds
                            userBet.put("odds", favoriteSpread);
                            // type
                            userBet.put("type", "spread");
                            //gameRefId
                            userBet.put("gameRef", documentID);

                            userBetsRef.add(userBet);
                            betsCollectionRef.add(userBet);
                        }
                        else {
                            Log.d("googy", "no sufficient points");
                            Context context = getApplicationContext();
                            CharSequence toastMessage = "Not enough points! Try Again.";
                            int toastDuration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastMessage, toastDuration);
                            toast.show();
                        }
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
