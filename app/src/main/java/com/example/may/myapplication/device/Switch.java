package com.example.may.myapplication.device;

import com.example.may.myapplication.R;

public class Switch extends Device{
    private boolean status = false;
    @Override
    public int getImage() {
        return R.mipmap.switch1;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
