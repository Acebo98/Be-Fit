package com.example.befit.Dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogoConfirmacion extends DialogFragment {

    private MiDialogListener listener;

    //Interfaz
    public interface MiDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        //Datos del diálogo
        Bundle bundle = getArguments();
        String titulo = bundle.getString("TITULO");
        String mensaje = bundle.getString("MENSAJE");

        //Cuerpo del cuadro de diálogo
        builder.setMessage(mensaje)
                .setTitle(titulo)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(DialogoConfirmacion.this);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();        //No hacer nada
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            Activity act = (Activity) context;
            listener = (MiDialogListener) act;
        }
        catch (Exception err) {
            System.out.print(err.getMessage());
        }
    }
}