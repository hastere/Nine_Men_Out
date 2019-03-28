package com.example.ninemenout;

public class Requests {

    private String Name;
    private boolean pending;

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getRName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Requests(String name, boolean pending) {
        this.Name = name;
        this.pending = pending;
    }

    public Requests() {
    }
}