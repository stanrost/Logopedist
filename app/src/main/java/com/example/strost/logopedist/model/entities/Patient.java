package com.example.strost.logopedist.model.entities;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strost on 16-2-2017.
 */

public class Patient {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("objectId")
    private String objectId;
    @SerializedName("exercises")
    List<Exercise> exercises = new ArrayList<Exercise>();
    @SerializedName("devices")
    List<Device> devices = new ArrayList<Device>();

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

    public void addExercise(int exerciseId, String exerciseTitle, String exercisePicture, Boolean help, String description)
    {
        Exercise o1 = new Exercise();
        o1.setId(exerciseId);
        o1.setTitle(exerciseTitle);
        o1.setPicture(exercisePicture);
        o1.setHelp(help);
        o1.setDescription(description);
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}