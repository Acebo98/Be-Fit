package com.example.befit.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.befit.R;

public class DialogoAlerta extends DialogFragment {
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
                .setPositiveButton(getString(R.string.entendido),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}