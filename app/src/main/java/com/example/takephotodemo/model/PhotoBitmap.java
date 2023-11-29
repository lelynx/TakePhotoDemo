package com.example.takephotodemo.model;

import android.graphics.Bitmap;

public class PhotoBitmap {


    String name;
    Bitmap bitmap;

    public PhotoBitmap(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "PhotoBitmap{" +
                "name='" + name + '\'' +
                '}';
    }
}
