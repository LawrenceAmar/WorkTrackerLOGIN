package com.example.worktrackerlogin.activity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class activityDataClass {

    String actDate, actName, actType, actCrop, actContactPerson, actNumber;
    int actReach;

    public activityDataClass() {

    }

    public String getActDate() {
        return actDate;
    }

    public String getActName() {
        return actName;
    }

    public String getActType() {
        return actType;
    }

    public String getActCrop() {
        return actCrop;
    }

    public String getActContactPerson() {
        return actContactPerson;
    }

    public int getActReach() {
        return actReach;
    }

    public String getActNumber() {
        return actNumber;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public void setActCrop(String actCrop) {
        this.actCrop = actCrop;
    }

    public void setActContactPerson(String actContactPerson) {
        this.actContactPerson = actContactPerson;
    }

    public void setActReach(int actReach) {
        this.actReach = actReach;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }

    public activityDataClass(String actDate, String actName, String actType, String actCrop, String actContactPerson, int actReach, String actNumber) {
        this.actDate = actDate;
        this.actName = actName;
        this.actType = actType;
        this.actCrop = actCrop;
        this.actContactPerson = actContactPerson;
        this.actReach = actReach;
        this.actNumber = actNumber;
    }
}
