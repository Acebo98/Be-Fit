package com.example.befit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class DialogoModificar {

    Button btnAceptar;
    Button btnCancelar;
    EditText tbNombre;
    EditText tbE1;
    EditText tbE2;
    EditText tbE3;
    EditText tbE4;
    ArrayList<EditText> lCampos = new ArrayList<>();

    private DialogoModificarListener interfaz;

    //Interfaz para conectar con las actividades
    public interface DialogoModificarListener {
        void AceptarModificar(VOSesion Sesion);
    }

    public DialogoModificar(final Context context, DialogoModificarListener actividad) {
        interfaz = actividad;

        //Configuración del cuadro de díalogo
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialogomodificarsesion);

        //Controles
        btnAceptar = (Button) dialog.findViewById(R.id.btnModificarSesion);
        btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        tbNombre = (EditText) dialog.findViewById(R.id.tbNNombreSesion);
        tbE1 = (EditText) dialog.findViewById(R.id.tbNEjercicio1);
        tbE2 = (EditText) dialog.findViewById(R.id.tbNEjercicio2);
        tbE3 = (EditText) dialog.findViewById(R.id.tbNEjercicio3);
        tbE4 = (EditText) dialog.findViewById(R.id.tbNEjercicio4);
        lCampos.add(tbE1);
        lCampos.add(tbE2);
        lCampos.add(tbE3);
        lCampos.add(tbE4);
        lCampos.add(tbNombre);

        //Eventos
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ComprobarCampos() ==  true) {
                    try {
                        if (new DAOSesiones(context).ExistirSesion(tbNombre.getText().toString().trim()) == true) {
                            VOSesion sesion = new VOSesion();

                            //Datos de la sesión
                            sesion.setNombre(tbNombre.getText().toString().trim());
                            sesion.setMusculo_1(tbE1.getText().toString().trim());
                            sesion.setMusculo_2(tbE2.getText().toString().trim());
                            sesion.setMusculo_3(tbE3.getText().toString().trim());
                            sesion.setMusculo_4(tbE4.getText().toString().trim());

                            //Mandamos el temario
                            interfaz.AceptarModificar(sesion);
                            dialog.dismiss();
                        }
                        else {
                            LogeoActivity.centralizarToast(context, "Parece que ya tienes una sesión con " +
                                    "dicho nombre ya insertada");
                        }
                    }
                    catch (Exception err) {
                        LogeoActivity.centralizarToast(context, err.getMessage());
                    }
                }
                else {
                    LogeoActivity.centralizarToast(context, "Los campos de texto deben de tener mínimo 5 carácteres");
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

    //Comprobamos los campos rellenos
    private boolean ComprobarCampos() {
        for (EditText campo : lCampos) {
            if (campo.getText().toString().trim().length() < 5) {
                return false;
            }
        }
        return true;
    }
}