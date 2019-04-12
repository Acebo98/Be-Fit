package com.example.befit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    //Sacamos los pesos a partir del identificador de la sesión a la que hacen referencia
    public VOPeso SacarPesos(int IdSesion) throws Exception {
        VOPeso peso = null;

        try {
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.PESOS + " " +
                    "WHERE idSesion = ?", new String[] {String.valueOf(IdSesion)});
            c.moveToNext();

            //Sacamos los datos
            peso = new VOPeso();
            peso.setIdentificador(c.getInt(0));
            peso.setPeso_1(c.getString(1));
            peso.setPeso_2(c.getString(2));
            peso.setPeso_3(c.getString(3));
            peso.setPeso_4(c.getString(4));
            peso.setNotas(c.getString(5));
            peso.setIdSesion(c.getInt(6));

        }
        catch (Exception err) {
            peso = null;
            throw new Exception(err.getMessage());
        }

        return peso;
    }
}
