package com.example.befit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOError;

public class DAOPesos {

    SQLiteDatabase database;                        //Base de Datos

    //Constructor en el que abrimos la conexión la base de datos
    public DAOPesos(Context context) {
        BeFitDB beFitDB = new BeFitDB(context);
        database = beFitDB.getWritableDatabase();
    }

    //Insertamos un peso por defecto a partir del identificador de la sesión a la que está relacionada
    public void InsertarPeso(int IdSesion) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos de la insercción
            values.put("peso_1", "5kg");
            values.put("peso_2", "5kg");
            values.put("peso_3", "5kg");
            values.put("peso_4", "5kg");
            values.put("notas", "Escribe aquí tus notas");
            values.put("idSesion", IdSesion);

            //Insertamos
            database.insert(BeFitDB.Structure.PESOS, null, values);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }
}
