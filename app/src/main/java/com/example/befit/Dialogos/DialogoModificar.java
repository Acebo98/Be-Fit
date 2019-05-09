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
import com.example.befit.Entidades.VOSesion;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

import java.util.ArrayList;

public class DialogoModificar {

    //Controles
    Button btnAceptar;
    Button btnCancelar;
    EditText tbNombre;
    EditText tbE1;
    EditText tbE2;
    EditText tbE3;
    EditText tbE4;
    Spinner spnTags;

    //Listas
    String[] tags;
    ArrayList<EditText> lCampos = new ArrayList<>();

    VOSesion Sesion;

    private DialogoModificarListener interfaz;

    //Interfaz para conectar con las actividades
    public interface DialogoModificarListener {
        void AceptarModificar(VOSesion Sesion);
    }

    //Le pasamos la sesión actual que se va a modificar
    public DialogoModificar(final Context context, DialogoModificarListener actividad, VOSesion sesion, String[] Tags) {
        tags = Tags;
        interfaz = actividad;
        Sesion = sesion;

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
        spnTags = (Spinner) dialog.findViewById(R.id.spnMiTag);
        spnTags.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, tags));
        lCampos.add(tbE1);
        lCampos.add(tbE2);
        lCampos.add(tbE3);
        lCampos.add(tbE4);
        lCampos.add(tbNombre);

        //Especificamos los atributos de la sesión original
        tbNombre.setText(Sesion.getNombre());
        tbE1.setText(Sesion.getEjercicio_1());
        tbE2.setText(Sesion.getEjercicio_2());
        tbE3.setText(Sesion.getEjercicio_3());
        tbE4.setText(Sesion.getEjercicio_4());

        //Etiqueta
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals(sesion.getTag())) {
                spnTags.setSelection(i);
                break;
            }
        }

        //Eventos
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ComprobarCampos() ==  true) {
                    try {
                        //Nombre del entrenamiento actual y antiguo
                        String nombreNuevo = tbNombre.getText().toString().trim();
                        String nombreOld = Sesion.getNombre();
                        if (new DAOSesiones(context).ExistirSesion(nombreNuevo, nombreOld) == true) {
                            VOSesion sesion = new VOSesion();

                            //Datos de la sesión
                            sesion.setNombre(tbNombre.getText().toString().trim());
                            sesion.setEjercicio_1(tbE1.getText().toString().trim());
                            sesion.setEjercicio_2(tbE2.getText().toString().trim());
                            sesion.setEjercicio_3(tbE3.getText().toString().trim());
                            sesion.setEjercicio_4(tbE4.getText().toString().trim());
                            sesion.setTag(String.valueOf(spnTags.getSelectedItem()));
                            int idSesion = new DAOTag(context).SacarID(spnTags.getSelectedItem().toString());
                            sesion.setIdTag(idSesion);

                            //Mandamos el temario
                            interfaz.AceptarModificar(sesion);
                            dialog.dismiss();
                        }
                        else {
                            LogeoActivity.centralizarToast(context, context.getString(R.string.sesion_repetida));
                        }
                    }
                    catch (Exception err) {
                        LogeoActivity.centralizarToast(context, err.getMessage());
                    }
                }
                else {
                    LogeoActivity.centralizarToast(context, context.getString(R.string.sesion_5caracteres));
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
            if (campo.getText().toString().trim().length() < 3) {
                return false;
            }
        }
        return true;
    }
}