package com.example.befit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener, DialogoConfirmacion.MiDialogListener {

    EditText tbNombre;
    EditText tbContra;
    Button btnModificar;
    Button btnBorrar;
    TextView labModificacion;
    TextView labEntrenamientos;

    final String MODIF = "modif";
    final String BORRADO = "borrar";

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

        //Eventos
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);

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
                labModificacion.setText("Última Modificación: N/A");
            }
            else {
                labModificacion.setText("Última Modificación: " + modificacion);
            }
        }
        catch (Exception err) {
            LogeoActivity.centralizarToast(getApplicationContext(), err.getMessage());
        }
    }

    //Modificamos los datos del usuario
    private void ModifyData(String nombre, String contra) {
        try {
            SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            //Modificamos
            editor.putString("USUARIO", nombre);
            editor.putString("CONTRASENA", contra);

            //Fecha actual
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            editor.putString("MODIFICACION", simpleDateFormat.format(new Date()));

            //Guardamos
            editor.commit();
            LogeoActivity.centralizarToast(getApplicationContext(), "Datos modificados correctamente");
            ReadUserData();
        }
        catch (Exception err) {
            LogeoActivity.centralizarToast(getApplicationContext(), err.getMessage());
        }
    }

    //Borramos los datos del usuario
    private void DeleteData() {
        try {
            SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            //Borramos todos los datos
            editor.putString("USUARIO", null);
            editor.putString("CONTRASENA", null);
            editor.putString("MODIFICACION", null);
            editor.commit();

            //Volvemos a la actividad de logeo
            LogeoActivity.centralizarToast(getApplicationContext(), "Datos borrados satisfactoriamente");
            Intent intent = new Intent(PerfilActivity.this, LogeoActivity.class);
            startActivity(intent);
        }
        catch (Exception err) {
            LogeoActivity.centralizarToast(getApplicationContext(), err.getMessage());
        }
    }

    //Comprobamos que sea correcto que el usuario se pueda registrar (Como en la actividad de logeo)
    private boolean IsDataCorrect(String usuario, String contrasena) {
        return usuario.length() >= 4 && contrasena.length() >= 4;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnBorrar: {

                    //Cuadro de diálogo
                    DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                    Bundle bundle = new Bundle();
                    bundle.putString("TITULO", "Borrado");
                    bundle.putString("MENSAJE", "¿Estás seguro de que quieres borrar tus datos?");
                    dialogoConfirmacion.setArguments(bundle);
                    dialogoConfirmacion.show(getSupportFragmentManager(), BORRADO);
                }
                break;
                case R.id.btnModificar: {
                    String usuario = tbNombre.getText().toString().trim();
                    String contra = tbContra.getText().toString().trim();
                    if (IsDataCorrect(usuario, contra) == true) {

                        //Cuadro de diálogo
                        DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", "Modificación");
                        bundle.putString("MENSAJE", "¿Estás seguro de que quieres modificar tus datos?");
                        dialogoConfirmacion.setArguments(bundle);
                        dialogoConfirmacion.show(getSupportFragmentManager(), MODIF);
                    }
                    else {
                        LogeoActivity.centralizarToast(getApplicationContext(), "Recuerda de que el nombre de " +
                                "usuario y la contraseña deben de tener 4 carácteres como mínimo");
                    }
                }
                break;
            }
        }
        catch (Exception err) {
            LogeoActivity.centralizarToast(getApplicationContext(), err.getMessage());
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag() == MODIF) {
            String usuario = tbNombre.getText().toString().trim();
            String contra = tbContra.getText().toString().trim();
            ModifyData(usuario, contra);
        }
        else if (dialog.getTag() == BORRADO) {
            DeleteData();
        }
    }
}