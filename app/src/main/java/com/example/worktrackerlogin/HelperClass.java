package com.example.worktrackerlogin;

public class HelperClass {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactn() {
        return contactn;
    }

    public void setContactn(String contactn) {
        this.contactn = contactn;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String name,username, email, address, contactn, territory, password;

    public HelperClass(String name,String username, String email, String address,String contactn,String territory, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.contactn = contactn;
        this.territory = territory;
        this.password = password;
    }

}
