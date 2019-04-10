package com.example.befit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PerfilFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    //Controles
    View view;
    Button btnModificar;
    Button btnBorrar;
    EditText tbUsuario;
    EditText tbContra;

    public PerfilFragment() {
        // Required empty public constructor
    }

    //Creación del fragment
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Controles
        btnBorrar = (Button)view.findViewById(R.id.btnBorrar);
        btnModificar = (Button)view.findViewById(R.id.btnModificar);
        tbContra = (EditText)view.findViewById(R.id.tbMiContra);
        tbUsuario = (EditText)view.findViewById(R.id.tbMiNombre);

        //Eventos
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBorrar: {

            }
            break;
            case R.id.btnModificar: {

            }
            break;
        }
    }

    //Interfaz del fragmento. IMPORTANTE AÑADIRLO A LA ACTIVIDAD QUE RECOGE TODOS LOS FRAGMENTOS
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
