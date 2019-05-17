package com.example.befit.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.befit.Actividades.MainActivity;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.R;

public class DialogoModifFotos {

    //Controles
    ImageView imageView;
    Button btnModificar;
    Button btnBorrar;

    Context Context;                            //Contexto
    DialogoModifFotosListener interfaz;         //Interfaz

    //Interfaz
    public interface DialogoModifFotosListener {
        void ModificarFoto(int idSesion);
        void BorrarFoto(int idSesion);
    }

    public DialogoModifFotos(Context context, final VOSesion sesion, DialogoModifFotosListener actividad) {
        Context = context;
        interfaz = actividad;

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

        //Aplicamos la foto
        byte[] fotobytes = sesion.getFoto();
        if (fotobytes != null) {
            imageView.setImageBitmap(BytesToPhoto(fotobytes));
        }
        else {
            imageView.setImageResource(R.drawable.no_photo);
        }

        //Eventos
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.BorrarFoto(sesion.getIdentificador());
                dialog.dismiss();
            }
        });
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ModificarFoto(sesion.getIdentificador());
                dialog.dismiss();
            }
        });

        //Mostramos el diálogo con toda su gloria
        dialog.show();
    }

    //Transformamos los bytes a una imagen
    private Bitmap BytesToPhoto(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    //Abrimos la galeria
    private void AbrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    }
}