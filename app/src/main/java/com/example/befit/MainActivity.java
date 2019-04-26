package com.example.befit;

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

import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SesionesFragment.OnFragmentInteractionListener,
        NSesionFragment.OnFragmentInteractionListener, DialogoConfirmacion.MiDialogListener {

    final String CERRAR = "cerrar";
    final int ACTUALIZAR = 1111;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTUALIZAR) {
            if (resultCode == RESULT_OK) {
                sesionesFragment.LeerBD();
            }
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

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //fragment.setArguments(args);

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