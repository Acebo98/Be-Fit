package com.example.befit;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PesosActivity extends AppCompatActivity implements DialogoConfirmacion.MiDialogListener {

    int identificador;                      //Identificador de la sesión

    final String BORRADO = "borrar";

    //Controles
    TextView tbNombre;
    TextView tbm1;
    TextView tbm2;
    TextView tbm3;
    TextView tbm4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesos);

        //IDS
        tbNombre = (TextView)findViewById(R.id.labNombreSesion);
        tbm1 = (TextView)findViewById(R.id.labM1);
        tbm2 = (TextView)findViewById(R.id.labM2);
        tbm3 = (TextView)findViewById(R.id.labM3);
        tbm4 = (TextView)findViewById(R.id.labM4);

        //Obtenemos el identificador
        Bundle bundle = getIntent().getExtras();
        identificador = bundle.getInt("ID");

        //Botón de ir atrás
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Leemos la sesión
        LeerSesion();
    }

    //Leemos los datos de la sesión
    private void LeerSesion() {
        try {
            VOSesion sesion = new DAOSesiones(getApplicationContext()).SacarSesion(String.valueOf(identificador));

            //Aplicamos los textos
            tbNombre.setText(sesion.getNombre());
            tbm1.setText(sesion.getMusculo_1());
            tbm2.setText(sesion.getMusculo_2());
            tbm3.setText(sesion.getMusculo_3());
            tbm4.setText(sesion.getMusculo_4());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: this.finish();
            break;
            case R.id.itemBorrar: {
                //Cuadro de diálogo
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", "Borrado");
                bundle.putString("MENSAJE", "¿Estás seguro de que quieres borrar esta sesión?");
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), BORRADO);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pesos, menu);

        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag() == BORRADO) {
            try {
                new DAOSesiones(getApplicationContext()).DeletePeso(String.valueOf(identificador));
                this.finish();
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
}
