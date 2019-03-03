package com.example.ninemenout;

public class Bets {

    private String odds, user1, user2, team1, team2, date_expires;

    public Bets(){
        // needed for cardview and bet searches
    }

    public Bets(String odds, String user1, String team1, String team2, String date_expires) {
        this.odds = odds;
        this.user1 = user1;
        this.user2 = user2;
        this.team1 = team1;
        this.team2 = team2;
        this.date_expires = date_expires;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(String date_expires) {
        this.date_expires = date_expires;
    }
}
