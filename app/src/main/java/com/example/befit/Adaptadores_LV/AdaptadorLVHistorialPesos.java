package com.example.befit.Adaptadores_LV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.befit.Entidades.VOPeso;
import com.example.befit.R;

import java.util.ArrayList;

public class AdaptadorLVHistorialPesos extends BaseAdapter {

    private Context context;                //Contexto
    private ArrayList<VOPeso> lPesos;       //Lista de los pesos

    public AdaptadorLVHistorialPesos(Context context, ArrayList<VOPeso> lPesos) {
        this.context = context;
        this.lPesos = lPesos;
    }

    @Override
    public int getCount() {
        return this.lPesos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.lPesos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VOPeso peso = (VOPeso) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_registro_peso, null);

        //Texto a mostrar
        TextView tbFecha = (TextView)convertView.findViewById(R.id.tbRegistroPeso);
        tbFecha.setText(peso.getFecha_Peso());

        return convertView;
    }
}