package com.example.befit.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.befit.Entidades.VOTag;
import com.example.befit.Estructura_BD.BeFitDB;
import com.example.befit.Entidades.VOSesion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        nombre = nombre.toLowerCase();      //Minúsculas

        try {
            Cursor c = database.rawQuery("SELECT nombre FROM " + BeFitDB.Structure.SESIONES,null);

            //Miramos si el nombre está duplicado
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    String nombreComprobar = c.getString(0).toLowerCase();
                    if (nombreComprobar.equals(nombre)) {
                        vof = false;
                        break;
                    }
                }
            }
        }
        catch (Exception err) {
            vof = false;
            throw new Exception(err.getMessage());
        }

        return vof;
    }

    //Comprobamos que la sesión no esté insertada, quitando eso si la posibilidad de que se modifique la
    //sesion y no se cambie el nombre
    public boolean ExistirSesion(String nombreNew, String nombreOld) throws Exception {
        boolean vof = true;

        //Minúsculas
        nombreNew = nombreNew.toLowerCase();
        nombreOld = nombreOld.toLowerCase();

        try {
            Cursor c = database.rawQuery("SELECT nombre FROM " + BeFitDB.Structure.SESIONES,null);

            //Miramos si el nombre está duplicado
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    String nombreComprobar = c.getString(0).toLowerCase();
                    if (nombreComprobar.equals(nombreOld) == false && nombreComprobar.equals(nombreNew) == true) {
                        vof = false;
                        break;
                    }
                }
            }
        }
        catch (Exception err) {
            vof = false;
            throw new Exception(err.getMessage());
        }

        return vof;
    }

    //Insert
    public void InsertSesion(VOSesion sesion) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos de la insercción
            values.put("activo", sesion.getActivo());
            values.put("nombre", sesion.getNombre());
            values.put("ejercicio_1", sesion.getEjercicio_1());
            values.put("ejercicio_2", sesion.getEjercicio_2());
            values.put("ejercicio_3", sesion.getEjercicio_3());
            values.put("ejercicio_4", sesion.getEjercicio_4());
            values.put("f_creacion", sacarFechaHoy());
            values.put("actualizacion", sacarFechaHoy());
            values.put("foto", sesion.getFoto());
            values.put("idTag", sesion.getIdTag());

            //Insertamos
            database.insert(BeFitDB.Structure.SESIONES, null, values);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Read (todas las sesiones)
    public ArrayList<VOSesion> ReadSesiones() throws Exception {
        ArrayList<VOSesion> lSesiones = null;

        try {
            lSesiones = new ArrayList<>();

            //Leemos los datos
            Cursor c = database.rawQuery("SELECT " + BaseColumns._ID + ", nombre, actualizacion, activo, idTag, foto " +
                    "FROM " + BeFitDB.Structure.SESIONES + " ORDER BY actualizacion DESC", null);

            //Recorremos el cursor
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    VOSesion sesion = new VOSesion(c.getString(1), c.getString(2));
                    sesion.setIdentificador(c.getInt(0));
                    sesion.setActivo(c.getString(3));
                    sesion.setIdTag(c.getInt(4));
                    sesion.setFoto(c.getBlob(5));           //FOTO!!!
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

    //Leemos las sesiones activas o no
    public ArrayList<VOSesion> ReadSesiones(boolean activo) throws Exception {
        ArrayList<VOSesion> lSesiones = null;
        Cursor c = null;

        try {
            lSesiones = new ArrayList<>();

            //Leemos los datos
            if (activo == true) {
                c = database.rawQuery("SELECT " + BaseColumns._ID + ", nombre, actualizacion, activo, idTag " +
                        "FROM " + BeFitDB.Structure.SESIONES + " WHERE activo = ? ORDER BY actualizacion DESC",
                        new String[] {"s"});
            }
            else {
                c = database.rawQuery("SELECT " + BaseColumns._ID + ", nombre, actualizacion, activo, idTag " +
                                "FROM " + BeFitDB.Structure.SESIONES + " WHERE activo = ? ORDER BY actualizacion DESC",
                        new String[] {"n"});
            }

            //Recorremos el cursor
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    VOSesion sesion = new VOSesion(c.getString(1), c.getString(2));
                    sesion.setIdentificador(c.getInt(0));
                    sesion.setActivo(c.getString(3));
                    sesion.setIdTag(c.getInt(4));
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
    public void DeleteSesion(String ID) throws Exception {
        try {
            database.delete(BeFitDB.Structure.SESIONES, BaseColumns._ID + " = ?", new String[] {ID});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Sacamos la información de una sesión a partir de su ID
    public VOSesion SacarSesion(String Id) throws Exception {
        VOSesion sesion = new VOSesion();

        try {
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.SESIONES + " " +
                    "WHERE " + BaseColumns._ID + " = ?", new String[] {Id} );

            //Sacamos los datos
            c.moveToNext();
            sesion.setNombre(c.getString(2));
            sesion.setEjercicio_1(c.getString(3));
            sesion.setEjercicio_2(c.getString(4));
            sesion.setEjercicio_3(c.getString(5));
            sesion.setEjercicio_4(c.getString(6));
            sesion.setIdTag(c.getInt(9));
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
            ContentValues values = new ContentValues();
            values.put("actualizacion", this.sacarFechaHoy());

            //Actualizamos
            database.update(BeFitDB.Structure.SESIONES, values, BaseColumns._ID + " = ?",
                    new String[] {String.valueOf(Id)});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Borramos todas las sesiones almacenadas
    public void DeleteSesiones() throws Exception {
        try {
            database.delete(BeFitDB.Structure.SESIONES, null, null);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Update Sesion
    public void UpdateSesion(VOSesion NSesion) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos de la modificación
            values.put("nombre", NSesion.getNombre());
            values.put("ejercicio_1", NSesion.getEjercicio_1());
            values.put("ejercicio_2", NSesion.getEjercicio_2());
            values.put("ejercicio_3", NSesion.getEjercicio_3());
            values.put("ejercicio_4", NSesion.getEjercicio_4());
            values.put("actualizacion", this.sacarFechaHoy());
            values.put("idTag", NSesion.getIdTag());

            //Realizamos la modificación
            database.update(BeFitDB.Structure.SESIONES, values, BaseColumns._ID + " = ?",
                    new String[] {String.valueOf(NSesion.getIdentificador())});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Bloqueamos una sesión pasándola a "n"
    public void BloquearSesion(String identificador) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos a modificar
            values.put("activo", "n");

            //Modificamos
            database.update(BeFitDB.Structure.SESIONES, values,BaseColumns._ID + " = ?" ,
                    new String[] {identificador});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Bloqueamos una sesión pasándola a "s"
    public void DesbloquearSesion(String identificador) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos a modificar
            values.put("activo", "s");

            //Modificamos
            database.update(BeFitDB.Structure.SESIONES, values,BaseColumns._ID + " = ?" ,
                    new String[] {identificador});
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Comprobamos si la sesión está bloqueada
    public boolean IsBlocked(String identificador) throws Exception {
        boolean vof = true;

        try {
            Cursor c = database.rawQuery("SELECT activo FROM " + BeFitDB.Structure.SESIONES +
                    " WHERE " + BaseColumns._ID + " = ?", new String[] {identificador});

            //Vemos si está activo
            c.moveToNext();
            if (c.getString(0).equals("n")) {
                vof = false;
            }
        }
        catch (Exception err) {
            vof = false;
            throw new Exception(err.getMessage());
        }

        return vof;
    }

    //Comprobamos si hay sesiones con una determinada tag
    public boolean ComprobarSiHayTags(VOTag tag) throws Exception {
        boolean vof = true;

        try {
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.SESIONES + " WHERE idTag = ?",
                    new String[] {String.valueOf(tag.getIdentificador())});
            if (c.getCount() > 0) {
                vof = !vof;
            }
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return vof;
    }
    //Sacamos la fecha de hoy
    private String sacarFechaHoy() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String hoy = simpleDateFormat.format(new Date());
        return hoy;
    }
}