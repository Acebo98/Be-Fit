package com.example.befit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOError;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DAOPesos {

    SQLiteDatabase database;                        //Base de Datos

    //Constructor en el que abrimos la conexión la base de datos
    public DAOPesos(Context context) {
        BeFitDB beFitDB = new BeFitDB(context);
        database = beFitDB.getWritableDatabase();
    }

    //Insertamos un peso
    public void InsertarPeso(VOPeso peso) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos de la insercción
            values.put("peso_1", peso.getPeso_1());
            values.put("peso_2", peso.getPeso_2());
            values.put("peso_3", peso.getPeso_3());
            values.put("peso_4", peso.getPeso_4());
            values.put("notas", peso.getNotas());
            values.put("fecha_peso", sacarFechaHoy());
            values.put("idSesion", peso.getIdSesion());

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
                    "WHERE idSesion = ? ORDER BY fecha_peso DESC", new String[] {String.valueOf(IdSesion)});
            c.moveToNext();

            //Sacamos los datos
            peso = new VOPeso();
            peso.setIdentificador(c.getInt(0));
            peso.setPeso_1(c.getString(1));
            peso.setPeso_2(c.getString(2));
            peso.setPeso_3(c.getString(3));
            peso.setPeso_4(c.getString(4));
            peso.setNotas(c.getString(5));
            peso.setIdSesion(c.getInt(7));
        }
        catch (Exception err) {
            peso = null;
            throw new Exception(err.getMessage());
        }

        return peso;
    }

    //Update de los pesos a partir del identificador de la sesión
    public void UpdatePeso(VOPeso NPeso, String IdSesion) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos del nuevo peso
            values.put("peso_1", NPeso.getPeso_1());
            values.put("peso_2", NPeso.getPeso_2());
            values.put("peso_3", NPeso.getPeso_3());
            values.put("peso_4", NPeso.getPeso_4());
            values.put("notas", NPeso.getNotas());

            //Actualizamos
            database.update(BeFitDB.Structure.PESOS, values, "idSesion = ?", new String[] {IdSesion});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Borramos un peso a partir del ID de su sesión
    public void DeletePeso(String IdSesion) throws Exception {
        try {
            database.delete(BeFitDB.Structure.PESOS, "idSesion = ?", new String[] {IdSesion});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Borramos todos los pesos
    public void DeletePeso() throws Exception {
        try {
            database.delete(BeFitDB.Structure.PESOS, null, null);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Sacamos el número de pesos que tiene una sesión
    public int SacarNumPesos(String IdSesion) throws Exception {
        int num = 0;

        try {
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.PESOS + " WHERE idSesion = ?",
                    new String[] {IdSesion});
            num = c.getCount();
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return num;
    }

    //Sacamos la fecha de hoy
    private String sacarFechaHoy() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String hoy = simpleDateFormat.format(new Date());
        return hoy;
    }
}