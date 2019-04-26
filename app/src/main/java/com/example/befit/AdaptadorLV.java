package com.example.befit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorLV extends BaseAdapter {

    private Context context;                        //Contexto
    private ArrayList<VOSesion> lSesiones;          //Datos que se van a mostrar (importante)
    private ArrayList<VOSesion> lSesionesCopia;     //Copia de TODOS los datos de la BD. Útilizada para el buscador

    public AdaptadorLV(Context Context, ArrayList<VOSesion> Sesiones) {
        this.context = Context;
        this.lSesiones = Sesiones;
    }

    @Override
    public int getCount() {
        return lSesiones.size();
    }

    @Override
    public Object getItem(int position) {
        return lSesiones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VOSesion sesion = (VOSesion) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.itemsesion, null);

        //Textos a mostrar
        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtItemNombre);
        TextView txtActu = (TextView) convertView.findViewById(R.id.txtItemActu);

        //Asignamos el texto
        txtNombre.setText(sesion.getNombre());
        txtActu.setText(sesion.getActualizacion());

        return convertView;
    }

    //Filtrador
    public void Filtrar(String nombre) throws Exception {
        /*lSesiones.clear();                                          //Limpiamos la listview
        lSesionesCopia = new DAOSesiones(context).ReadSesiones();   //Copia de la base de datos

        //Si no hay texto cargamos todos los datos
        if (nombre.trim().length() == 0) {
            lSesiones.addAll(new DAOSesiones(context).ReadSesiones());
        }
        else {
            for (VOSesion sesion : lSesionesCopia) {
                if (sesion.getNombre().contains(nombre.trim()) == true) {
                    lSesiones.add(sesion);
                }
            }
        }

        //Notificamos de los campos
        this.notifyDataSetChanged();*/
    }
}
