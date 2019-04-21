package com.example.ninemenout;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class BetUtility {

    // copies document over to the bet collection of whoever accepted the bet
    public static Map<String, Object> newUserBet(DocumentSnapshot document, String betOnF, String betOnU){
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

}
