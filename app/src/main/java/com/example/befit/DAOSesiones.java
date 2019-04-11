package com.example.befit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOSesiones {

    SQLiteDatabase database;                        //Base de Datos

    //Constructor en el que abrimos la conexión la base de datos
    public DAOSesiones(Context context) {
        BeFitDB beFitDB = new BeFitDB(context);
        database = beFitDB.getWritableDatabase();
    }

    //Comprobamos que la sesión no esté insertada ya a partir de su nombre
    public boolean ExistirSesion(String nombre) throws Exception {
        boolean vof = true;

        try {
            Cursor c = database.rawQuery("SELECT nombre FROM " + BeFitDB.Structure.SESIONES + " " +
                            "WHERE nombre = ?", new String[] {nombre});

            //Si hay datos significa que está duplicada
            if (c.getCount() > 0) {
                vof = false;
            }
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return vof;
    }

    //Insert
    public void InsertSesion(VOSesion sesion) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos de la insercción
            values.put("nombre", sesion.getNombre());
            values.put("musculo_1", sesion.getMusculo_1());
            values.put("musculo_2", sesion.getMusculo_2());
            values.put("musculo_3", sesion.getMusculo_3());
            values.put("musculo_4", sesion.getMusculo_4());
            values.put("actualizacion", sesion.getActualizacion());

            //Insertamos
            database.insert(BeFitDB.Structure.SESIONES, null, values);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Read
    public ArrayList<VOSesion> ReadSesiones() throws Exception {
        ArrayList<VOSesion> lSesiones = null;

        try {
            lSesiones = new ArrayList<>();

            //Leemos los datos
            Cursor c = database.rawQuery("SELECT nombre, actualizacion FROM " + BeFitDB.Structure.SESIONES, null);

            //Recorremos el cursor
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    VOSesion sesion = new VOSesion(c.getString(0), c.getString(1));
                    lSesiones.add(sesion);
                }
            }
        }
        catch (Exception err) {
            lSesiones = null;
            throw new Exception(err.getMessage());
        }

        return lSesiones;
    }
}
