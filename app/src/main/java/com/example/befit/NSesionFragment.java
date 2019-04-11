package com.example.befit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class NSesionFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    //Controles
    View view;
    EditText tbNombre;
    EditText tbM1;
    EditText tbM2;
    EditText tbM3;
    EditText tbM4;
    Button btnAceptar;
    Button btnLimpiar;
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
        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);

        //Lista de campos de texto
        lCampos.add(tbNombre);
        lCampos.add(tbM1);
        lCampos.add(tbM2);
        lCampos.add(tbM3);
        lCampos.add(tbM4);

        //Controles
        btnLimpiar.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);

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
            case R.id.btnAceptar: {
                try {
                    if (ComprobarCampos() == true) {

                    }
                    else {
                        LogeoActivity.centralizarToast(getContext(), "Primero rellena todos los campos");
                    }
                }
                catch (Exception err) {

                }
            }
            break;
            case R.id.btnLimpiar: {
                LimpiarUI();
            }
            break;
        }
    }

    //Comprobamos que todos los campos estén rellenos
    private boolean ComprobarCampos() {
        for (EditText campo : lCampos) {
            if (campo.getText().toString().trim().length() == 0) {
                return false;
            }
        }
        return true;
    }

    //Limpiamos los campos de texto
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
