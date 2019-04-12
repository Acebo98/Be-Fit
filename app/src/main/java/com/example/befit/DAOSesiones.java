package com.example.befit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.design.widget.TabLayout;

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
            Cursor c = database.rawQuery("SELECT " + BaseColumns._ID + ", nombre, actualizacion " +
                    "FROM " + BeFitDB.Structure.SESIONES, null);

            //Recorremos el cursor
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    VOSesion sesion = new VOSesion(c.getString(1), c.getString(2));
                    sesion.setIdentificador(c.getInt(0));
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

    //Delete
    public void DeletePeso(String ID) throws Exception {
        try {
            database.delete(BeFitDB.Structure.SESIONES, BaseColumns._ID + " = ?", new String[] {ID});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Sacamos la información de una sesión
    public VOSesion SacarSesion(String Id) throws Exception {
        VOSesion sesion = new VOSesion();

        try {
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.SESIONES + " " +
                    "WHERE " + BaseColumns._ID + " = ?", new String[] {Id} );

            //Sacamos los datos
            c.moveToNext();
            sesion.setNombre(c.getString(1));
            sesion.setMusculo_1(c.getString(2));
            sesion.setMusculo_2(c.getString(3));
            sesion.setMusculo_3(c.getString(4));
            sesion.setMusculo_4(c.getString(5));
        }
        catch (Exception err) {
            sesion = null;
            throw new Exception(err.getMessage());
        }

        return sesion;
    }

    //Sacamos el identificador a partir de su nombre
    public int SacarIdentificador(String nombre) throws Exception {
        int ID = 0;

        try {
            Cursor c = database.rawQuery("SELECT " + BaseColumns._ID + " FROM " + BeFitDB.Structure.SESIONES + " " +
                    "WHERE nombre = ?", new String[] {nombre});
            c.moveToNext();
            ID = c.getInt(0);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return ID;
    }

    //Actualizamos la fecha de actualización al día de hoy
    public void UpdateFecha(int Id) throws Exception {
        try {
            //Fecha de hoy
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String hoy = simpleDateFormat.format(new Date());
            ContentValues values = new ContentValues();
            values.put("actualizacion", hoy);

            //Actualizamos
            database.update(BeFitDB.Structure.SESIONES, values, BaseColumns._ID + " = ?",
                    new String[] {String.valueOf(Id)});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }
}