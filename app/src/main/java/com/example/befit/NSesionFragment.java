package com.example.befit;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NSesionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    TabLayout tabLayout;

    //Controles
    View view;
    EditText tbNombre;
    EditText tbM1;
    EditText tbM2;
    EditText tbM3;
    EditText tbM4;
    FloatingActionButton floatingAdd;
    ArrayList<EditText> lCampos = new ArrayList<>();

    public NSesionFragment() {
        // Required empty public constructor
    }

    //Creación del fragment
    public static NSesionFragment newInstance(String param1, String param2) {
        NSesionFragment fragment = new NSesionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos la vista y la guardamos para manejar los eventos
        view = inflater.inflate(R.layout.fragment_nsesion, container, false);

        //IDS
        tbNombre = view.findViewById(R.id.tbNombreSesion);
        tbM1 = view.findViewById(R.id.tbM1);
        tbM2 = view.findViewById(R.id.tbM2);
        tbM3 = view.findViewById(R.id.tbM3);
        tbM4 = view.findViewById(R.id.tbM4);
        tabLayout = view.findViewById(R.id.tabLayout);
        floatingAdd = view.findViewById(R.id.floatingAdd);

        //Lista de campos de texto
        lCampos.add(tbNombre);
        lCampos.add(tbM1);
        lCampos.add(tbM2);
        lCampos.add(tbM3);
        lCampos.add(tbM4);

        //Controles
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ComprobarCampos() == true) {
                        if (new DAOSesiones(getContext()).ExistirSesion(tbNombre.getText().toString().trim()) == true) {
                            VOSesion sesion = new VOSesion();

                            //Recogemos los datos de la sesión
                            sesion.setNombre(tbNombre.getText().toString().trim());
                            sesion.setMusculo_1(tbM1.getText().toString().trim());
                            sesion.setMusculo_2(tbM2.getText().toString().trim());
                            sesion.setMusculo_3(tbM3.getText().toString().trim());
                            sesion.setMusculo_4(tbM4.getText().toString().trim());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            sesion.setActualizacion(simpleDateFormat.format(new Date()));

                            //Primero insertamos la sesión y obtenemos su ID
                            new DAOSesiones(getContext()).InsertSesion(sesion);
                            int IdSesion = new DAOSesiones(getContext()).SacarIdentificador(sesion.getNombre());

                            //Finalmente insertamos su peso por defecto
                            new DAOPesos(getContext()).InsertarPeso(IdSesion);

                            //Informamos de que haya ido bien la cosa
                            LogeoActivity.centralizarToast(getContext(), "Sesión insertada");
                            LimpiarUI();

                            //NOS COMUNICAMOS MEDIANTE LA INTERFAZ CON LA ACTIVIDAD MAIN PARA QUE SE ACTUALICE
                            //LA LISTVIEW
                            mListener.onFragmentInteraction(Uri.parse("actualiza"));
                        }
                        else {
                            LogeoActivity.centralizarToast(getContext(), "Parece que ya tienes una sesión con " +
                                    "dicho nombre ya insertada");
                        }
                    }
                    else {
                        LogeoActivity.centralizarToast(getContext(), "Los campos de texto deben de tener mínimo " +
                                "5 carácteres");
                    }
                }
                catch (Exception err) {
                    DialogFragment dialogFragment = new DialogoAlerta();
                    Bundle bundle = new Bundle();

                    bundle.putString("TITULO", "Ha ocurrido un Error");
                    bundle.putString("MENSAJE", err.getMessage());
                    dialogFragment.setArguments(bundle);

                    dialogFragment.show(getFragmentManager(), "error");
                }
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Comprobamos que todos los campos estén rellenos
    private boolean ComprobarCampos() {
        for (EditText campo : lCampos) {
            if (campo.getText().toString().trim().length() < 5) {
                return false;
            }
        }
        return true;
    }

    //Limpiamos los campos de texto (SIN UTILIZAR)
    private void LimpiarUI() {
        for (EditText campo : lCampos) {
            campo.getText().clear();
        }
    }

    //Interfaz del fragmento. IMPORTANTE AÑADIRLO A LA ACTIVIDAD QUE RECOGE TODOS LOS FRAGMENTOS
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
