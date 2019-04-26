package com.example.befit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class SesionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    
    AdaptadorLV adaptadorLV;                //Adaptador
    final int ACTUALIZAR = 1111;            //Constante de que es necesario actualizar la lista

    //Controles
    View view;
    ListView lvSesiones;
    EditText tbbuscar;

    public SesionesFragment() {
        // Required empty public constructor
    }

    //Creación del fragment
    public static SesionesFragment newInstance(String param1, String param2) {
        SesionesFragment fragment = new SesionesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos la vista
        view = inflater.inflate(R.layout.fragment_sesiones, container, false);

        //Controles
        lvSesiones = view.findViewById(R.id.lvSesiones);
        lvSesiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //Miramos si hay sesiones
                    if (new DAOSesiones(getContext()).ReadSesiones().size() > 0) {
                        VOSesion sesion = (VOSesion) adaptadorLV.getItem(position);

                        //Bundle de datos con el identificador
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", sesion.getIdentificador());

                        //Intent
                        Intent intent = new Intent(getContext(), PesosActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, ACTUALIZAR);
                    }
                    else {
                        LogeoActivity.centralizarToast(getContext(), "Parece que tu lista no está actualizada");
                        LeerBD();
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
        tbbuscar = (EditText)view.findViewById(R.id.tbBuscador);
        tbbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adaptadorLV.Filtrar(tbbuscar.getText().toString());
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tbbuscar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    tbbuscar.getText().clear();
                }
            }
        });

        //Leemos los datos
        LeerBD();

        return view;
    }

    //Leemos los entrenamientos de la base de datos
    public void LeerBD() {
        try {
            adaptadorLV = new AdaptadorLV(getActivity().getApplicationContext(),
                    new DAOSesiones(getContext()).ReadSesiones());
            lvSesiones.setAdapter(adaptadorLV);
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

    //Método llamado por todos los fragmentos contenidos en esta actividad!!!!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTUALIZAR || requestCode == 66647) {
            this.LeerBD();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    //Interfaz del fragmento. IMPORTANTE AÑADIRLO A LA ACTIVIDAD QUE RECOGE TODOS LOS FRAGMENTOS
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
