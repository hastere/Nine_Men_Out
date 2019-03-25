package com.example.ninemenout;

public class Requests {

    private String name;
    private boolean pending;

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Requests(String name, boolean pending) {
        this.name = name;
        this.pending = pending;
    }

    public Requests() {
    }
}