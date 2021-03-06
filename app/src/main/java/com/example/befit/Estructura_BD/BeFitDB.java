package com.example.befit.Estructura_BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BeFitDB extends SQLiteOpenHelper {

    public BeFitDB(Context context) {
        super(context, Structure.NOMBRE_BD, null, 9);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_SESIONES);
        db.execSQL(SCRIPT_PESOS);
        db.execSQL(SCRIPT_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Structure.SESIONES);
        db.execSQL("DROP TABLE IF EXISTS " + Structure.PESOS);
        db.execSQL("DROP TABLE IF EXISTS " + Structure.TAGS);
        onCreate(db);
    }

    //Clase que controla el nombre de las columnas, que se me van a olvidar :(
    public static class Structure {
        public static final String NOMBRE_BD = "BeFit";
        public static final String SESIONES = "sesiones";
        public static final String PESOS = "pesos";
        public static final String TAGS = "tags";
    }

    //Scripts de creación
    final String SCRIPT_SESIONES = "CREATE TABLE " + Structure.SESIONES + " (" +
            "activo TEXT NOT NULL, " +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT NOT NULL, " +
            "ejercicio_1 TEXT NOT NULL, " +
            "ejercicio_2 TEXT NOT NULL, " +
            "ejercicio_3 TEXT NOT NULL, " +
            "ejercicio_4 TEXT NOT NULL, " +
            "f_creacion NOT NULL, " +
            "actualizacion TEXT NOT NULL, " +
            "idTag INTEGER NOT NULL, " +        //Clave foránea para la etiqueta
            "foto BLOB)";
    final String SCRIPT_PESOS = "CREATE TABLE " + Structure.PESOS + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "peso_1 TEXT NOT NULL, " +
            "peso_2 TEXT NOT NULL, " +
            "peso_3 TEXT NOT NULL, " +
            "peso_4 TEXT NOT NULL, " +
            "notas TEXT, " +
            "fecha_peso TEXT NOT NULL, " +
            "idSesion INTEGER NOT NULL)";       //Clave foránea para la sesión
    final String SCRIPT_TAGS = "CREATE TABLE " + Structure.TAGS + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "tag TEXT NOT NULL)";
}