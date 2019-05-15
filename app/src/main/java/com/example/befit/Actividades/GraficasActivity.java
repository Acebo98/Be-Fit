package com.example.befit.Actividades;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.service.autofill.Dataset;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

public class GraficasActivity extends AppCompatActivity {

    int totalTags;                                  //Número de tags en uso

    PieChart pieChart;                              //Gráfica de espiral
    BarChart barChart;                              //Gráfica de barras

    int[] arrayColores;                           //Colores para las gráficas

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

            //Creamos las tablas
            this.CrearColores();
            this.createCharts();
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

    //Creamos un arreglo de colores aleatorios
    public void CrearColores() {
        arrayColores = new int[tags.length];

        for (int i = 0; i < arrayColores.length; i++) {
            Random rand = new Random();

            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);

            arrayColores[i] = Color.rgb(r, g, b);
        }
    }

    //--------------------------------------GRÁFICAS--------------------------------------\\

    //Estructura de la tabla
    private Chart getSameChart(Chart chart, String descripcion, int textColor, int background, int animateY) {
        chart.getDescription().setText(descripcion);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animateY);
        legend(chart);

        return chart;
    }

    //Leyenda
    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.formColor = arrayColores[i];
            legendEntry.label = tags[i];
            entries.add(legendEntry);
        }
        legend.setCustom(entries);
    }

    //Rellenamos las tablas
    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < tags.length; i++) {
            entries.add(new BarEntry(i, nCuantos[i]));
        }

        return entries;
    }
    private ArrayList<PieEntry> getPieEntries() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < tags.length; i++) {
            entries.add(new PieEntry(nCuantos[i]));
        }

        return entries;
    }

    //Rellenamos el eje X con las etiquetas
    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(tags));
    }

    //Ejes X
    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }
    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    //Creamos las gráficas
    public void createCharts() {
        //Gráfica de tablas
        barChart = (BarChart) getSameChart(barChart, "", Color.BLUE, Color.WHITE, 3000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);
        barChart.setData(getBarData());
        barChart.invalidate();
        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());
        barChart.getLegend().setEnabled(false);

        //Gráfica de sector
        pieChart = (PieChart) getSameChart(pieChart, "", Color.GRAY, Color.WHITE, 3000);
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(12);
        pieChart.setData(getPieData());
        pieChart.invalidate();
    }

    //Datos a mostrar...
    private DataSet getData(DataSet dataSet) {
        //dataSet.setColor(arrayColores);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);

        return dataSet;
    }
    private BarData getBarData() {
        BarDataSet barDataSet = (BarDataSet) getData(new BarDataSet(getBarEntries(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);

        return barData;
    }
    private PieData getPieData() {
        PieDataSet pieDataSet = (PieDataSet) getData(new PieDataSet(getPieEntries(), ""));

        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueFormatter(new PercentFormatter());

        return new PieData(pieDataSet);
    }
}