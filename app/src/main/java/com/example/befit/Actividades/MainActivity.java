package com.example.befit.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Dialogos.DialogoConfirmacion;
import com.example.befit.Entidades.VOPeso;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.Modelos.DAOPesos;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.R;

public class MainActivity extends AppCompatActivity implements SesionesFragment.OnFragmentInteractionListener,
        NSesionFragment.OnFragmentInteractionListener, DialogoConfirmacion.MiDialogListener {

    final String CERRAR = "cerrar";
    final int ACTUALIZAR = 1111;

    //FloatingButtons
    FloatingActionButton floatingAdd;
    FloatingActionButton floatingErase;

    //INSTANCIAS DE LOS FRAGMENTOS A UTILIZAR
    SesionesFragment sesionesFragment;
    NSesionFragment nSesionFragment;

    //Tablayout
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sesion);

        //Evento para mostrar el floatingbutton
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    floatingAdd.show();
                    floatingErase.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    floatingAdd.hide();
                    floatingErase.hide();
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
                    if (nSesionFragment.ComprobarCampos() == true) {
                        if (new DAOSesiones(getApplicationContext()).ExistirSesion(nSesionFragment.tbNombre.getText().toString().trim()) == true) {
                            VOSesion sesion = new VOSesion();

                            //Recogemos los datos de la sesión
                            sesion.setNombre(nSesionFragment.tbNombre.getText().toString().trim());
                            sesion.setMusculo_1(nSesionFragment.tbM1.getText().toString().trim());
                            sesion.setMusculo_2(nSesionFragment.tbM2.getText().toString().trim());
                            sesion.setMusculo_3(nSesionFragment.tbM3.getText().toString().trim());
                            sesion.setMusculo_4(nSesionFragment.tbM4.getText().toString().trim());
                            sesion.setTag(String.valueOf(nSesionFragment.spnTags.getSelectedItem()));

                            //Primero insertamos la sesión y obtenemos su ID
                            new DAOSesiones(getApplicationContext()).InsertSesion(sesion);
                            int IdSesion = new DAOSesiones(getApplicationContext()).SacarIdentificador(sesion.getNombre());

                            //Finalmente insertamos su peso por defecto (5kgs)
                            VOPeso NPeso = new VOPeso();
                            NPeso.setPeso_1("5kg");
                            NPeso.setPeso_2("5kg");
                            NPeso.setPeso_3("5kg");
                            NPeso.setPeso_4("5kg");
                            NPeso.setNotas("Escribe aquí tus notas...");
                            NPeso.setIdSesion(IdSesion);
                            new DAOPesos(getApplicationContext()).InsertarPeso(NPeso);

                            //Informamos de que haya ido bien la cosa
                            LogeoActivity.centralizarToast(getApplicationContext(), "Sesión Insertada");
                            nSesionFragment.LimpiarUI();

                            //NOS COMUNICAMOS MEDIANTE LA INTERFAZ CON LA ACTIVIDAD MAIN PARA QUE SE ACTUALICE
                            //LA LISTVIEW
                            nSesionFragment.mListener.onFragmentInteraction(Uri.parse("actualiza"));
                        }
                        else {
                            LogeoActivity.centralizarToast(getApplicationContext(), "Parece que ya tienes una sesión con " +
                                    "dicho nombre ya insertada");
                        }
                    }
                    else {
                        LogeoActivity.centralizarToast(getApplicationContext(), "Los campos de texto deben de tener mínimo " +
                                "5 carácteres");
                    }
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
        });

        //Floating erase
        floatingErase = (FloatingActionButton)findViewById(R.id.floatingErase);
        floatingErase.hide();       //Lo escondemos al principio
        floatingErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nSesionFragment.LimpiarUI();
            }
        });

        //Mensaje de bienvenida con el nombre de la persona
        LogeoActivity.centralizarToast(getApplicationContext(), "Bienvenid@ " + SacarNombre());
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
                Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
                startActivityForResult(intent, ACTUALIZAR);
            }
            break;
            case R.id.itemAyuda: {
                DialogFragment dialogFragment = new DialogoAlerta();
                Bundle bundle = new Bundle();

                bundle.putString("TITULO", "¿Cómo funciona esta aplicación?");
                bundle.putString("MENSAJE", "Con esta aplicación podras tener un registro de forma intuitiva en tu teléfono " +
                        "móvil del seguimiento de tus sesiones en el gimnasio. \n\n" +
                        "Con la sección NUEVA SESIÓN podrás introducir una nueva sesión, estas sesiones se encontrarán reflejadas " +
                        "en la sección SEGUIMIENTO, lugar donde podrás seleccionar y actualizar el seguimiento de pesos " +
                        "de dicha sesión. \n\nObviamente puedes también borrar una sesión, y por consiguiente su " +
                        "seguimiento, en cualquier momento.");
                dialogFragment.setArguments(bundle);

                dialogFragment.show(getSupportFragmentManager(), "1111");
            }
            break;
            case R.id.itemFinalizar: {
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                Bundle bundle = new Bundle();

                bundle.putString("TITULO", "Confirmación");
                bundle.putString("MENSAJE", "¿Deseas desconectarte de la aplicación?");
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
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog.getTag() == CERRAR) {
            this.finish();
        }
    }

    //Método llamado por todos los fragmentos contenidos en esta actividad!!!!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            sesionesFragment.LeerBD();
        }
    }

    //CLASE QUE CONTROLA LA SELECCIÓN DE FRAGMENTOS
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

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
            if (position == 0) {
                sesionesFragment = (SesionesFragment) fragment;
            }
            else if (position == 1) {
                nSesionFragment = (NSesionFragment) fragment;
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
                    return getString(R.string.nueva_sesion);
                }
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}