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
import android.widget.RadioButton;
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
import java.util.Date;
import java.text.SimpleDateFormat;

public class placeBetActivity extends AppCompatActivity {

    TextView gameTitle, gameTime, odds;
    RadioButton homeTeamOverButton, awayTeamUnderButton;

    String home, away, favorite, favoriteSpread, gameStart;
    String[] options = new String[2];
    double overUnder, homeSpread, awaySpread;

    private Button button;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference gamesRef = db.collection("games");
    private CollectionReference userCollectionRef = db.collection("users");
    private String documentID;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        options[0] = "spread";
        options[1] = "homeOver";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bet);
        // establish variable connections to various text and buttons
        gameTitle = findViewById(R.id.gameTitle);
        gameTime = findViewById(R.id.gameTime);
        odds = findViewById(R.id.odds);
        homeTeamOverButton = findViewById(R.id.radioHomeOver);
        awayTeamUnderButton = findViewById(R.id.radioAwayUnder);
        //when button pressed, create the bet
        button = findViewById(R.id.placeBet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBet(v);
            }
        });
        // game is passed in as string
        Bundle b = this.getIntent().getExtras();
        if(b != null){
            //bind the game values to class variables
            bindValues(b);
        } else {
            Log.d("error", "bet viewer received no data");
        }
    }
    // creates the bet and returns to the list of gamees
    public void createBet(View view){
        DocumentReference userRef = userCollectionRef.document(user.getEmail());
        CollectionReference userBetsRef = userRef.collection("bets");
        CollectionReference betsCollectionRef = db.collection("bets");
        //get bet value
        EditText betSize = findViewById(R.id.betSize);
        long betValue = Long.parseLong(betSize.getText().toString());
        //create new bet document
        final Map<String, Object> userBet = newBet(betValue);
        //check that user has enough points to bet
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        long points = (long) document.get("points");
                        long activePoints = 0;
                        if(document.contains("activePoints")) {
                         activePoints =   (long) document.get("activePoints");
                        }
                                //move points to active status
                        if((points - activePoints) >= betValue && (betValue > 0)) {
                            userRef.update("activePoints", (activePoints + betValue));
                            DocumentReference newBetRef = userBetsRef.document();
                            newBetRef.set(userBet);
                            betsCollectionRef.document(newBetRef.getId()).set(userBet);
                        }
                        else {
                            //error for a bet that is too big
                            Context context = getApplicationContext();
                            CharSequence toastMessage = "Not enough points! Try Again.";
                            int toastDuration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastMessage, toastDuration);
                            toast.show();
                        }
                    }
                }
            }
        });
        Context context = getApplicationContext();
        CharSequence toastMessage = "Bet Placed";
        int toastDuration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastMessage, toastDuration);
        toast.show();
        //redirect to homepage
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
    //alternates what the bet type is
    public void chooseBetType(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioSpread:
                if(checked) {
                    homeTeamOverButton.setText(home);
                    awayTeamUnderButton.setText(away);
                    options[0] = "spread";
                }
                break;
            case R.id.radioOverUnder:
                if(checked) {
                    homeTeamOverButton.setText("Over");
                    awayTeamUnderButton.setText("Under");
                    options[0] = "over under";
                }
                break;
        }
    }
    //alternates what the bet will be placed on
    public void chooseBetOn(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioHomeOver:
                if(checked)
                    options[1] = "homeOver";
                break;
            case R.id.radioAwayUnder:
                if(checked)
                    options[1] = "awayUnder";
                break;
        }
    }
    //generates a bet based off of class variables assigned on create
    public Map newBet(long betValue) {
        //create new bet document
        Map<String, Object> userBet = new HashMap<String, Object>();
        userBet.put("active", 0);
        userBet.put("amount", (int) betValue);
        userBet.put("away", away);
        userBet.put("date_expires", gameStart);
        userBet.put("favorite", favorite);
        userBet.put("home", home);
        userBet.put("odds", favoriteSpread);
        userBet.put("type", "spread");
        userBet.put("gameRef", documentID);
        //check options for overunder and who the bet is placed on
        if (options[0].equals("spread")) {
            userBet.put("type", "spread");
            if (options[1].equals("homeOver")) {
                if (home.equals(favorite)) {
                    userBet.put("betOnFavorite", user.getEmail());
                    userBet.put("betOnUnderdog", "");
                }
                else {
                    userBet.put("betOnFavorite", "");
                    userBet.put("betOnUnderdog", user.getEmail());
                }
            }
            else {
                if (away.equals(favorite)) {
                    userBet.put("betOnFavorite", user.getEmail());
                    userBet.put("betOnUnder", "");
                }
                else{
                    userBet.put("betOnUnderdog", user.getEmail());
                    userBet.put("betOnFavorite", "");
                }
            }
        }
        else {
            userBet.put("type", "over under");
            String ou = Double.toString(overUnder);
            userBet.put("odds", ou);
            if (options[1].equals("homeOver")) {
                userBet.put("betOnFavorite", user.getEmail());
                userBet.put("betOnUnderdog", "");
            } else {
                userBet.put("betOnUnderdog", user.getEmail());
                userBet.put("betOnFavorite", "");
            }
        }
    return userBet;
    }
    //binds game information to class variables for use when creating a bet
    public void bindValues(Bundle b) {
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
                        //determine favorite based off of spread value
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
                        homeTeamOverButton.setText(home);
                        awayTeamUnderButton.setText(away);
                    } else {
                        Log.d("oops", "No such document");
                    }
                } else {
                    Log.d("oops", "get failed with ", task.getException());
                }
            }
        });
    }
}
