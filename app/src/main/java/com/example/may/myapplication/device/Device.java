package com.example.may.myapplication.device;

import com.example.may.myapplication.MainActivity;

public abstract class Device{
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract int getImage();
}