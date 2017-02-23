package com.example.strost.logopedist.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class Patient {
    private int id;
    private String name;
    List<Opdracht> opdrachten  = new ArrayList<Opdracht>();

    public Patient(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
    public void addOpdracht(int opdrachtId, String opdrachtTitle)
    {
        Opdracht o1 = new Opdracht(opdrachtId, opdrachtTitle);
        opdrachten.add(o1);
    }

    public List<Opdracht> getList(){
        return opdrachten;
    }

    public void removeExcersice(Opdracht opdracht){
        opdrachten.remove(opdracht);
    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }

}