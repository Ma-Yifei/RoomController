package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class Hcho extends Device{
    private double ho=0;
    @Override
    public int getImage() {
        return R.mipmap.hcho1;
    }
    public double getHcho(){
        return ho;
    }
    public void setHcho(double ho){
        this.ho = ho;

    }
}
