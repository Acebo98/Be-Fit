package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.befit.R;

public class DialogoModifFotos {

    //Controles
    ImageView imageView;
    Button btnModificar;
    Button btnBorrar;

    Context Context;

    public DialogoModifFotos(Context context) {
        Context = context;

        //Configuración del cuadro de díalogo
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialogo_modif_fotos);

        //Controles
        imageView = (ImageView) dialog.findViewById(R.id.imageViewModifFoto);
        btnBorrar = (Button) dialog.findViewById(R.id.btnBorrarFoto);
        btnModificar = (Button) dialog.findViewById(R.id.btnModificarFoto);

        //Eventos
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }
}