package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.befit.Actividades.LogeoActivity;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.Entidades.VOTag;
import com.example.befit.R;

public class DialogoModificarTag {

    Context context;                                    //Contexto

    //Controles
    EditText tbTag;
    Button btnModificar;
    Button btnBorrar;

    private DialogoModificarTagListener interfaz;      //Interfaz propio

    //Interfaz
    public interface DialogoModificarTagListener {
        void Modificar(VOTag Tag);
        void Borrar (VOTag Tag);
    }

    public DialogoModificarTag(Context Context, final VOTag Tag, DialogoModificarTagListener actividad) {
        context = Context;
        interfaz = actividad;

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
                if (ComprobarCampo() == true) {
                    if (ComprobarCambioTag(Tag) == false) {
                        VOTag tag = new VOTag(Tag.getIdentificador(), tbTag.getText().toString().trim());

                        //Modificamos la tag mediante la interfaz
                        interfaz.Modificar(tag);
                        dialog.dismiss();
                    }
                    else {
                        LogeoActivity.centralizarToast(context, context.getString(R.string.tag_igual));
                    }
                }
                else {
                    LogeoActivity.centralizarToast(context, context.getString(R.string.introduce_tag));
                }
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.Borrar(Tag);
                dialog.dismiss();
            }
        });

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }

    //Comprobamos si el nombre de la nueva tag es el mismo que original
    public boolean ComprobarCambioTag(VOTag tag) {
        return tbTag.getText().toString().trim().equals(tag.getTag());
    }

    //Comprobamos que el campo de texto esté relleno
    private boolean ComprobarCampo() {
        return tbTag.getText().toString().trim().length() > 0;
    }
}