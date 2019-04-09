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

        //Leemos las preferencias
        LeerPreferencias();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConectar: {

            }
            break;
            case R.id.btnFinalizar: {
                this.finish();
            }
            break;
        }
    }

    //Leemos las preferencias y vemos si el usuario ya está registrado
    private void LeerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);

        //Si leemos el usuario y es null significa que es la primera vez que no estamos registrados
        String nombreUsuario = preferences.getString("USUARIO", null);
        if (nombreUsuario == null) {
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
}
