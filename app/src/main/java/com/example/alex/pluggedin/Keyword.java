package com.example.alex.pluggedin;


public class Keyword {

    private String title;

    private int id;

    public Keyword(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public Keyword(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
