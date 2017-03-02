package com.example.strost.logopedist.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class Patient {
    private int id;
    private String name;
    private String email;
    private String password;

   List<Exercise> exercises = new ArrayList<Exercise>();

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<Exercise> getExercises(){
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void removeExcersice(Exercise exercise){
        exercises.remove(exercise);
    }

    public void addExercise(int exerciseId, String exerciseTitle)
    {
        Exercise o1 = new Exercise();
        o1.setId(exerciseId);
        o1.setTitle(exerciseTitle);
        exercises.add(o1);
    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}