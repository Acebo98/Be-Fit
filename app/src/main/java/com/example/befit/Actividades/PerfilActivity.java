package com.example.befit.Actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Dialogos.DialogoConfirmacion;
import com.example.befit.Modelos.DAOPesos;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfilActivity extends AppCompatActivity implements DialogoConfirmacion.MiDialogListener {

    EditText tbNombre;
    EditText tbContra;
    TextView labModificacion;
    TextView labEntrenamientos;
    Switch switchModificar;

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
        labEntrenamientos = (TextView)findViewById(R.id.labNEntrenamientos);
        labModificacion = (TextView)findViewById(R.id.labUpdate);
        switchModificar = (Switch)findViewById(R.id.switchAutomatico);
        switchModificar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    //Indicamos mediante el estado del switch
                    editor.putBoolean("AUTOMATICO", isChecked);
                    editor.commit();
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

        //Miramos si está el incio de sesión automático para actualizar el estado del botón al incio
        switchModificar.setChecked(ComprobarInicioAutomatico());

        //Leemos los datos del usuario
        ReadUserData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home: {
                    this.finish();
                }
                break;
                case R.id.itemModificarPerfil: {
                    String usuario = tbNombre.getText().toString().trim();
                    String contra = tbContra.getText().toString().trim();
                    if (IsDataCorrect(usuario, contra) == true) {
                        //Cuadro de diálogo
                        DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                        Bundle bundle = new Bundle();
                        bundle.putString("TITULO", getString(R.string.modificacion));
                        bundle.putString("MENSAJE", getString(R.string.pregunta_modificar));
                        dialogoConfirmacion.setArguments(bundle);
                        dialogoConfirmacion.show(getSupportFragmentManager(), MODIF);
                    } else {
                        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.cuatro_caracteres));
                    }
                }
                break;
                case R.id.itemDeleteFitness: {
                    //Cuadro de diálogo
                    DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                    Bundle bundle = new Bundle();
                    bundle.putString("TITULO", getString(R.string.borrado));
                    bundle.putString("MENSAJE", getString(R.string.pregunta_borrar_sesiones));
                    dialogoConfirmacion.setArguments(bundle);
                    dialogoConfirmacion.show(getSupportFragmentManager(), BORRADO);
                }
                break;
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);

        return true;
    }

    //Leemos los datos existentes del usuario
    private void ReadUserData() {
        try {
            SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);

            //Datos
            String nombre = preferences.getString("USUARIO", null);
            String contrasena = preferences.getString("CONTRASENA", null);
            String modificacion = preferences.getString("MODIFICACION", null);
            int cantidad = new DAOSesiones(getApplicationContext()).ReadSesiones().size();

            //Asignamos
            tbNombre.setText(nombre);
            tbContra.setText(contrasena);
            if (modificacion == null) {
                labModificacion.setText(getString(R.string.ultima_modificacion) + " N/A");
            }
            else {
                labModificacion.setText(getString(R.string.ultima_modificacion) + " " + modificacion);
            }
            labEntrenamientos.setText(getString(R.string.n_entrenamientos) + " " + String.valueOf(cantidad));
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
            LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.datos_modificados));
            ReadUserData();
        }
        catch (Exception err) {
            LogeoActivity.centralizarToast(getApplicationContext(), err.getMessage());
        }
    }

    //Comprobamos que sea correcto que el usuario se pueda registrar (Como en la actividad de logeo)
    private boolean IsDataCorrect(String usuario, String contrasena) {
        return usuario.length() >= 4 && contrasena.length() >= 4;
    }

    //Comprobamos si está activo el inicio automático de sesión
    private boolean ComprobarInicioAutomatico() {
        SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        boolean automatico = true;

        try {
            automatico = preferences.getBoolean("AUTOMATICO", false);
        }
        catch (Exception err) {
            DialogFragment dialogFragment = new DialogoAlerta();
            Bundle bundle = new Bundle();

            bundle.putString("TITULO", getString(R.string.error));
            bundle.putString("MENSAJE", err.getMessage());
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getSupportFragmentManager(), "error");
            automatico = false;
        }

        return automatico;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag() == MODIF) {
            //Modificamos los datos del usuario
            String usuario = tbNombre.getText().toString().trim();
            String contra = tbContra.getText().toString().trim();
            ModifyData(usuario, contra);
        }
        else if (dialog.getTag() == BORRADO) {
            try {
                //Borramos las sesiones
                new DAOSesiones(getApplicationContext()).DeleteSesiones();
                new DAOPesos(getApplicationContext()).DeletePeso();
                ReadUserData();
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.sesiones_borradas));

                //Resultado OK
                setResult(RESULT_OK);
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
    }
}