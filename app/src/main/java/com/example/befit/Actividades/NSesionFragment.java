package com.example.befit.Actividades;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.R;

import java.util.ArrayList;

public class NSesionFragment extends Fragment {

    public OnFragmentInteractionListener mListener;         //Interfaz
    TabLayout tabLayout;                                    //Tablayout

    //Controles
    View view;
    EditText tbNombre;
    EditText tbM1;
    EditText tbM2;
    EditText tbM3;
    EditText tbM4;
    ImageView imageView;
    Button btnQuitarFoto;

    //Booleana para informar del tamaño de las fotografias
    boolean avisoFotos;

    //Lista para los campos de texto
    ArrayList<EditText> lCampos = new ArrayList<>();

    public NSesionFragment() {
        // Required empty public constructor
    }

    //Creación del fragment
    public static NSesionFragment newInstance() {
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
        avisoFotos = true;

        //Inflamos la vista y la guardamos para manejar los eventos
        view = inflater.inflate(R.layout.fragment_nsesion, container, false);

        //IDS
        tbNombre = view.findViewById(R.id.tbNombreSesion);
        tbM1 = view.findViewById(R.id.tbM1);
        tbM2 = view.findViewById(R.id.tbM2);
        tbM3 = view.findViewById(R.id.tbM3);
        tbM4 = view.findViewById(R.id.tbM4);
        imageView = view.findViewById(R.id.imageViewFoto);
        tabLayout = view.findViewById(R.id.tabLayout);
        btnQuitarFoto = view.findViewById(R.id.btnQuitarFoto);

        //Pillamos la foto
        imageView.setImageResource(R.drawable.add_photo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Primero mostramos una alerta sobre el tamaño de las fotos...
                if (avisoFotos == true) {
                    LogeoActivity.centralizarToast(getActivity(), getString(R.string.aviso_foto));
                    avisoFotos = !avisoFotos;
                }

                //Permisos...
                if (ContextCompat.checkSelfPermission((MainActivity)getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((MainActivity)getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MainActivity.PERMISO_ALMACEN);
                }
                else if (ContextCompat.checkSelfPermission((MainActivity)getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    AbrirGaleria();
                }
            }
        });

        //Quitamos la foto
        btnQuitarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnQuitarFoto.getVisibility() == View.VISIBLE) {
                    imageView.setImageResource(R.drawable.add_photo);
                    mListener.onFragmentInteraction(Uri.parse("quita_foto"));
                    btnQuitarFoto.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnQuitarFoto.setVisibility(View.INVISIBLE);

        //Lista de campos de texto
        lCampos.clear();
        lCampos.add(tbNombre);
        lCampos.add(tbM1);
        lCampos.add(tbM2);
        lCampos.add(tbM3);
        lCampos.add(tbM4);

        return view;
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

    //Abrimos la galeria
    public void AbrirGaleria() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(gallery, MainActivity.GALERIA);
    }

    //Comprobamos que todos los campos estén rellenos
    public boolean ComprobarCampos() {
        for (EditText campo : lCampos) {
            if (campo.getText().toString().trim().length() < 3) {
                return false;
            }
        }
        return true;
    }

    //Limpiamos los campos de texto
    public void LimpiarUI() {
        for (EditText campo : lCampos) {
            campo.getText().clear();
        }
    }

    //Interfaz del fragmento. IMPORTANTE AÑADIRLO A LA ACTIVIDAD QUE RECOGE TODOS LOS FRAGMENTOS
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}