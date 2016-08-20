package ru.tulupov.alex.pluggedin.models;

public class Slide {

    protected int id;
    protected String text;
    protected String link;
    protected String file;

    public Slide(int id, String text, String link, String file) {
        this.id = id;
        this.text = text;
        this.link = link;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
