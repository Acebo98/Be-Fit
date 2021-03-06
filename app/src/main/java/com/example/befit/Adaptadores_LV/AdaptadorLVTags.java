package com.example.befit.Adaptadores_LV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.befit.Entidades.VOSesion;
import com.example.befit.Entidades.VOTag;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

import java.util.ArrayList;

public class AdaptadorLVTags extends BaseAdapter {

    private Context context;                    //Contexto
    private ArrayList<VOTag> lTags;             //Lista de tags
    private ArrayList<VOTag> lTagsCopia;        //Lista de tags copia

    public AdaptadorLVTags(Context context, ArrayList<VOTag> lTags) {
        this.context = context;
        this.lTags = lTags;
    }

    @Override
    public int getCount() {
        return lTags.size();
    }

    @Override
    public Object getItem(int position) {
        return lTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VOTag tag = (VOTag) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_tag, null);

        //Texto a mostrar
        TextView tvTag = (TextView) convertView.findViewById(R.id.tvTagItem);
        tvTag.setText(tag.getTag());

        return convertView;
    }

    //Filtrador
    public void filtrar(String tag) throws Exception {
        lTags.clear();

        try {
            lTagsCopia = new DAOTag(context).ReadTags();

            if (tag.trim().length() == 0) {
                lTags.addAll(lTagsCopia);
            }
            else {
                for (VOTag tg : lTagsCopia) {
                    if (tg.getTag().contains(tag) == true) {
                        lTags.add(tg);
                    }
                }
            }

            this.notifyDataSetChanged();
        }
        catch (Exception err) {
            throw new Exception(err.getMessage());
        }
    }
}