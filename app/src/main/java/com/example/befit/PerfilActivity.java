package com.example.befit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener, DialogoConfirmacion.MiDialogListener {

    EditText tbNombre;
    EditText tbContra;
    Button btnModificar;
    Button btnBorrar;
    TextView labModificacion;
    TextView labEntrenamientos;

    final String MODIF = "modif";

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

    //Comprobamos que sea correcto que el usuario se pueda registrar (Como en la actividad de logeo)
    private boolean IsDataCorrect(String usuario, String contrasena) {
        return usuario.length() >= 4 && contrasena.length() >= 4;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnBorrar: {

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

        }
    }
}