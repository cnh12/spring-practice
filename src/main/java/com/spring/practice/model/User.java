package com.spring.practice.model;

public class User {

    private String id;
    private String name;
    private String pass;
    private int stamps;

    public User(String id, String name, String pass, int stamps) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.stamps = stamps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getStamps() {
        return stamps;
    }

    public void setStamps(int stamps) {
        this.stamps = stamps;
    }
}
