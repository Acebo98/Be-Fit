package com.example.befit.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.befit.Entidades.VOTag;
import com.example.befit.Estructura_BD.BeFitDB;

import java.util.ArrayList;

public class DAOTag {

    SQLiteDatabase database;                        //Base de Datos

    //Constructor en el que abrimos la conexión la base de datos
    public DAOTag(Context context) {
        BeFitDB beFitDB = new BeFitDB(context);
        database = beFitDB.getWritableDatabase();
    }

    //Insert
    public void InsertarTag(VOTag nTag) throws Exception {
        try {
            ContentValues values = new ContentValues();

            //Datos
            values.put("tag", nTag.getTag());

            //Insert
            database.insert(BeFitDB.Structure.TAGS, null, values);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }

    //Read
    public ArrayList<VOTag> ReadTags() throws Exception {
        ArrayList<VOTag> lTags = null;

        try {
            lTags = new ArrayList<>();

            //Lectura
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.TAGS, null);
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    lTags.add(new VOTag(c.getInt(0), c.getString(1)));
                }
            }
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return lTags;
    }

    //Comprobamos si exixte una etiqueta
    public boolean ExistirTag(VOTag tag) throws Exception {
        boolean vof = false;

        try {
            //Buscamos una tag por el nombre, si hay datos significa que ya está introducida
            Cursor c = database.rawQuery("SELECT * FROM " + BeFitDB.Structure.TAGS + " WHERE tag = ?",
                    new String[] {tag.getTag()});
            if (c.getCount() == 0) {
                vof = true;
            }
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return vof;
    }

    //Sacamos las tags mediante una array de Strings
    public String[] SacarArrayTags() throws Exception {
        String[] arrayTags = null;

        try {
            //Leemos las tags
            ArrayList<VOTag> lTags = ReadTags();

            //Pasamos la lista a array...
            arrayTags = new String[lTags.size()];
            for (int i = 0; i < lTags.size(); i++) {
                arrayTags[i] = lTags.get(i).getTag();
            }
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return arrayTags;
    }

    //Sacamos el ID a partir del nombre de la tag
    public int SacarID(String nombre) throws Exception {
        int identificador = -1;

        try {
            Cursor c = database.rawQuery("SELECT " + BaseColumns._ID + " FROM " + BeFitDB.Structure.TAGS + " WHERE tag = ?",
                    new String[] {nombre});
            c.moveToNext();
            identificador = c.getInt(0);
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return identificador;
    }
}