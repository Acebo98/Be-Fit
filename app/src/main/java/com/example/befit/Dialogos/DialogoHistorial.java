package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.befit.Entidades.VOPeso;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.R;

import java.util.ArrayList;

public class DialogoHistorial {

    //Controles
    TextView labFecha;
    TextView labEjercicio1;
    TextView labEjercicio2;
    TextView labEjercicio3;
    TextView labEjercicio4;
    EditText tbEjercicio1;
    EditText tbEjercicio2;
    EditText tbEjercicio3;
    EditText tbEjercicio4;
    EditText tbNotas;

    //Lista de campos de texto
    ArrayList<EditText> lCampos;

    public DialogoHistorial(Context context, VOPeso peso, VOSesion sesion) {

        //Configuración del cuadro de díalogo
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialogo_historial);

        //Controles
        labFecha = (TextView) dialog.findViewById(R.id.labHistorialFecha);
        labEjercicio1 = (TextView) dialog.findViewById(R.id.labHistorialEjercicio1);
        labEjercicio2 = (TextView) dialog.findViewById(R.id.labHistorialEjercicio2);
        labEjercicio3 = (TextView) dialog.findViewById(R.id.labHistorialEjercicio3);
        labEjercicio4 = (TextView) dialog.findViewById(R.id.labHistorialEjercicio4);
        tbEjercicio1 = (EditText) dialog.findViewById(R.id.tbHistorialEjercicio1);
        tbEjercicio2 = (EditText) dialog.findViewById(R.id.tbHistorialEjercicio2);
        tbEjercicio3 = (EditText) dialog.findViewById(R.id.tbHistorialEjercicio3);
        tbEjercicio4 = (EditText) dialog.findViewById(R.id.tbHistorialEjercicio4);
        tbNotas = (EditText) dialog.findViewById(R.id.tbHistorialNotas);

        //Lista de campos de texto
        lCampos = new ArrayList<>();
        lCampos.add(tbEjercicio1);
        lCampos.add(tbEjercicio2);
        lCampos.add(tbEjercicio3);
        lCampos.add(tbEjercicio4);
        lCampos.add(tbNotas);
        for (EditText editText : lCampos) {
            editText.setEnabled(false);
        }

        //Aplicamos el texto
        labFecha.setText(peso.getFecha_Peso());
        labEjercicio1.setText(sesion.getEjercicio_1());
        tbEjercicio1.setText(peso.getPeso_1());
        labEjercicio2.setText(sesion.getEjercicio_2());
        tbEjercicio2.setText(peso.getPeso_2());
        labEjercicio3.setText(sesion.getEjercicio_3());
        tbEjercicio3.setText(peso.getPeso_3());
        labEjercicio4.setText(sesion.getEjercicio_4());
        tbEjercicio4.setText(peso.getPeso_4());
        tbNotas.setText(peso.getNotas());

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }
}