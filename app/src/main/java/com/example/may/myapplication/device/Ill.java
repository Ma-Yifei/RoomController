package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class Ill extends Device {
    private double i=0;
    @Override
    public int getImage() {
        return R.mipmap.ill1;
    }

    public double getIll() {
        return i;
    }

    public void setIll(double i) {
        this.i = i;
    }

}
