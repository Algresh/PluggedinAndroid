package ru.tulupov.alex.pluggedin.models;


public class Calendar {

    protected int id;

    protected int type;

    protected String title;

    protected String date;

    protected String file;

    public Calendar(int id, int type, String title, String date, String file) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.date = date;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
