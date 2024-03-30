package com.example.worktrackerlogin;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class salesDataClass {

    String salesDate, customerType, product, customerName;
    int unit;
    double price, value;

    public salesDataClass() {

    }

    public String getSalesDate() {
        return salesDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getProduct() {
        return product;
    }

    public int getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public double getValue() {
        return value;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public void setName(String name) {
        this.customerName = name;
    }

    public void setCustomer(String customer) {
        this.customerType = customer;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public salesDataClass(String salesDate, String customerType, String product, int unit, double price, double value, String customerName) {
        this.salesDate = salesDate;
        this.customerType = customerType;
        this.product = product;
        this.unit = unit;
        this.price = price;
        this.value = value;
        this.customerName = customerName;
    }
}
