package com.example.befit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.befit.Actividades.LogeoActivity;

public class DialogoSeleccion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] tags = getResources().getStringArray(R.array.etiquetas);     //Etiquetas

        //Cuerpo del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Selección")
                .setItems(tags, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        LogeoActivity.centralizarToast(getContext(), tags[item]);
                    }
                });
        return builder.create();
    }
}