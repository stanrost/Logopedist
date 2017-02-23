package com.example.strost.logopedist.model.entities;

/**
 * Created by strost on 17-2-2017.
 */

public class Opdracht {
    private int id;
    private String title;


    public Opdracht (int id, String title){
        this.id = id;
        this.title = title;
    }

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String toString() {
        return this.id + ". " + this.title;
    }

}
