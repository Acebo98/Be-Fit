package com.example.befit.Modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.befit.Entidades.VOConfiGraficas;
import com.example.befit.Estructura_BD.BeFitDB;
import com.example.befit.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DAOGraficas {

    SQLiteDatabase database;                        //Base de Datos
    SimpleDateFormat conversorFechas;               //Conversor de fechas
    Context context;                                //Contexto

    //Constructor en el que abrimos la conexión la base de datos
    public DAOGraficas(Context context) {
        BeFitDB beFitDB = new BeFitDB(context);
        database = beFitDB.getWritableDatabase();
        conversorFechas = new SimpleDateFormat("dd/MM/yyyy");
        this.context = context;
    }

    //Sacamos un hashmap con el nombre de la etiqueta y cuantas sesiones tiene
    public HashMap<String, Integer> SacarGraficaTags(VOConfiGraficas confiGraficas) {
        Cursor c = null;
        HashMap<String, Integer> tagNums =  null;

        try {
            tagNums = new HashMap<String, Integer>();

            //Query
            if (confiGraficas.getEstadoSesion().equals(context.getString(R.string.todos))) {
                c = database.rawQuery("select tag, count(*) as cuantos " +
                        "from sesiones, tags " +
                        "where tags._id = sesiones.idTag AND date(sesiones.actualizacion) " +
                        "BETWEEN date(?) AND date(?) " +
                        "group by tag " +
                        "order by 2 desc", new String[]{confiGraficas.getFechaAnterior(), confiGraficas.getFechaProxima()});
            }
            else if (confiGraficas.getEstadoSesion().equals(context.getString(R.string.bloqueados))) {
                c = database.rawQuery("select tag, count(*) as cuantos " +
                        "from sesiones, tags " +
                        "where tags._id = sesiones.idTag AND date(sesiones.actualizacion) " +
                        "BETWEEN date(?) AND date(?) AND sesiones.activo = ? " +
                        "group by tag " +
                        "order by 2 desc", new String[]{confiGraficas.getFechaAnterior(),
                        confiGraficas.getFechaProxima(), "n"});
            }
            else if (confiGraficas.getEstadoSesion().equals(context.getString(R.string.sin_bloquer))) {
                c = database.rawQuery("select tag, count(*) as cuantos " +
                        "from sesiones, tags " +
                        "where tags._id = sesiones.idTag AND date(sesiones.actualizacion) " +
                        "BETWEEN date(?) AND date(?) AND sesiones.activo = ? " +
                        "group by tag " +
                        "order by 2 desc", new String[]{confiGraficas.getFechaAnterior(),
                        confiGraficas.getFechaProxima(), "s"});
            }

            //Sacamos los datos
            if (c.getCount() > 0) {
                while (c.moveToNext() == true) {
                    tagNums.put(c.getString(0), c.getInt(1));
                }
            }
        }
        catch (Exception err) {
            tagNums = null;
        }

        return tagNums;
    }
}