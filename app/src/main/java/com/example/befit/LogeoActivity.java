package com.example.befit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogeoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConectar;
    Button btnFinalizar;
    EditText tbNombre;
    EditText tbContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logeo_activity);

        //Mensaje de bienvenida
        Toast.makeText(getApplicationContext(), "Bienvenid@", Toast.LENGTH_SHORT).show();

        //IDS
        btnConectar = (Button)findViewById(R.id.btnConectar);
        btnFinalizar = (Button)findViewById(R.id.btnFinalizar);
        tbContra = (EditText)findViewById(R.id.tbContrasena);
        tbNombre = (EditText)findViewById(R.id.tbNombreUsuario);

        //Eventos
        btnConectar.setOnClickListener(this);
        btnFinalizar.setOnClickListener(this);

        //Mostramos un cuadro de diálogo al usuario si no está registrado
        ShowRegister();
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

                        }
                        else {
                            centralizarToast(getApplicationContext(),
                                    "Nombre de Usuario o Contraseña incorrectos");
                        }
                    }
                    else {
                        //Si no se ha registrado vemos si los datos son correctos
                        if (IsDataCorrect(usuario, contra) == true) {
                            RegistrarUsuario(usuario, contra);
                        }
                        else {
                            centralizarToast(getApplicationContext(), "El nombre y la contraseña deben " +
                                    "de tener 4 caractéres como mínimo");
                        }
                    }
                }
                catch (Exception err) {
                    centralizarToast(getApplicationContext(), err.getMessage());
                }
            }
            break;
            case R.id.btnFinalizar: {
                this.finish();
            }
            break;
        }
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

            bundle.putString("TITULO", "¡Parece que eres nuevo!");
            bundle.putString("MENSAJE", "Al ser la primera vez que ejecutas la aplicación debes de " +
                    "registrarte introduciendo un nombre y una contraseña. \n\nCuando quieras volver a conectarte " +
                    "deberás de introducir dichos datos otra vez. \n\nNo te preocupes, dentro de la aplicación " +
                    "podrás cambiarlos.");
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getSupportFragmentManager(), "1111");
        }
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
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }
}
