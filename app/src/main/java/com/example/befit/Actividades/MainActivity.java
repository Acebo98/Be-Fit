package com.example.befit.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.befit.Ayudas.ConversorFotos;
import com.example.befit.Ayudas.Pantalla;
import com.example.befit.Dialogos.DialogoModificarTag;
import com.example.befit.Dialogos.DialogoInsertTag;
import com.example.befit.Dialogos.DialogoSeleccion;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Dialogos.DialogoConfirmacion;
import com.example.befit.Entidades.VOPeso;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.Entidades.VOTag;
import com.example.befit.Modelos.DAOPesos;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity implements SesionesFragment.OnFragmentInteractionListener,
        NSesionFragment.OnFragmentInteractionListener, DialogoConfirmacion.MiDialogListener,
        TagsFragment.OnFragmentInteractionListener, DialogoInsertTag.DialogoInsertListener, DialogoModificarTag.DialogoModificarTagListener {

    Context context;

    //Constantes para las llamadas
    static final String CERRAR = "cerrar";
    static final String SELECCIONAR = "seleccionar";
    static int ACTUALIZAR = 1111;
    static int GALERIA = 2019;
    static int PERMISO_ALMACEN = 1;

    //Booleana para especificar que se va a añadir una foto
    boolean fotoAñadir;

    //Constante para el mínimo ancho de la pantalla
    final int MIN_ALTO_PANTALLA = 2000;

    //FloatingButtons
    FloatingActionButton floatingAdd;
    FloatingActionButton floatingErase;

    //INSTANCIAS DE LOS FRAGMENTOS A UTILIZAR
    SesionesFragment sesionesFragment;
    NSesionFragment nSesionFragment;
    TagsFragment tagsFragment;

    //Tablayout
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      //No rotar la actividad!!!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nos conectamos con firebase
        FirebaseApp.initializeApp(getApplicationContext());

        //No se añade foto al principio
        fotoAñadir = false;

        //Contexto
        context = this;

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //PageAdapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Tablayout
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_dumbell);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tag);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_sesion);

        //Evento para mostrar el floatingbutton
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: {
                        floatingAdd.hide();
                        floatingErase.hide();

                        //Limpiamos el formulario
                        fotoAñadir = false;
                    }
                    break;
                    case 1: {
                        floatingAdd.show();
                    }
                    break;
                    case 2: {
                        //Si la pantalla es pequeña esconemos el tablayout
                        if (Pantalla.saberAltoPantalla(context) < MIN_ALTO_PANTALLA) {
                            tabLayout.setVisibility(View.GONE);
                        }
                        floatingAdd.show();
                        floatingErase.show();
                    }
                    break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 2: {
                        //Volvemos a mostrar el tablayout
                        if (Pantalla.saberAltoPantalla(context) < MIN_ALTO_PANTALLA) {
                            tabLayout.setVisibility(View.VISIBLE);
                        }
                        floatingErase.hide();
                    }
                    break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //No hacer nada...
            }
        });

        //Floatingbtn add
        floatingAdd = (FloatingActionButton)findViewById(R.id.floatingAdd);
        floatingAdd.hide();   //Lo escondemos al principio
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Miramos que tab está seleccionado
                    switch (tabLayout.getSelectedTabPosition()) {
                        case 1: {
                            //Mostrar aquí el dialogo personalizado
                            new DialogoInsertTag(context, MainActivity.this);
                        }
                        break;
                        case 2: {
                            //Miramos si existe la sesión...
                            if (nSesionFragment.ComprobarCampos() == true) {
                                String nombre = nSesionFragment.tbNombre.getText().toString().trim();
                                if (new DAOSesiones(getApplicationContext()).ExistirSesion(nombre) == true) {
                                    //Si no hay etiquetas guardadas indicamos que hay que tener
                                    int numTags = new DAOTag(getApplicationContext()).SacarNumTags();
                                    if (numTags > 0 && numTags != -1) {
                                        DialogoSeleccion seleccion = new DialogoSeleccion();
                                        seleccion.show(getSupportFragmentManager(), SELECCIONAR);
                                    }
                                    else {
                                        DialogFragment dialogFragment = new DialogoAlerta();
                                        Bundle bundle = new Bundle();

                                        bundle.putString("TITULO", getString(R.string.no_tags));
                                        bundle.putString("MENSAJE", getString(R.string.no_tags_explicacion));
                                        dialogFragment.setArguments(bundle);

                                        dialogFragment.show(getSupportFragmentManager(), "error");
                                    }
                                }
                                else {
                                    LogeoActivity.centralizarToast(getApplicationContext(),
                                            getString(R.string.sesion_repetida));
                                }
                            }
                            else {
                                LogeoActivity.centralizarToast(getApplicationContext(),
                                        getString(R.string.sesion_5caracteres));
                            }
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
            }
        });

        //Floating erase
        floatingErase = (FloatingActionButton)findViewById(R.id.floatingErase);
        floatingErase.hide();       //Lo escondemos al principio
        floatingErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nSesionFragment.LimpiarUI();

                //Foto
                fotoAñadir = false;
                nSesionFragment.imageView.setImageResource(R.drawable.add_photo);
            }
        });

        //Mensaje de bienvenida con el nombre de la persona
        LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.bienvenido) + " " + SacarNombre());
    }

    //Método para introducir la sesión (es llamado por el diálogo de etiquetas)
    public void introducirEtiqueta(String etiqueta) {
        try {
            VOSesion sesion = new VOSesion();

            //Recogemos los datos de la sesión
            sesion.setNombre(nSesionFragment.tbNombre.getText().toString().trim());
            sesion.setEjercicio_1(nSesionFragment.tbM1.getText().toString().trim());
            sesion.setEjercicio_2(nSesionFragment.tbM2.getText().toString().trim());
            sesion.setEjercicio_3(nSesionFragment.tbM3.getText().toString().trim());
            sesion.setEjercicio_4(nSesionFragment.tbM4.getText().toString().trim());

            //Sacamos el identificador de la etiqueta
            int idTag = new DAOTag(getApplicationContext()).SacarID(etiqueta);
            sesion.setIdTag(idTag);

            //Fotografía
            if (fotoAñadir == true) {
                sesion.setFoto(ConversorFotos.ImageToBytes(nSesionFragment.imageView));
            }
            else {
                sesion.setFoto(null);
            }

            //Primero insertamos la sesión y obtenemos su ID
            new DAOSesiones(getApplicationContext()).InsertSesion(sesion);
            int IdSesion = new DAOSesiones(getApplicationContext()).SacarIdentificador(sesion.getNombre());

            //Finalmente insertamos su peso por defecto (5kgs)
            VOPeso NPeso = new VOPeso();
            NPeso.setPeso_1("5kg");
            NPeso.setPeso_2("5kg");
            NPeso.setPeso_3("5kg");
            NPeso.setPeso_4("5kg");
            NPeso.setNotas(null);
            NPeso.setIdSesion(IdSesion);
            new DAOPesos(getApplicationContext()).InsertarPeso(NPeso);

            //Informamos de que haya ido bien la cosa
            LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.sesion_insertada));
            nSesionFragment.LimpiarUI();

            //Cambiamos el estado de la foto
            fotoAñadir = false;
            nSesionFragment.imageView.setImageResource(R.drawable.add_photo);
            nSesionFragment.btnQuitarFoto.setVisibility(View.INVISIBLE);

            //NOS COMUNICAMOS MEDIANTE LA INTERFAZ CON LA ACTIVIDAD MAIN PARA QUE SE ACTUALICE LA LISTVIEW
            nSesionFragment.mListener.onFragmentInteraction(Uri.parse("actualiza"));
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

    //Sacamos el nombre de la persona
    private String SacarNombre() {
        String nombre = null;

        try {
            SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
            nombre = preferences.getString("USUARIO", null);
        }
        catch (Exception err) {
            nombre = null;
        }

        return nombre;
    }

    //Desactivamos el inicio de sesión automatico
    private void DesactivarInicioAutomatico() {
        try {
            SharedPreferences preferences = getSharedPreferences("Logeo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            //Lo desactivamos
            editor.putBoolean("AUTOMATICO", false);
            editor.commit();
        }
        catch (Exception err) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPerfil: {
                Intent intent = new Intent(MainActivity.this, AjustesActivity.class);
                startActivityForResult(intent, ACTUALIZAR);
            }
            break;
            case R.id.itemAyuda: {
                DialogFragment dialogFragment = new DialogoAlerta();
                Bundle bundle = new Bundle();

                bundle.putString("TITULO", getString(R.string.funcionar_app));
                bundle.putString("MENSAJE", getString(R.string.explicacion_app));
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "1111");
            }
            break;
            case R.id.itemFinalizar: {
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                Bundle bundle = new Bundle();

                bundle.putString("TITULO", getString(R.string.confirmacion));
                bundle.putString("MENSAJE", getString(R.string.desconectarse_app));
                dialogoConfirmacion.setArguments(bundle);

                dialogoConfirmacion.show(getSupportFragmentManager(), CERRAR);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Interfaz para que los fragmenos nos envien cosicas
    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.toString() == "actualiza") {
            sesionesFragment.LeerBD();
        }
        else if (uri.toString() == "quita_foto") {
            fotoAñadir = false;
        }
    }

    //Desconectamos de la pantalla principal
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag() == CERRAR) {
            //Desactivamos el inicio de sesión automático y salimos de la actividad
            DesactivarInicioAutomatico();
            this.finish();
        }
    }

    //Método llamado por todos los fragmentos contenidos en esta actividad!!!!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //Leer la BD
            if (requestCode == MainActivity.ACTUALIZAR) {
                sesionesFragment.LeerBD();
            }
            //Cargar foto de la galería
            else if (requestCode == MainActivity.GALERIA) {
                Uri imageUri = data.getData();
                nSesionFragment.imageView.setImageURI(imageUri);

                //Indicamos que se va a añadir foto
                nSesionFragment.btnQuitarFoto.setVisibility(View.VISIBLE);
                fotoAñadir = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MainActivity.PERMISO_ALMACEN) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                nSesionFragment.AbrirGaleria();
            }
        }
    }

    //Recuperamos la etiqueta y la insertamos
    @Override
    public void InsertTag(VOTag tag) {
        try {
            if (new DAOTag(getApplicationContext()).ExistirTag(tag) == true) {
                new DAOTag(getApplicationContext()).InsertarTag(tag);
                tagsFragment.LeerTags();
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.tag_introducida));
            }
            else {
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.tag_existente));
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

    //Modificamos una tag
    @Override
    public void Modificar(VOTag Tag) {
        try {
            if (new DAOTag(getApplicationContext()).ExistirTag(Tag) == true) {
                new DAOTag(getApplicationContext()).UpdateTag(Tag);
                tagsFragment.LeerTags();                //Leemos las etiquetas otra vez
                sesionesFragment.LeerBD();              //Leemos la base de datos de nuevo
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.tag_modif));
            }
            else {
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.tag_existente));
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

    //Borramos una tag
    @Override
    public void Borrar(VOTag Tag) {
        try {
            //Miramos si hay sesiones almacenadas con la tag que vamos a borrar. En caso de que las haya no
            //dejamos al usuario que borre la etiqueta. Se lo mostramos con un cuadro de díalogo
            if (new DAOSesiones(getApplicationContext()).ComprobarSiHayTags(Tag) == true) {
                new DAOTag(getApplicationContext()).DeleteTag(Tag.getIdentificador());
                tagsFragment.LeerTags();
                LogeoActivity.centralizarToast(getApplicationContext(), getString(R.string.tag_borrada));
            }
            else {
                DialogFragment dialogFragment = new DialogoAlerta();
                Bundle bundle = new Bundle();

                bundle.putString("TITULO", getString(R.string.cuidado));
                bundle.putString("MENSAJE", getString(R.string.no_borrar_tag));
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "error");
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

    //METODO QUE SE EJECUTA AL CLICAR EN EL BOTON DE ATRÁS DE LOS MÓVILES
    @Override
    public void onBackPressed() {
        DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
        Bundle bundle = new Bundle();

        bundle.putString("TITULO", getString(R.string.confirmacion));
        bundle.putString("MENSAJE", getString(R.string.desconectarse_app));
        dialogoConfirmacion.setArguments(bundle);

        dialogoConfirmacion.show(getSupportFragmentManager(), CERRAR);
    }

    //CLASE QUE CONTROLA LA SELECCIÓN DE FRAGMENTOS
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        //Creamos cada fragmento
        public static Fragment newInstance(int sectionNumber) {

            Fragment fragment = null;

            switch (sectionNumber) {
                case 1: {
                    fragment = new SesionesFragment();
                }
                break;
                case 2: {
                    fragment = new TagsFragment();
                }
                break;
                case 3: {
                    fragment = new NSesionFragment();
                }
                break;
            }

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    //CLASE QUE CONTROLA EL PAGE-ADAPTER
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //Obtenemos el fragmento
            Fragment fragment = PlaceholderFragment.newInstance(position + 1);

            //Lo guardamos como instancia para utilizarlo
            switch (position) {
                case 0: {
                    sesionesFragment = (SesionesFragment) fragment;
                }
                break;
                case 1: {
                    tagsFragment = (TagsFragment) fragment;
                }
                break;
                case 2: {
                    nSesionFragment = (NSesionFragment) fragment;
                }
                break;
            }

            //Lo retornamos para que se muestre en pantalla
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int itemPosition) {
            switch (itemPosition) {
                case 0: {
                    return getString(R.string.sesiones);
                }
                case 1: {
                    return getString(R.string.tags);
                }
                case 2: {
                    return getString(R.string.nueva_sesion);
                }

                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}