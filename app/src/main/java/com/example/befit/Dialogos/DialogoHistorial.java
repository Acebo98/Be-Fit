package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.befit.Entidades.VOPeso;
import com.example.befit.R;

public class DialogoHistorial {

    TextView labFecha;
    TextView labEjercicio1;
    TextView labEjercicio2;
    TextView labEjercicio3;
    TextView labEjercicio4;
    EditText tbNotas;

    public DialogoHistorial(Context context, VOPeso peso) {

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
        tbNotas = (EditText) dialog.findViewById(R.id.tbHistorialNotas);

        //Aplicamos el texto
        labFecha.setText(peso.getFecha_Peso());
        labEjercicio1.setText("Ejercicio 1: " + peso.getPeso_1());
        labEjercicio2.setText("Ejercicio 2: " + peso.getPeso_2());
        labEjercicio3.setText("Ejercicio 3: " + peso.getPeso_3());
        labEjercicio4.setText("Ejercicio 4: " + peso.getPeso_4());
        tbNotas.setText(peso.getNotas());

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }
}