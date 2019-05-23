package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.befit.R;

public class DialogoFechasGraficas {

    Context context;                                //Contexto

    String[] lEstados;                              //Lista con los estados

    DialogoFechasGraficasListener interfaz;         //Interfaz

    //Controles
    EditText tbFechaAnterior;
    EditText tbFechaProxima;
    Button btnAceptar;
    Button btnCancelar;
    Spinner spnEstados;

    //Interfaz
    public interface DialogoFechasGraficasListener {
        void AceptarDialogo();
        void CancelarDialogo();
    }

    public DialogoFechasGraficas(Context context, DialogoFechasGraficasListener actividad) {
        this.context = context;
        this.interfaz = actividad;

        //Configuración del cuadro de díalogo
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialogo_confi_graficas);

        //IDS
        tbFechaAnterior = (EditText) dialog.findViewById(R.id.tbFechaAnterior);
        tbFechaProxima = (EditText) dialog.findViewById(R.id.tbFechaProxima);
        btnAceptar = (Button) dialog.findViewById(R.id.btnAceptarFechas);
        btnCancelar = (Button) dialog.findViewById(R.id.btnCancelarFechas);
        spnEstados = (Spinner) dialog.findViewById(R.id.spnEstadoSesion);

        //Spinner
        lEstados = new String[] {context.getString(R.string.todos), context.getString(R.string.bloqueados), context.getString(R.string.sin_bloquer)};
        spnEstados.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, lEstados));

        //Conteoles
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();   //Temporal
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }
}