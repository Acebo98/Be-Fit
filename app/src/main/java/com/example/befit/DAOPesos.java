package com.example.befit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAOPesos {

    SQLiteDatabase database;                        //Base de Datos

    //Constructor en el que abrimos la conexión la base de datos
    public DAOPesos(Context context) {
        BeFitDB beFitDB = new BeFitDB(context);
        database = beFitDB.getWritableDatabase();
    }
}
