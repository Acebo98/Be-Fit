package com.example.befit;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class HistorialPesosActivity extends AppCompatActivity {

    ListView lvHistorial;                       //Lista del historial

    AdaptadorLVHistorialPesos adaptadorLV;      //Adaptador LV

    int identificador;                          //Identificador de la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pesos);

        //Id de la sesión
        identificador = getIntent().getExtras().getInt("ID");

        //Litview
        lvHistorial = (ListView)findViewById(R.id.lvHistorialPesos);
        LeerHistorial();

        //Botón de ir atrás
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Leemos el historial
    private void LeerHistorial() {
        try {
            adaptadorLV = new AdaptadorLVHistorialPesos(getApplicationContext(),
                    new DAOPesos(getApplicationContext()).SacarHistorialPesos(String.valueOf(identificador)));
            lvHistorial.setAdapter(adaptadorLV);
        }
        catch (Exception err) {
            DialogFragment dialogFragment = new DialogoAlerta();
            Bundle bundle = new Bundle();

            bundle.putString("TITULO", "Ha ocurrido un Error");
            bundle.putString("MENSAJE", err.getMessage());
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getSupportFragmentManager(), "error");
        }
    }
}