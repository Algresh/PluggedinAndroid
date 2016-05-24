package com.example.alex.pluggedin.models;

import java.util.ArrayList;

public class Article {
    protected int id;

    protected String title;

    protected String text;

    protected String author;

    protected String authorSrc;

    protected String datePublish;

    protected String file;

    private int type;

    protected ArrayList<Keyword> keywords;


    public Article() {
    }

    public Article(int id, String title, String text, String author, String authorSrc, String datePublish) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.author = author;
        this.authorSrc = authorSrc;
        this.datePublish = datePublish;
    }

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<Keyword> keywords) {
        this.keywords = keywords;
    }

    public void addKeywords(Keyword keyword) {
        this.keywords.add(keyword);
    }

    public int countKeywords() {
        return this.keywords.size();
    }


}
