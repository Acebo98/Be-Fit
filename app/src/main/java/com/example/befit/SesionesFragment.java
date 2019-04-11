package com.example.befit;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

public class SesionesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ArrayList<VOSesion> lSesiones;          //Lista de las sesiones

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflamos la vista
        view = inflater.inflate(R.layout.fragment_sesiones, container, false);

        //Controles
        lvSesiones = view.findViewById(R.id.lvSesiones);
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
            AdaptadorLV adaptadorLV = new AdaptadorLV(getActivity().getApplicationContext(), lSesiones);
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

    //Interfaz del fragmento. IMPORTANTE AÑADIRLO A LA ACTIVIDAD QUE RECOGE TODOS LOS FRAGMENTOS
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
