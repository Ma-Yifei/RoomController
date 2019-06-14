package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class Curtain extends Device{
    private int status;
    @Override
    public int getImage() {
        return R.mipmap.curtain;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
