package com.example.befit.Actividades;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.HashMap;

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

public class GraficasActivity extends AppCompatActivity {

    HashMap<String, Integer> tagsSesiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_graficas);

            //Botón de ir atrás
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //Leemos las tags
            tagsSesiones = new DAOTag(getApplicationContext()).SacarGraficaTags();
            if (tagsSesiones == null) {
                throw new Exception("error");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
