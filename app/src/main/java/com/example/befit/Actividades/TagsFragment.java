package com.example.befit.Actividades;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.befit.AdaptadorLVTags;
import com.example.befit.Dialogos.DialogoAlerta;
import com.example.befit.Modelos.DAOTag;
import com.example.befit.R;

public class TagsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;        //Interfaz propio

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
            adaptadorLVTags = new AdaptadorLVTags(getContext(), new DAOTag(getContext()).ReadTags());
            lvTags.setAdapter(adaptadorLVTags);
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