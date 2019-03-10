package com.example.ninemenout;

public class Bets {

    private String away, home, favorite, odds, type, user1, user2, date_expires;
    private int active, amount;


    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(String date_expires) {
        this.date_expires = date_expires;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Bets(String away, String home, String favorite, String odds, String type, String user1, String date_expires, int amount) {
        this.away = away;
        this.home = home;
        this.favorite = favorite;
        this.odds = odds;
        this.type = type;
        this.user1 = user1;
        this.date_expires = date_expires;
        this.active = 0;
        this.amount = amount;
    }

    public Bets(){
        // needed for cardview and bet searches
    }


}
