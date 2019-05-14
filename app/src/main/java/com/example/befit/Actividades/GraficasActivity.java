package com.example.befit.Actividades;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.HashMap;

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

public class GraficasActivity extends AppCompatActivity {

    int totalTags;                                  //Número de tags en uso

    PieChart pieChart;                              //Gráfica de espiral
    BarChart barChart;                              //Gráfica de barras

    String[] tags;                                  //Tags (eje X)
    int [] nCuantos;                                //Cuántas tags (eje Y)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_graficas);

            //Botón de ir atrás
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //Gráficas
            pieChart = (PieChart) findViewById(R.id.pieChart);
            barChart = (BarChart) findViewById(R.id.barChart);

            //Leemos las tags
            HashMap<String, Integer> tagsSesiones = new DAOTag(getApplicationContext()).SacarGraficaTags();
            totalTags = new DAOTag(getApplicationContext()).SacarNTagsEnUso(tagsSesiones);
            if (tagsSesiones == null) {
                throw new Exception("error");
            }
            else if (totalTags == -1) {
                throw new Exception("error");
            }
            SacarArreglos(tagsSesiones);                        //Pasamos los datos a vectores
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

    //Convertirmos a arreglos
    public void SacarArreglos(HashMap<String, Integer> tagsSesiones) {
        tags = new String[tagsSesiones.size()];
        nCuantos = new int[tagsSesiones.size()];

        //Recorremos y guardamos en su respectivo arreglo
        int i = 0;
        for (String tag : tagsSesiones.keySet()) {
            tags[i] = tag;
            nCuantos[i] = tagsSesiones.get(tag);
            i++;
        }
    }
}