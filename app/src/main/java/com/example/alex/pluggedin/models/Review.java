package com.example.alex.pluggedin.models;


import java.util.ArrayList;

public class Review extends Article{

    protected float mark;

    protected String plusesMinuses;

    protected String conclusion;

    public Review() {
        setType(-1);
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getPlusesMinuses() {
        return plusesMinuses;
    }

    public void setPlusesMinuses(String plusesMinuses) {
        this.plusesMinuses = plusesMinuses;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
}
