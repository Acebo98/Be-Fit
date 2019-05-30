package com.example.befit.Ayudas;

import android.app.Activity;
import android.util.DisplayMetrics;
/*
Clase para conocer las dimensiones de la pantalla del móvil
 */
public class Pantalla {

    //Alto de la pantalla
    public static int saberAltoPantalla(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    //Ancho de la pantalla
    public static int saberAnchoPantalla(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}