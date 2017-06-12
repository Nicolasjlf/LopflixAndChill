package com.ychill.nicolaslopezf.entregablefinal.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.AdapterRecyclerSoloImagen;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 12/4/2016.
 */

public class FragmentPeliculasGenero extends Fragment {

    private RecyclerView recyclerViewPeliculas;
    private AdapterRecyclerSoloImagen unAdapterPelicula;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View unaVistaADevolver = inflater.inflate(R.layout.fragment_recycle_solo, container, false);

        String genero = "";
        try{
            Bundle bundle = getArguments();
            genero = bundle.getString("genero");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        recyclerViewPeliculas = (RecyclerView)unaVistaADevolver.findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewPeliculas.setLayoutManager(linearLayoutManager);

        PeliculaController peliculasController = new PeliculaController();


        unAdapterPelicula = new AdapterRecyclerSoloImagen(getActivity());

        final ArrayList<MovieDB> peliculas = new ArrayList<>();
        unAdapterPelicula.setListaDePeliculas(peliculas);

    //    unAdapterPelicula.setListener(new FragmentRecyclerSoloImagen.ListenerPeliculasSoloImagen(recyclerViewPeliculas, unAdapterPelicula));




        peliculasController.obtenerPeliculasPorGenero(genero, getActivity(), new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieDB peliculasLatest = (ContainerMovieDB) resultado;

                unAdapterPelicula.setListaDePeliculas(peliculasLatest.getResult());
                unAdapterPelicula.agregarPeliculasAlRecycle(peliculasLatest.getResult());
                unAdapterPelicula.notifyDataSetChanged();
            }
        });



        recyclerViewPeliculas.setAdapter(unAdapterPelicula);

        unAdapterPelicula.setListener(new FragmentPeliculasGenero.ListenerPeliculasSoloImagen(recyclerViewPeliculas,unAdapterPelicula));

        //unAdapterPelicula.setListaDePeliculas(peliculasDelRecycle);
       // unAdapterPelicula.setListener(new FragmentRecycleGridFavoritas.ListenerPeliculasSoloImagen(recyclerViewPeliculas,unAdapterPelicula));
        recyclerViewPeliculas.setAdapter(unAdapterPelicula);



        return unaVistaADevolver;
    }

    public class ListenerPeliculasSoloImagen implements View.OnClickListener {

        private RecyclerView recyclerViewAUsar;
        private AdapterRecyclerSoloImagen adapterAUsar;

        public ListenerPeliculasSoloImagen(RecyclerView recyclerViewAUsar, AdapterRecyclerSoloImagen adapterAUsar) {
            this.recyclerViewAUsar = recyclerViewAUsar;
            this.adapterAUsar = adapterAUsar;
        }

        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewAUsar.getChildAdapterPosition(view);
            MovieDB peliculaTocada = adapterAUsar.getPeliculaAtPosition(posicionTocada);

            Log.d("imagen",peliculaTocada.getPoster());

            FragmentRecyclerSoloImagen.ComunicadorFragmentActivity unComunicador = (FragmentRecyclerSoloImagen.ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEstaPelicula(peliculaTocada);


        }
    }
    public interface ComunicadorFragmentActivity {
        void clickearonEstaPelicula(MovieDB peliculaClickeada);

    }


}

