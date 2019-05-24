package com.example.befit.Actividades;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Dialogos.DialogoConfirmacion;
import com.example.befit.Dialogos.DialogoModifFotos;
import com.example.befit.Dialogos.DialogoModificar;
import com.example.befit.Entidades.VOPeso;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.Modelos.DAOPesos;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PesosActivity extends AppCompatActivity implements DialogoConfirmacion.MiDialogListener,
        DialogoModificar.DialogoModificarListener, DialogoModifFotos.DialogoModifFotosListener {

    int identificador;                      //Identificador de la sesión

    final String BORRADO = "borrar";        //Constante para el borrado
    final String BLOQUEADO = "bloquear";    //Constante para el bloqueo

    Context context;                        //Contexto para el diálogo personalizado

    VOSesion sesion;                        //Sesión en pantalla

    DialogoModifFotos dialogoModifFotos;    //Dialogo para modificar fotos (necesitamos su instancia)

    //Controles
    TextView tbNombre;
    TextView labm1;
    TextView labm2;
    TextView labm3;
    TextView labm4;
    EditText tbP1;
    EditText tbP2;
    EditText tbP3;
    EditText tbP4;
    EditText tbNotas;
    ArrayList<EditText> lCampos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesos);
        context = this;

        //IDS
        tbNombre = (TextView)findViewById(R.id.labNombreSesion);
        labm1 = (TextView)findViewById(R.id.labM1);
        labm2 = (TextView)findViewById(R.id.labM2);
        labm3 = (TextView)findViewById(R.id.labM3);
        labm4 = (TextView)findViewById(R.id.labM4);
        tbP1 = (EditText)findViewById(R.id.tbMiEjercicio1);
        tbP2 = (EditText)findViewById(R.id.tbMiEjercicio2);
        tbP3 = (EditText)findViewById(R.id.tbMiEjercicio3);
        tbP4 = (EditText)findViewById(R.id.tbMiEjercicio4);
        tbNotas = (EditText)findViewById(R.id.tbNotas);

        //Campos de texto
        lCampos.add(tbP1);
        lCampos.add(tbP2);
        lCampos.add(tbP3);
        lCampos.add(tbP4);

        //Obtenemos el identificador
        Bundle bundle = getIntent().getExtras();
        identificador = bundle.getInt("ID");

        //Botón de ir atrás
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Leemos la sesión y sus pesos
        LeerSesion();
        LeerPesos();
    }

    //Leemos los datos de la sesión
    private void LeerSesion() {
        try {
            sesion = new DAOSesiones(getApplicationContext()).SacarSesion(String.valueOf(identificador));

            //Aplicamos los textos
            tbNombre.setText(sesion.getNombre());
            labm1.setText(sesion.getEjercicio_1());
            labm2.setText(sesion.getEjercicio_2());
            labm3.setText(sesion.getEjercicio_3());
            labm4.setText(sesion.getEjercicio_4());
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

    //Leemos los datos de los pesos
    public void LeerPesos() {
        try {
            VOPeso peso = new DAOPesos(getApplicationContext()).SacarPeso(identificador);

            //Mostramos los pesos
            tbP1.setText(peso.getPeso_1());
            tbP2.setText(peso.getPeso_2());
            tbP3.setText(peso.getPeso_3());
            tbP4.setText(peso.getPeso_4());
            tbNotas.setText(peso.getNotas());
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

    //Comprobamos que los 4 campos de pesos estén cubiertos
    private boolean ComprobarCampos() {
        for (EditText campo : lCampos) {
            if (campo.getText().toString().trim().length() == 0) {
                return false;
            }
        }
        return true;
    }

    //Convertimos la imagen en un array de bytes
    private byte[] ImageToBytes(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    //Abrimos la galería
    private void AbrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, MainActivity.GALERIA);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: this.finish();
            break;
            case R.id.itemModificar: {
                try {
                    VOSesion NSesion = new VOSesion();

                    //Datos de la sesion
                    NSesion.setNombre(tbNombre.getText().toString().trim());
                    NSesion.setEjercicio_1(labm1.getText().toString().trim());
                    NSesion.setEjercicio_2(labm2.getText().toString().trim());
                    NSesion.setEjercicio_3(labm3.getText().toString().trim());
                    NSesion.setEjercicio_4(labm4.getText().toString().trim());
                    NSesion.setIdTag(sesion.getIdTag());

                    //Iniciamos el diálogo personalizado
                    final String[] tags = new DAOTag(getApplicationContext()).SacarArrayTags();
                    if (tags != null) {
                        new DialogoModificar(context, PesosActivity.this, NSesion, tags);
                    }
                    else {
                        throw new Exception("error");
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
            case R.id.itemBorrar: {
                //Cuadro de diálogo
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", getString(R.string.borrado));
                bundle.putString("MENSAJE", getString(R.string.pregunta_borrar));
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), BORRADO);
            }
            break;
            case R.id.itemActualizar: {
                try {
                    if (ComprobarCampos() == true) {
                        VOPeso Npeso = new VOPeso();
                        Npeso.setPeso_1(tbP1.getText().toString().trim());
                        Npeso.setPeso_2(tbP2.getText().toString().trim());
                        Npeso.setPeso_3(tbP3.getText().toString().trim());
                        Npeso.setPeso_4(tbP4.getText().toString().trim());
                        Npeso.setNotas(tbNotas.getText().toString());           //Opcional
                        Npeso.setIdSesion(identificador);

                        //Actualizamos
                        new DAOPesos(getApplicationContext()).InsertarPeso(Npeso);
                        new DAOSesiones(getApplicationContext()).UpdateFecha(identificador);
                        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.pesos_actualizados));

                        //Resultado ok
                        setResult(RESULT_OK);
                    }
                    else {
                        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.pesos_obligatorios));
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
            case R.id.itemHistorial: {
                //Pasamos el ID de la sesión
                Bundle bundle = new Bundle();
                bundle.putInt("ID", identificador);

                Intent intent = new Intent(PesosActivity.this, HistorialPesosActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
            case R.id.itemBloquear: {
                //Cuadro de diálogo
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                Bundle bundle = new Bundle();
                bundle.putString("TITULO", getString(R.string.bloqueo_sesion));
                bundle.putString("MENSAJE", getString(R.string.pregunta_desbloquear));
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), BLOQUEADO);
            }
            break;
            case R.id.itemPhoto: {
                //Díalodo donde se modifica o borra la foto
                dialogoModifFotos = new DialogoModifFotos(context, sesion, PesosActivity.this);
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.GALERIA) {
            if (resultCode == RESULT_OK) {
                if (dialogoModifFotos != null) {
                    try {
                        //Accedemos a la foto del dialogo personalizado y la cambiamos...
                        Uri imageUri = data.getData();
                        dialogoModifFotos.getImageView().setImageURI(imageUri);

                        //Pasamos a bytes y modificamos
                        byte[] fotoBytes = ImageToBytes(dialogoModifFotos.getImageView());
                        new DAOSesiones(getApplicationContext()).ModificarFoto(identificador, fotoBytes);
                        sesion.setFoto(fotoBytes);          //Modificamos la foto en la propiedad
                        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.foto_modif));
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MainActivity.PERMISO_ALMACEN) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AbrirGaleria();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag() == BORRADO) {
            try {
                //Borramos la sesión y su peso asociado
                new DAOSesiones(getApplicationContext()).DeleteSesion(String.valueOf(identificador));
                new DAOPesos(getApplicationContext()).DeletePeso(String.valueOf(identificador));

                //Indicamos el resultado de que se ha borrado el entrenamiento
                setResult(RESULT_OK);
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.sesion_borrada));
                this.finish();
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
        else if (dialog.getTag() == BLOQUEADO) {
            try {
                //Modificamos...
                new DAOSesiones(getApplicationContext()).BloquearSesion(String.valueOf(identificador));
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.sesion_bloqueada));

                //Cerramos
                setResult(RESULT_OK);
                this.finish();
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

    @Override
    public void AceptarModificar(VOSesion Sesion) {
        try {
            //Id de la sesión
            Sesion.setIdentificador(identificador);
            new DAOSesiones(context).UpdateSesion(Sesion);

            //Actualizamos el interfaz e informamos
            LogeoActivity.centralizarToast(context, getString(R.string.sesion_modificada));
            LeerSesion();
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

    @Override
    public void ModificarFoto() {
        //Permisos...
        if (ContextCompat.checkSelfPermission(PesosActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PesosActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.PERMISO_ALMACEN);
        }
        else if (ContextCompat.checkSelfPermission(PesosActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            AbrirGaleria();
        }
    }

    @Override
    public void BorrarFoto(int idSesion) {
        try {
            new DAOSesiones(getApplicationContext()).BorrarFoto(idSesion);
            sesion.setFoto(null);      //Indicamos que no hay foto
            LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.foto_borrada));
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