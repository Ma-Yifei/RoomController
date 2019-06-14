package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class Pm extends Device {
    private double p=0;
    @Override
    public int getImage() {
        return R.mipmap.pm1;
    }
    public double getPm(){
        return p;
    }
    public void setPm(double p){
        this.p = p;
    }
}
