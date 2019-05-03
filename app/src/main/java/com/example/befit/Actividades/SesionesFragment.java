package com.example.befit.Actividades;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.befit.Adaptadores_LV.AdaptadorLV;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Entidades.VOSesion;
import com.example.befit.Modelos.DAOSesiones;
import com.example.befit.R;

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
    public static SesionesFragment newInstance() {
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
                    VOSesion sesion = (VOSesion) adaptadorLV.getItem(position);
                    final int identificador = sesion.getIdentificador();

                    //Vemos si la sesión está bloqueda
                    if (new DAOSesiones(getContext()).IsBlocked(String.valueOf(identificador)) == true) {

                        //Bundle de datos con el identificador
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", identificador);

                        //Intent
                        Intent intent = new Intent(getContext(), PesosActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, ACTUALIZAR);
                    }
                    else {
                        Snackbar.make(view, "Dicha sesión está bloqueada", Snackbar.LENGTH_LONG)
                                .setAction("Desbloquear", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            //Desbloqueamos y actualizamos la lista
                                            new DAOSesiones(getContext()).DesbloquearSesion(String.valueOf(identificador));
                                            LeerBD();
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
                                }).show();
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
                //Nada que hacer...
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
                //Nada que hacer...
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

        //Leemos de la base de datos
        LeerBD();

        return view;
    }

    //Leemos los entrenamientos de la base de datos (TIENE QUE SER PUBLICO)
    public void LeerBD() {
        try {
            adaptadorLV = new AdaptadorLV(getActivity().getApplicationContext(),
                    new DAOSesiones(getContext()).ReadSesiones());
            lvSesiones.setAdapter(adaptadorLV);

            //Filtramos
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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
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
        void onFragmentInteraction(Uri uri);
    }
}