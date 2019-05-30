package com.example.befit.Ayudas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ConversorFotos {

    //Ancho y alto de la compresión
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static final int QUALITY = 70;

    //Convertimos la imagen en un array de bytes
    public static byte[] ImageToBytes(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    //Transformamos los bytes a una imagen
    public static Bitmap BytesToPhoto(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}