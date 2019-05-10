package com.example.befit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.befit.Entidades.VOSesion;
import com.example.befit.Entidades.VOTag;

public class DialogoModificarTag {

    Context context;                                //Contexto

    //Controles
    EditText tbTag;
    Button btnModificar;
    Button btnBorrar;

    public DialogoModificarTag(Context Context, VOTag Tag) {
        context = Context;

        //Configuración del cuadro de díalogo
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialogo_modificar_tag);

        //Controles
        tbTag = (EditText) dialog.findViewById(R.id.tbNombreTag);
        btnBorrar = (Button) dialog.findViewById(R.id.btnBorrarTag);
        btnModificar = (Button) dialog.findViewById(R.id.btnModificarTag);

        //Eventos
        tbTag.setText(Tag.getTag());
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
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