package com.example.ninemenout;


public class Users {

    private String email, name;
    private int activePoints, points;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActivePoints() {
        return activePoints;
    }

    public void setActivePoints(int activePoints) {
        this.activePoints = activePoints;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Users(String email, String name, int activePoints, int points) {
        this.email = email;
        this.name = name;
        this.activePoints = activePoints;
        this.points = points;
    }

    public Users() {};

}
