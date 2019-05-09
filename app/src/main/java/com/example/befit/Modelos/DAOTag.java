package com.example.befit.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
                lTags.add(new VOTag(c.getInt(0), c.getString(1)));
            }
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }

        return lTags;
    }
}