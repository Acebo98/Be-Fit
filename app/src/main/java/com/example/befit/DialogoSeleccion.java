package com.example.befit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.befit.Actividades.LogeoActivity;
import com.example.befit.Actividades.MainActivity;

public class DialogoSeleccion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] tags = getResources().getStringArray(R.array.etiquetas);     //Etiquetas

        //Cuerpo del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.etiqueta_sesion))
                .setItems(tags, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.introducirEtiqueta(tags[item]);
                    }
                });
        return builder.create();
    }
}