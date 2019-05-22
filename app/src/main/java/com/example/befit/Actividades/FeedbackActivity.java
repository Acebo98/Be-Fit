package com.example.befit.Actividades;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.befit.Modelos.DAOFirebase;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Entidades.VOFeedback;
import com.example.befit.R;

public class FeedbackActivity extends AppCompatActivity {

    //Controles
    EditText tbTitulo;
    EditText tbMensaje;
    Spinner spnTipo;
    Button btnEnviar;

    String[] lReportes;                         //Arreglo con los tipos de reporte

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //Botón de ir atrás
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //IDS
        tbTitulo = (EditText) findViewById(R.id.tbTituloOpinion);
        tbMensaje = (EditText) findViewById(R.id.tbCuerpoOpinion);
        spnTipo = (Spinner) findViewById(R.id.spnTipoOpinion);
        btnEnviar = (Button) findViewById(R.id.btnEnviarOpinion);

        //Spinner
        lReportes = new String[] {getString(R.string.opinion_bug), getString(R.string.opinion_dar)};
        spnTipo.setAdapter(new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, lReportes));

        //Evento de enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ComprobarCamposRellenos() == true) {
                        VOFeedback feedback = new VOFeedback();

                        //Datos del feedback
                        feedback.setTitulo(tbTitulo.getText().toString().trim());
                        feedback.setCuerpo(tbMensaje.getText().toString().trim());
                        feedback.setTipo(spnTipo.getSelectedItem().toString());

                        //Realizamos la insercción
                        new DAOFirebase().insertarReporte(feedback);
                        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.gracias_opinion));
                        LimpiarCampos();
                    }
                    else {
                        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.campos_opiniones));
                    }
                }
                catch (Exception err) {
                    DialogFragment dialogFragment = new DialogoAlerta();
                    Bundle bundle = new Bundle();

                    bundle.putString("TITULO", getString(R.string.error));
                    bundle.putString("MENSAJE", err.getMessage());
                    dialogFragment.setArguments(bundle);

                    dialogFragment.show(getSupportFragmentManager(), "error");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Comprobamos que los campos estén rellenos
    private boolean ComprobarCamposRellenos() {
        return tbMensaje.getText().toString().trim().length() > 0 && tbTitulo.getText().toString().trim().length() > 0;
    }

    //Limpiamos los campos de texto
    private void LimpiarCampos() {
        tbTitulo.getText().clear();
        tbMensaje.getText().clear();
    }
}