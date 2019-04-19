package com.example.ninemenout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.checkerframework.checker.nullness.compatqual.NonNullType;

import java.util.HashMap;
import java.util.Map;

public class AcceptFriendBet extends AppCompatActivity {

    TextView homeTeam, awayTeam, odds, points, unclaimedTeam, favorite, betTypeText, friend;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference betsRef = db.collection("bets");
    private CollectionReference userRef = db.collection("users");
    private String documentID, unclaimed, unclaimedField;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    boolean amountTooHigh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_friend_bet);

        // establish variable connections to various text and buttons
        homeTeam = findViewById(R.id.homeText);
        awayTeam = findViewById(R.id.awayText);
        odds = findViewById(R.id.oddsText);
        points = findViewById(R.id.pointsText);
        unclaimedTeam = findViewById(R.id.unclaimedTeamText);
        favorite = findViewById(R.id.favoriteText);
        betTypeText = findViewById(R.id.betTypeText);
        friend = findViewById(R.id.fromText);

        // betsViewer should be passed the document ID as a string (reduces overall querying needed)
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
                            unclaimed = getOpenTeam(((String) document.get("betOnFavorite")));
                            unclaimedField = getOpenTeamField(unclaimed,
                                    ((String) document.get("favorite")),
                                    ((String) document.get("home")));
                            String pointsConverter = String.valueOf(document.getLong("amount"));
                            homeTeam.setText((String) document.get("home"));
                            awayTeam.setText((String) document.get("away"));
                            odds.setText((String) document.get("odds"));
                            points.setText(pointsConverter);
                            friend.setText((String) document.get("from"));
                            String betType = ((String) document.get("type"));
                            if(betType.equals("over under")) {
                                betTypeText.setText("Over Under");
                                if (unclaimed.equals("favorite"))
                                    unclaimedTeam.setText("Over");
                                else
                                    unclaimedTeam.setText("Under");
                            } else {
                                unclaimedTeam.setText((String) document.get(unclaimedField));
                            }
                            favorite.setText((String) document.get("favorite"));
                        } else {
                            Log.d("error", "No such document");
                        }
                    } else {
                        Log.d("error", "get failed with ", task.getException());
                    }
                }
            });

        } else {
            // do nothing - this is an error
            Log.d("error", "bet viewer received no data");
        }


    }

    // accepts the bet and returns to the home page
    public void friendBetAccepted(View view){
        amountTooHigh = false;
        DocumentReference docRef = betsRef.document(documentID);
        DocumentReference emailRef = userRef.document(user.getEmail());
        CollectionReference userBetsRef = emailRef.collection("bets");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        int pointChanger = document.getLong("amount").intValue();
                        emailRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                if (userTask.isSuccessful()) {
                                    DocumentSnapshot userDocument = userTask.getResult();
                                    int userActive = 0;
                                    if (userDocument.exists()) {
                                        if(userDocument.contains("activePoints")) {
                                            userActive = userDocument.getLong("activePoints").intValue();
                                        }

                                        int userInactive = userDocument.getLong("points").intValue() - userActive;
                                        int sumTotal = userActive + pointChanger;
                                        if(pointChanger > userInactive){
                                            insufficientToast();
                                            amountTooHigh = true;
                                            return;
                                        }

                                        emailRef.update("activePoints", sumTotal);

                                        if(!amountTooHigh) {
                                            String betOnF, betOnU;
                                            docRef.update("active", 1);
                                            if (unclaimed.equals("favorite")) {
                                                docRef.update("betOnFavorite", user.getEmail());
                                                betOnF = user.getEmail();
                                                betOnU = ((String) document.get("betOnUnderdog"));
                                                db.collection("users").document(betOnU)
                                                        .collection("bets").document(documentID).update(
                                                        "active", 1,
                                                        "betOnFavorite", user.getEmail()
                                                );
                                            } else {
                                                docRef.update("betOnUnderdog", user.getEmail());
                                                betOnU = user.getEmail();
                                                betOnF =  ((String) document.get("betOnFavorite"));
                                                db.collection("users").document(betOnF)
                                                        .collection("bets").document(documentID).update(
                                                        "active", 1,
                                                        "betOnUnderdog", user.getEmail()
                                                );
                                            }
                                            userBetsRef.document(documentID).set(newUserBet(document, betOnF, betOnU));
                                            userRef.document(user.getEmail()).collection("betReq").document(documentID).delete();
                                            acceptedToast();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        // user moved back to FriendBetsViewer page
        Intent intent = new Intent(this, FriendBetsViewerActivity.class);
        startActivity(intent);
    }


   public void friendBetRejected(View view){
        DocumentReference docRef = betsRef.document(documentID);
        String  email = user.getEmail();
       docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task){
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    String fEmail;
                    if(unclaimed.equals("favorite"))
                    { fEmail = (String) document.get("betOnUnderdog"); }
                    else
                    { fEmail = (String) document.get("betOnFavorite"); }
                    betsRef.document(documentID).delete();
                    userRef.document(email).collection("betReq").document(documentID).delete();
                    userRef.document(fEmail).collection("bets").document(documentID).delete();
                }
            }

           }
       });
       // user moved back to FriendBetsViewer page
       Intent intent = new Intent(this, FriendBetsViewerActivity.class);
       startActivity(intent);
   }



    // returns which team has NOT been bet on based on user selection in the DB
    public static String getOpenTeam(String favorite){
        if(favorite.equals(""))
            return "favorite";
        else
            return "underdog";
    }

    // returns which team (HOME or AWAY) is the underdog or favorite, whichever is in the 'unclaimed' String
    public static String getOpenTeamField(String unclaimed, String favorite, String home) {
        if (unclaimed.equals("underdog")) {
            if (home.equals(favorite)) {
                return "away";
            } else
                return "home";
        } else {
            if (home.equals(favorite)) {
                return "home";
            } else
                return "away";
        }
    }

    // copies document over to the bet collection of whoever accepted the bet
    public Map<String, Object> newUserBet(DocumentSnapshot document, String betOnF, String betOnU){
        Map<String, Object> userBet = new HashMap<String, Object>();
        userBet.put("active", 1);
        userBet.put("amount", document.getLong("amount"));
        userBet.put("away", ((String) document.get("away")));
        userBet.put("date_expires", ((String) document.get("date_expires")));
        userBet.put("favorite", ((String) document.get("favorite")));
        userBet.put("home", ((String) document.get("home")));
        userBet.put("odds", ((String) document.get("odds")));
        userBet.put("type", ((String) document.get("type")));
        userBet.put("betOnFavorite", betOnF);
        userBet.put("betOnUnderdog", betOnU);
        return userBet;
    }

    //// YOAST CONTROLLER FUNCTIONS ////

    // toast when accept is denied
    public void insufficientToast(){
        Context context = getApplicationContext();
        CharSequence toastMessage = "Insufficient Points to Accept Bet";
        int toastDuration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastMessage, toastDuration);
        toast.show();
    }

    // toast when accept is accepted
    public void acceptedToast(){
        Context context = getApplicationContext();
        CharSequence toastMessage = "Bet Accepted!";
        int toastDuration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastMessage, toastDuration);
        toast.show();
    }

}
