package com.endproject.endproject;

public class Store {
    String name;
    String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
