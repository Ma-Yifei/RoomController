package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class TH extends Device {
    private double t = 25;
    private double h = 80;

    @Override
    public int getImage() {
        return R.mipmap.th1;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }
}
