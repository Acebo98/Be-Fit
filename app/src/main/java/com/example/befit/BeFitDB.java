package com.example.befit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BeFitDB extends SQLiteOpenHelper {

    public BeFitDB(Context context) {
        super(context, Columns.NOMBRE_BD, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREACION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Columns.SESIONES);
        db.execSQL("DROP TABLE IF EXISTS " + Columns.PESOS);
        db.execSQL(SCRIPT_CREACION);
    }

    //Clase que controla el nombre de las columnas, que se me van a olvidar :(
    public static class Columns {
        public static final String NOMBRE_BD = "BeFit";
        public static final String SESIONES = "sesiones";
        public static final String PESOS = "pesos";
    }

    //Script de creación
    final String SCRIPT_CREACION = "CREATE TABLE " + Columns.SESIONES + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT NOT NULL, " +
            "musculo_1 TEXT NOT NULL, " +
            "musculo_2 TEXT NOT NULL, " +
            "musculo_3 TEXT NOT NULL, " +
            "musculo_4 TEXT NOT NULL, " +
            "actualizacion TEXT NOT NULL); " +
            "CREATE TABLE " + Columns.PESOS + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "peso_1 INTEGER NOT NULL, " +
            "peso_2 INTEGER NOT NULL, " +
            "peso_3 INTEGER NOT NULL, " +
            "peso_4 INTEGER NOT NULL, " +
            "FOREIGN KEY(idSesion) REFERENCES sesiones(" + BaseColumns._ID + ")";
}