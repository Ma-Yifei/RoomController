package com.example.may.myapplication;

public class Item {
    private String title;
    private int resId;
    public Item(String title,int resId){
        this.title = title;
        this.resId = resId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
    public int getResId(){
        return resId;
    }
}
