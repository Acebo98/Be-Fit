package com.example.befit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class SesionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ArrayList<VOSesion> lSesiones;          //Lista de las sesiones
    AdaptadorLV adaptadorLV;                //Adaptador
    final int ACTUALIZAR = 1111;            //Constante de que es necesario actualizar la lista

    //Controles
    View view;
    ListView lvSesiones;
    FloatingActionButton floatingUpdate;

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
                    if (new DAOSesiones(getContext()).ReadSesiones().size() > 0) {
                        VOSesion sesion = lSesiones.get(position);

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

                }
            }
        });
        floatingUpdate = view.findViewById(R.id.floatingUpdate);
        floatingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeerBD();
                LogeoActivity.centralizarToast(getContext(), "Lista actualizada");
            }
        });

        //Leemos los datos
        LeerBD();

        return view;
    }

    //Leemos los entrenamientos de la base de datos
    private void LeerBD() {
        try {
            lSesiones = new DAOSesiones(getContext()).ReadSesiones();
            adaptadorLV = new AdaptadorLV(getActivity().getApplicationContext(), lSesiones);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTUALIZAR) {
            if (resultCode == RESULT_OK) {
                //Si se ha borrado actualizamos la lista para que no salga la sesión recién borrada
                LeerBD();
            }
        }
    }

    //Interfaz del fragmento. IMPORTANTE AÑADIRLO A LA ACTIVIDAD QUE RECOGE TODOS LOS FRAGMENTOS
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
