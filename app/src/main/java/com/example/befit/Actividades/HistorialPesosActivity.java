package com.example.befit.Actividades;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.befit.Adaptadores_LV.AdaptadorLVHistorialPesos;
import com.example.befit.DialogoHistorial;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Entidades.VOPeso;
import com.example.befit.Modelos.DAOPesos;
import com.example.befit.R;

public class HistorialPesosActivity extends AppCompatActivity {

    ListView lvHistorial;                       //Lista del historial
    TextView tbTotal;                           //Campo del total

    AdaptadorLVHistorialPesos adaptadorLV;      //Adaptador LV

    int identificador;                          //Identificador de la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pesos);

        //Id de la sesión
        identificador = getIntent().getExtras().getInt("ID");

        //Listview
        lvHistorial = (ListView)findViewById(R.id.lvHistorialPesos);
        lvHistorial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Adquirimos el peso y lo mostramos...
                VOPeso peso = (VOPeso) adaptadorLV.getItem(position);
                new DialogoHistorial(HistorialPesosActivity.this, peso);
            }
        });

        //Textview con el total
        tbTotal = findViewById(R.id.tbTotalHistorial);

        //Leemos los registros
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
            //Datos de la lista
            adaptadorLV = new AdaptadorLVHistorialPesos(getApplicationContext(),
                    new DAOPesos(getApplicationContext()).SacarHistorialPesos(String.valueOf(identificador)));
            lvHistorial.setAdapter(adaptadorLV);

            //Cantidad de registros
            tbTotal.setText(tbTotal.getText() + " " + String.valueOf(adaptadorLV.getCount()));
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