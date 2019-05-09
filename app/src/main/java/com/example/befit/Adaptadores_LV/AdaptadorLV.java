package com.example.befit.Adaptadores_LV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.befit.Entidades.VOSesion;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

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

        //A mostrar
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewSesion);
        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtItemNombre);
        TextView txtActu = (TextView) convertView.findViewById(R.id.txtItemActu);
        TextView txtTag = (TextView) convertView.findViewById(R.id.tbTagItem);

        //Nombre y cuando fue actualizada por última vez
        txtNombre.setText(sesion.getNombre());
        txtActu.setText(sesion.getActualizacion());

        //Etiqueta
        String tag = new DAOTag(context).SacarNombre(sesion.getIdTag());
        if (tag == null) {
            txtTag.setText("ERROR");
        }
        else{
            txtTag.setText(tag);
        }

        //Icono
        if (sesion.getActivo().equals("s")) {
            imageView.setImageResource(R.drawable.itemmancuerna);
        }
        else {
            imageView.setImageResource(R.drawable.lock);
        }

        return convertView;
    }

    //Filtramos a partir del nombre y del criterio de búsqueda
    public void Filtrar(String nombre, int criterio) throws Exception {
        lSesiones.clear();

        //Guardamos una lista con todos los datos a partir del criterio...
        switch (criterio) {
            case 0: {
                lSesionesCopia = new DAOSesiones(context).ReadSesiones();
            }
            break;
            case 1: {
                lSesionesCopia = new DAOSesiones(context).ReadSesiones(false);
            }
            break;
            case 2: {
                lSesionesCopia = new DAOSesiones(context).ReadSesiones(true);
            }
            break;
        }

        //Si no hay texto cargamos todos los datos a partir del criterio de búsqueda
        if (nombre.trim().length() == 0) {
            lSesiones.addAll(lSesionesCopia);
        }
        else {
            for (VOSesion sesion : lSesionesCopia) {
                if (sesion.getNombre().contains(nombre.trim()) == true) {
                    lSesiones.add(sesion);
                }
            }
        }

        //Notificamos de los campos
        this.notifyDataSetChanged();
    }
}