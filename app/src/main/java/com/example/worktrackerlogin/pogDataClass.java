package com.example.worktrackerlogin;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class pogDataClass {

    String year, month, tech, brand, customer;

    int begInv, endInv;

    double pogUnits;
    double begInvKgs, endInvKgs, pogKgs;
    double begInvCtn, endInvCtn, pogCtn;
    double begInvVal, endInvVal, pogVal;

    public pogDataClass() {

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getBegInv() {
        return begInv;
    }

    public void setBegInv(int begInv) {
        this.begInv = begInv;
    }

    public int getEndInv() {
        return endInv;
    }

    public void setEndInv(int endInv) {
        this.endInv = endInv;
    }

    public double getPogUnits() {
        return pogUnits;
    }

    public void setPogUnits(double pogUnits) {
        this.pogUnits = pogUnits;
    }

    public double getBegInvKgs() {
        return begInvKgs;
    }

    public void setBegInvKgs(double begInvKgs) {
        this.begInvKgs = begInvKgs;
    }

    public double getEndInvKgs() {
        return endInvKgs;
    }

    public void setEndInvKgs(double endInvKgs) {
        this.endInvKgs = endInvKgs;
    }

    public double getPogKgs() {
        return pogKgs;
    }

    public void setPogKgs(double pogKgs) {
        this.pogKgs = pogKgs;
    }

    public double getBegInvCtn() {
        return begInvCtn;
    }

    public void setBegInvCtn(double begInvCtn) {
        this.begInvCtn = begInvCtn;
    }

    public double getEndInvCtn() {
        return endInvCtn;
    }

    public void setEndInvCtn(double endInvCtn) {
        this.endInvCtn = endInvCtn;
    }

    public double getPogCtn() {
        return pogCtn;
    }

    public void setPogCtn(double pogCtn) {
        this.pogCtn = pogCtn;
    }

    public double getBegInvVal() {
        return begInvVal;
    }

    public void setBegInvVal(double begInvVal) {
        this.begInvVal = begInvVal;
    }

    public double getEndInvVal() {
        return endInvVal;
    }

    public void setEndInvVal(double endInvVal) {
        this.endInvVal = endInvVal;
    }

    public double getPogVal() {
        return pogVal;
    }

    public void setPogVal(double pogVal) {
        this.pogVal = pogVal;
    }

    public pogDataClass(String year, String month, String tech, String brand, String customer, int begInv, int endInv, double pogUnits, double begInvKgs, double endInvKgs, double pogKgs, double begInvCtn, double endInvCtn, double pogCtn, double begInvVal, double endInvVal, double pogVal) {
        this.year = year;
        this.month = month;
        this.tech = tech;
        this.brand = brand;
        this.customer = customer;
        this.begInv = begInv;
        this.endInv = endInv;
        this.pogUnits = pogUnits;
        this.begInvKgs = begInvKgs;
        this.endInvKgs = endInvKgs;
        this.pogKgs = pogKgs;
        this.begInvCtn = begInvCtn;
        this.endInvCtn = endInvCtn;
        this.pogCtn = pogCtn;
        this.begInvVal = begInvVal;
        this.endInvVal = endInvVal;
        this.pogVal = pogVal;
    }
}
