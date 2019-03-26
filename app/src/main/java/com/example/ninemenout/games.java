package com.example.ninemenout;

public class games {
    private String away_team, home_team, event_date;
    private double over_under, home_spread;


    public String getHome_team() {
        return home_team;
    }
    public String getAway_team() {
        return away_team;
    }
    public double getHome_spread() { return home_spread; }
    public double getOver_under() { return over_under; }
    public String getEvent_date() { return event_date; }

    public void setHome_team(String home_team) { this.home_team = home_team; }
    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }
    public void setHome_spread(double home_spread) {
        this.home_spread = home_spread;
    }
    public void setOver_under(double over_under) { this.over_under = over_under; }
    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }


    public games(String away_team, String home_team, double home_spread, double over_under, String event_date) {
        this.away_team = away_team;
        this.home_team = home_team;
        this.home_spread = home_spread;
        this.over_under = over_under;
        this.event_date = event_date;
    }
    public games() {}

}
