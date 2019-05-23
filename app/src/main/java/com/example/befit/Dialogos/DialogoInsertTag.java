package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.befit.Actividades.LogeoActivity;
import com.example.befit.Entidades.VOTag;
import com.example.befit.R;

public class DialogoInsertTag {

    //Controles
    EditText tbTag;
    Button btnAceptar;
    Button btnCancelar;

    Context Context;

    private DialogoInsertListener interfaz;

    //Interfaz para comunicar con la actividad
    public interface DialogoInsertListener {
        void InsertTag(VOTag tag);
    }

    public DialogoInsertTag(final Context context, DialogoInsertListener actividad) {
        Context = context;
        interfaz = actividad;

        //Configuración del cuadro de díalogo
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialogo_insert_tag);

        //Controles
        tbTag = (EditText) dialog.findViewById(R.id.tbNombreTag);
        btnAceptar = (Button) dialog.findViewById(R.id.btnAceptarTag);
        btnCancelar = (Button) dialog.findViewById(R.id.btnCancelarTag);

        //Eventos
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ComprobarCampo() == true) {
                    VOTag tag = new VOTag();
                    tag.setTag(tbTag.getText().toString().trim());

                    //Mandamos la etiqueta
                    interfaz.InsertTag(tag);
                    dialog.dismiss();
                }
                else {
                    LogeoActivity.centralizarToast(context, context.getString(R.string.introduce_tag));
                }
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

    //Comprobamos que el campo de texto esté relleno
    private boolean ComprobarCampo() {
        return tbTag.getText().toString().trim().length() > 0;
    }
}