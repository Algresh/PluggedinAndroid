package com.example.alex.pluggedin;


import java.util.ArrayList;

public class Review {

    protected int id;

    protected String title;

    protected String text;

    protected String author;

    protected String authorSrc;

    protected String datePublish;

    protected float mark;

    protected String plusesMinuses;

    protected String conclusion;

    protected String file;

    public Review(int id, String title, String text, String author, String authorSrc, String datePublish) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.author = author;
        this.authorSrc = authorSrc;
        this.datePublish = datePublish;
    }

    /**
     * @TODO create class for keyword!
     */
    protected ArrayList<String> keywords;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorSrc() {
        return authorSrc;
    }

    public void setAuthorSrc(String authorSrc) {
        this.authorSrc = authorSrc;
    }

    public String getDatePublish() {
        return datePublish;
    }

    public void setDatePublish(String datePublish) {
        this.datePublish = datePublish;
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

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
