package com.ychill.nicolaslopezf.entregablefinal.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;

import java.util.ArrayList;

/**
 * Created by mora on 21/10/2016.
 */

public class FragmentRecyclerPeliculas extends Fragment {

    private RecyclerView recyclerViewPeliculas;
    private AdapterRecyclerPeliculas unAdapterPelicula;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View unaVistaADevolver = inflater.inflate(R.layout.fragment_recycle_solo, container, false);

        recyclerViewPeliculas = (RecyclerView)unaVistaADevolver.findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewPeliculas.setLayoutManager(linearLayoutManager);

        PeliculaController peliculasController = new PeliculaController();
        String genero = null;

        //voy a  hacer que le pase un bundle cuando clikias el boton y eel metodo reciba el bundle y cree el metodo basado en eso
        ArrayList<Pelicula> peliculasDelRecycle;
        try{
            Bundle unBundle = getArguments();
            genero = unBundle.getString("genero");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (genero != null){
            peliculasDelRecycle = peliculasController.damePeliculasPorGenero(genero,getContext());
        }
        else{
            peliculasDelRecycle = peliculasController.dameTodasLasPeliculas(getActivity());
        }



        unAdapterPelicula = new AdapterRecyclerPeliculas(getActivity(), peliculasDelRecycle, new ListenerPeliculas());
        recyclerViewPeliculas.setAdapter(unAdapterPelicula);


        return unaVistaADevolver;
    }
    public class ListenerPeliculas implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewPeliculas.getChildAdapterPosition(view);
            Pelicula peliculaTocada = unAdapterPelicula.getPeliculaAtPosition(posicionTocada);

            ComunicadorFragmentActivity unComunicador = (ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEstaPelicula(peliculaTocada);

        }
    }
    public interface ComunicadorFragmentActivity{
        public void clickearonEstaPelicula(Pelicula peliculaClickeada);

    }


}
