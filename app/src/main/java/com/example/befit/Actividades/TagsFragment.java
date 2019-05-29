package com.example.befit.Actividades;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.befit.Adaptadores_LV.AdaptadorLVTags;
import com.example.befit.Dialogos.DialogoModificarTag;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Entidades.VOTag;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

public class TagsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;        //Interfaz propio

    EditText tbFiltrarTags;                                 //Filtrador

    ListView lvTags;                                        //Listview
    AdaptadorLVTags adaptadorLVTags;                        //Adaptador

    public TagsFragment() {
        // Required empty public constructor
    }

    //Creación del fragment
    public static TagsFragment newInstance(String param1, String param2) {
        TagsFragment fragment = new TagsFragment();
        return fragment;
    }

    //Leemos los tags (tiene que ser público)
    public void LeerTags() {
        try {
            adaptadorLVTags = new AdaptadorLVTags(getActivity().getApplicationContext(), new DAOTag(getContext()).ReadTags());
            lvTags.setAdapter(adaptadorLVTags);

            //Filtramos en caso de que haya texto
            if (adaptadorLVTags != null) {
                adaptadorLVTags.filtrar(tbFiltrarTags.getText().toString());
            }
        }
        catch (Exception err) {
            DialogFragment dialogFragment = new DialogoAlerta();
            Bundle bundle = new Bundle();

            bundle.putString("TITULO", getString(R.string.error));
            bundle.putString("MENSAJE", err.getMessage());
            dialogFragment.setArguments(bundle);

            dialogFragment.show(getFragmentManager(), "error");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tags, container, false);

        //Lista
        lvTags = (ListView) view.findViewById(R.id.lvTags);
        lvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    VOTag tag = (VOTag) adaptadorLVTags.getItem(position);
                    new DialogoModificarTag((MainActivity)getActivity(), tag, (MainActivity)getActivity());
                }
                catch (Exception err) {
                    DialogFragment dialogFragment = new DialogoAlerta();
                    Bundle bundle = new Bundle();

                    bundle.putString("TITULO", getString(R.string.error));
                    bundle.putString("MENSAJE", err.getMessage());
                    dialogFragment.setArguments(bundle);

                    dialogFragment.show(getFragmentManager(), "error");
                }
            }
        });

        //Filtrador
        tbFiltrarTags = (EditText) view.findViewById(R.id.tbFiltrarTags);
        tbFiltrarTags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nada que hacer...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (adaptadorLVTags != null) {
                        adaptadorLVTags.filtrar(tbFiltrarTags.getText().toString());
                    }
                }
                catch (Exception err) {
                    DialogFragment dialogFragment = new DialogoAlerta();
                    Bundle bundle = new Bundle();

                    bundle.putString("TITULO", getString(R.string.error));
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

        //Leemos las etiquetas
        LeerTags();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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