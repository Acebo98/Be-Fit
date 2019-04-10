package com.example.befit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {

    EditText tbNombre;
    EditText tbContra;
    Button btnModificar;
    Button btnBorrar;
    TextView labModificacion;
    TextView labEntrenamientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Botón de ir atrás
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //IDS
        tbContra = (EditText)findViewById(R.id.tbMiContra);
        tbNombre = (EditText)findViewById(R.id.tbMiNombre);
        btnBorrar = (Button)findViewById(R.id.btnBorrar);
        btnModificar = (Button)findViewById(R.id.btnModificar);
        labEntrenamientos = (TextView)findViewById(R.id.labNEntrenamientos);
        labModificacion = (TextView)findViewById(R.id.labUpdate);

        //Leemos los datos del usuario
        ReadUserData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Leemos los datos existentes del usuario
    private void ReadUserData() {
        try {
            SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);

            //Datos
            String nombre = preferences.getString("USUARIO", null);
            String contrasena = preferences.getString("CONTRASENA", null);
            String modificacion = preferences.getString("MODIFICACION", null);

            //Asignamos
            tbNombre.setText(nombre);
            tbContra.setText(contrasena);
            if (modificacion == null) {
                labModificacion.setText(labModificacion.getText() + " N/A");
            }
            else {
                labModificacion.setText(labModificacion.getText() + " " + modificacion);
            }
        }
        catch (Exception err) {
            LogeoActivity.centralizarToast(getApplicationContext(), err.getMessage());
        }
    }
}