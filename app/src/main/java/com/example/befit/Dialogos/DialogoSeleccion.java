package com.example.befit.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.befit.Actividades.LogeoActivity;
import com.example.befit.Actividades.MainActivity;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

public class DialogoSeleccion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] tags;                                                        //Etiquetas
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());       //Dialogo

        try {
            tags = new DAOTag(getContext()).SacarArrayTags();

            //Cuerpo del diálogo
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
        }
        catch (Exception err) {
            DialogFragment dialogFragment = new DialogoAlerta();
            Bundle bundle = new Bundle();

            bundle.putString("TITULO", getString(R.string.error));
            bundle.putString("MENSAJE", err.getMessage());
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getFragmentManager(), "error");
        }

        return builder.create();
    }
}