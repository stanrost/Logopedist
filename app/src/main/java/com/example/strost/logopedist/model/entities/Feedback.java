package com.example.strost.logopedist.model.entities;

import java.io.Serializable;

/**
 * Created by strost on 5-5-2017.
 */

public class Feedback implements Serializable {
    private String objectId;
    private int patientRating;
    private String patientPicture;
    private Boolean patientHelp;
    private String patientNotes;
    private String patientRecord;
    private String feedbackDate;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getPatientNotes() {
        return patientNotes;
    }

    public void setPatientNotes(String patientNotes) {
        this.patientNotes = patientNotes;
    }

    public String getPatientRecord() {
        return patientRecord;
    }

    public void setPatientRecord(String patientRecord) {
        this.patientRecord = patientRecord;
    }

    public String toString() {
        return this.feedbackDate + ": " + this.patientNotes;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}
