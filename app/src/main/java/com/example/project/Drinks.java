package com.example.project;

import androidx.room.PrimaryKey;

import com.google.firebase.database.DatabaseReference;

public class Drinks {

    private String name;
    private String price;

    /*public Drinks() {};*/

    public Drinks(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
