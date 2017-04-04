package com.example.strost.logopedist.model.entities;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by strost on 17-2-2017.
 */

public class Exercise implements Serializable {
    private int id;
    private String title;
    private String picture;
    private Boolean help;
    private String description;
    private Date endDate;
    private int numberOfTimes;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    private String objectId;

    private int patientRating;
    private String patientPicture;
    private Boolean patientHelp;
    private int patientNumberOfTimes;
    private String patientNotes;

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getHelp() {
        return help;
    }

    public void setHelp(Boolean help) {
        this.help = help;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public int getPatientRating() {
        return patientRating;
    }

    public void setPatientRating(int patientRating) {
        this.patientRating = patientRating;
    }

    public String getPatientPicture() {
        return patientPicture;
    }

    public void setPatientPicture(String patientPicture) {
        this.patientPicture = patientPicture;
    }

    public Boolean getPatientHelp() {
        return patientHelp;
    }

    public void setPatientHelp(Boolean patientHelp) {
        this.patientHelp = patientHelp;
    }

    public int getPatientNumberOfTimes() {
        return patientNumberOfTimes;
    }

    public void setPatientNumberOfTimes(int patientNumberOfTimes) {
        this.patientNumberOfTimes = patientNumberOfTimes;
    }

    public String getPatientNotes() {
        return patientNotes;
    }

    public void setPatientNotes(String patientNotes) {
        this.patientNotes = patientNotes;
    }
}
