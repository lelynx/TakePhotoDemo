package com.example.takephotodemo;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static final String TAG = "TEST_CAMERA";
    public static final int MEDIA_LIST_CODE = 1;
    public static final String MEDIA_LIST = "com.example.takephotodemo";

    public static byte[] convertBitmapToBytes(Bitmap image) {
        Bitmap immagex = image;
        while (immagex.getByteCount() >= 10000000){
            immagex = Bitmap.createScaledBitmap(immagex, immagex.getWidth() / 2, immagex.getHeight() / 2, false);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        immagex.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        return baos.toByteArray();
    }

}
