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

import com.example.befit.Actividades.LogeoActivity;
import com.example.befit.Entidades.VOConfiGraficas;
import com.example.befit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogoFechasGraficas {

    Context context;                                //Contexto

    String[] lEstados;                              //Lista con los estados

    SimpleDateFormat simpleDateFormat;              //Convertidor de fechas

    DialogoFechasGraficasListener interfaz;         //Interfaz

    //Controles
    EditText tbFechaAnterior;
    EditText tbFechaProxima;
    Button btnAceptar;
    Button btnCancelar;
    Spinner spnEstados;

    //Interfaz
    public interface DialogoFechasGraficasListener {
        void AceptarDialogo(VOConfiGraficas confiGraficas);
        void CancelarDialogo();
    }

    public DialogoFechasGraficas(final Context context, DialogoFechasGraficasListener actividad) {
        this.context = context;
        this.interfaz = actividad;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");      //Convertidor de fechas

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
                try {
                    VOConfiGraficas confiGraficas = new VOConfiGraficas();

                    //Validamos los datos...
                    Date valida1 = simpleDateFormat.parse(tbFechaAnterior.getText().toString().trim());
                    Date valida2 = simpleDateFormat.parse(tbFechaProxima.getText().toString().trim());

                    //Pasamos los datos
                    confiGraficas.setFechaAnterior(tbFechaAnterior.getText().toString().trim());
                    confiGraficas.setFechaProxima(tbFechaProxima.getText().toString().trim());
                    confiGraficas.setEstadoSesion(spnEstados.getSelectedItem().toString());

                    interfaz.AceptarDialogo(confiGraficas);
                    dialog.dismiss();
                }
                catch (ParseException err) {
                    //Excepción por introducir un formato de fechas mal
                    LogeoActivity.centralizarToast(context, context.getString(R.string.fechas_mal));
                }
                catch (Exception err) {
                    LogeoActivity.centralizarToast(context, err.getMessage());
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.CancelarDialogo();
                dialog.dismiss();
            }
        });

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }
}