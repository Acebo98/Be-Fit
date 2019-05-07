package com.example.befit.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.befit.Actividades.LogeoActivity;
import com.example.befit.Actividades.MainActivity;
import com.example.befit.R;

public class DialogoSeleccion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Etiquetas
        final String[] tags = new String[] {getString(R.string.simple), getString(R.string.moderado),
                getString(R.string.complicado), getString(R.string.cardio), getString(R.string.tag_pierna),
                getString(R.string.tren_superior)};

        //Cuerpo del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.etiqueta_sesion))
                .setItems(tags, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Tag de la llamada
                        switch (getTag()) {
                            case "seleccionar": {
                                MainActivity activity = (MainActivity) getActivity();
                                activity.introducirEtiqueta(tags[item]);
                            }
                            break;
                        }
                    }
                });
        return builder.create();
    }
}