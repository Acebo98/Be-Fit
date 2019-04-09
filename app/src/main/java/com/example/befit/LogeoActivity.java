package com.example.befit;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogeoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConectar;
    Button btnFinalizar;
    EditText tbNombre;
    EditText tbContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logeo_activity);

        //IDS
        btnConectar = (Button)findViewById(R.id.btnConectar);
        btnFinalizar = (Button)findViewById(R.id.btnFinalizar);
        tbContra = (EditText)findViewById(R.id.tbContrasena);
        tbNombre = (EditText)findViewById(R.id.tbNombreUsuario);

        //Eventos
        btnConectar.setOnClickListener(this);
        btnFinalizar.setOnClickListener(this);
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
}
