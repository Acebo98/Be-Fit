package com.example.befit.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.befit.Ayudas.Pantalla;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.R;

public class LogeoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConectar;
    Button btnFinalizar;
    EditText tbNombre;
    EditText tbContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      //No rotar la actividad!!!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeo);

        //IDS
        btnConectar = (Button)findViewById(R.id.btnConectar);
        btnFinalizar = (Button)findViewById(R.id.btnFinalizar);
        tbContra = (EditText)findViewById(R.id.tbContrasena);
        tbNombre = (EditText)findViewById(R.id.tbNombreUsuario);

        //Eventos
        btnConectar.setOnClickListener(this);
        btnFinalizar.setOnClickListener(this);

        //Comprobamos el incio de sesión automático. En caso contrario vemos si está el usuario registrado
        if (ComprobarInicioAutomatico() == true) {
            IrPantallaPrincipal();
        }
        else {
            ShowRegister();
        }

        //Miramos si el usuario está registrado o para cambiar el texto del botón
        if (IsUserRegistered() == true) {
            btnConectar.setText(getString(R.string.conectar));
        }
        else {
            btnConectar.setText(getString(R.string.registrarse));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConectar: {
                try {
                    //Usuario y contraseña
                    String usuario = tbNombre.getText().toString().trim();
                    String contra = tbContra.getText().toString().trim();

                    if (IsUserRegistered() == true) {
                        if (IsAbleToConnect(usuario, contra) == true) {
                            IrPantallaPrincipal();
                        }
                        else {
                            centralizarToast(getApplicationContext(), getString(R.string.usuario_incorrecto));
                        }
                    }
                    else {
                        //Si no se ha registrado vemos si los datos son correctos
                        if (IsDataCorrect(usuario, contra) == true) {
                            RegistrarUsuario(usuario, contra);
                            IrPantallaPrincipal();
                            btnConectar.setText(getString(R.string.conectar));
                        }
                        else {
                            centralizarToast(getApplicationContext(), getString(R.string.cuatro_caracteres));
                        }
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
            break;
            case R.id.btnFinalizar: {
                this.finish();
            }
            break;
        }
    }

    //Nos dirimos a la patanda principal
    private void IrPantallaPrincipal() {
        Intent intent = new Intent(LogeoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Comprobamos que el usuario esté registrado
    private boolean IsUserRegistered() {
        SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        boolean vof = true;

        //Leemos de las preferencias
        String nombreUsuario = preferences.getString("USUARIO", null);
        if (nombreUsuario == null) {
            vof = false;
        }

        return vof;
    }

    //Comprobamos que sea correcto que el usuario se pueda registrar
    private boolean IsDataCorrect(String usuario, String contrasena) {
        return usuario.length() >= 4 && contrasena.length() >= 4;
    }

    //Leemos las preferencias y vemos si el usuario ya está registrado
    private void ShowRegister() {
        if (IsUserRegistered() == false) {
            DialogFragment dialogFragment = new DialogoAlerta();
            Bundle bundle = new Bundle();

            bundle.putString("TITULO", getString(R.string.parece_nuevo));
            bundle.putString("MENSAJE", getString(R.string.explicacion_registro));
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getSupportFragmentManager(), "1111");
        }
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

    //Vemos si podemos conectarnos
    private boolean IsAbleToConnect(String usuario, String contra) {
        SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        boolean vof = false;

        //Miramos si la contraseña introducida y el nombre de usuario son los mismos que los almacenados en
        //las preferencias
        if (preferences.getString("USUARIO", null).equals(usuario) &&
                preferences.getString("CONTRASENA", null).equals(contra)) {
            vof = true;
        }

        return vof;
    }

    //Registramos al usuario
    private void RegistrarUsuario(String nombre, String contrasena) {
        SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("USUARIO", nombre);
        editor.putString("CONTRASENA", contrasena);
        editor.commit();
    }

    //Centralizamos los Toast
    public static void centralizarToast(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}