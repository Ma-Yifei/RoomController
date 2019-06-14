package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class Co2 extends Device {
    private double c=0;
    private int value;

    @Override
    public int getImage() {
        return R.mipmap.co21;
    }
    public double getCo2(){

        return c;
    }
    public void setCo2(double c){
        this.c = c;
    }
}
