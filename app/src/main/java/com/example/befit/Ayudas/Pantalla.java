package com.example.befit.Ayudas;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
/*
Clase para conocer las dimensiones de la pantalla del móvil
 */
public class Pantalla {

    //Alto de la pantalla
    public static int saberAltoPantalla(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    //Ancho de la pantalla
    public static int saberAnchoPantalla(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}