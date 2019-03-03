package com.example.ninemenout;

public class Bets {

    private String odds, user1, user2, favorite, underdog, date_expires;

    public Bets(){
        // needed for cardview and bet searches
    }

    public Bets(String odds, String user1, String favorite, String underdog, String date_expires) {
        this.odds = odds;
        this.user1 = user1;
        this.favorite = favorite;
        this.underdog = underdog;
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

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getUnderdog() {
        return underdog;
    }

    public void setUnderdog(String underdog) {
        this.underdog = underdog;
    }

    public String getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(String date_expires) {
        this.date_expires = date_expires;
    }
}
