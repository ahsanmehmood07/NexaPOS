package com.example.nexapos;

public class Customer {
    private String name;
    private String phone;
    private String address;
    private String type;

    public Customer(String name, String phone, String address, String type) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type = type;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return name + "  •  " + phone;
    }
}