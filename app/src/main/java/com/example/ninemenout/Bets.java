package com.example.ninemenout;

public class Bets {

    private String away, home, favorite, odds, type, betOnFavorite, betOnUnderdog, date_expires;
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

    public String getBetOnFavorite() {
        return betOnFavorite;
    }

    public void setBetOnFavorite(String betOnFavorite) {
        this.betOnFavorite = betOnFavorite;
    }

    public String getBetOnUnderdog() {
        return betOnUnderdog;
    }

    public void setBetOnUnderdog(String betOnUnderdog) {
        this.betOnUnderdog = betOnUnderdog;
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

    // default BIG constructor
    public Bets(String away, String home, String favorite, String odds, String type, String betOnFavorite, String betOnUnderdog, String date_expires, int amount) {
        this.away = away;
        this.home = home;
        this.favorite = favorite;
        this.odds = odds;
        this.type = type;
        this.betOnUnderdog = betOnUnderdog;
        this.betOnFavorite = betOnFavorite;
        this.date_expires = date_expires;
        this.active = 0;
        this.amount = amount;
    }

    // smaller constructor to mitigate potential errors + used in testing
    public Bets(String away, String home, String favorite, String odds) {
        this.away = away;
        this.home = home;
        this.favorite = favorite;
        this.odds = odds;
    }

    public Bets(){
        // needed for cardview and bet searches
    }

}
