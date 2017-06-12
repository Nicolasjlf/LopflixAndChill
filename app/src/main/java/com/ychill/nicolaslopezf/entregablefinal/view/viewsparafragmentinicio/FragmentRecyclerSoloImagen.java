package com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDBTrailerContainer;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;

/**
 * Created by santiagoiraola on 11/10/16.
 */

public class FragmentRecyclerSoloImagen extends Fragment{

    private RecyclerView recyclerViewPeliculasTrending;
    private AdapterRecyclerSoloImagen unAdapterPeliculasTrending;

    private RecyclerView recyclerViewPeliculasHighestGrossing;
    private AdapterRecyclerSoloImagen unAdapterPeliculasHighestGrossing;

    private RecyclerView recyclerViewPeliculasTopRated;
    private AdapterRecyclerSoloImagen unAdapterPeliculasTopRated;


    private PeliculaController peliculaController;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String genero = "Comedy";



        try{
            Bundle bundle = getArguments();
            genero = bundle.getString("genero");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        View unaVistaADevolver = inflater.inflate(R.layout.fragment_listas_de_pelis, container, false);

        //CONTROLLER SE USA PARA TODOS LOS RECYCLERS
        peliculaController = new PeliculaController();


        //-------------------------COMIENZO DE RECYCLER PARA TRENDING----------------------------------//

        recyclerViewPeliculasTrending = (RecyclerView) unaVistaADevolver.findViewById(R.id.listasDePelis_recyclerTrending);
        LinearLayoutManager linearLayoutManagerTrending = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPeliculasTrending.setLayoutManager(linearLayoutManagerTrending);


        //TENGO QUE SETEAR EL LISTENER POR SEPARADO PORQUE SI EN LA MISMA LINEA CREO EL NEW ADAPTER Y LO SETEO AL LISTENER POR CONSTRUCTOR TIRA NULLPOINTER.
        unAdapterPeliculasTrending = new AdapterRecyclerSoloImagen(getActivity());
        unAdapterPeliculasTrending.setListener(new ListenerPeliculasSoloImagen(recyclerViewPeliculasTrending,unAdapterPeliculasTrending));


        peliculaController.obtenerListaDePeliculasTMDB(TMDBHelper.getPopularMovies(TMDBHelper.language_ENGLISH,1),getActivity(), new ResultListener() {
                @Override
                public void finish(Object resultado) {
                    ContainerMovieDB peliculasTrending = (ContainerMovieDB) resultado;

                    unAdapterPeliculasTrending.setListaDePeliculas(peliculasTrending.getResult());
                    unAdapterPeliculasTrending.notifyDataSetChanged();
                }
            });

        recyclerViewPeliculasTrending.setAdapter(unAdapterPeliculasTrending);
        //-------------------------FIN DE RECYCLER PARA TRENDING----------------------------------//


        //-------------------------COMIENZO DE RECYCLER PARA TOPRATED----------------------------------//
        recyclerViewPeliculasTopRated = (RecyclerView) unaVistaADevolver.findViewById(R.id.listasDePelis_recyclerTopRated);
        LinearLayoutManager linearLayoutManagerLatest = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPeliculasTopRated.setLayoutManager(linearLayoutManagerLatest);

        unAdapterPeliculasTopRated = new AdapterRecyclerSoloImagen(getActivity());
        unAdapterPeliculasTopRated.setListener(new ListenerPeliculasSoloImagen(recyclerViewPeliculasTopRated, unAdapterPeliculasTopRated));


            TextView textView = (TextView) unaVistaADevolver.findViewById(R.id.listasDePelis_textoTopRated);
            textView.setText("Recomended " + genero + " movies 2016");
            peliculaController.obtenerPeliculasPorGenero(genero, getActivity(), new ResultListener() {
                @Override
                public void finish(Object resultado) {
                    ContainerMovieDB peliculasLatest = (ContainerMovieDB) resultado;

                    unAdapterPeliculasTopRated.setListaDePeliculas(peliculasLatest.getResult());
                    unAdapterPeliculasTopRated.notifyDataSetChanged();
                }
            });



        recyclerViewPeliculasTopRated.setAdapter(unAdapterPeliculasTopRated);
        //-------------------------FIN DE RECYCLER PARA TOPRATED----------------------------------//

        //-------------------------COMIENZO DE RECYCLER PARA HIGHEST GROSSED----------------------------------//
        recyclerViewPeliculasHighestGrossing = (RecyclerView) unaVistaADevolver.findViewById(R.id.listasDePelis_recyclerHighestGrossing);
        LinearLayoutManager linearLayoutManagerHighestGrossing = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPeliculasHighestGrossing.setLayoutManager(linearLayoutManagerHighestGrossing);

        unAdapterPeliculasHighestGrossing = new AdapterRecyclerSoloImagen(getActivity());
        unAdapterPeliculasHighestGrossing.setListener(new ListenerPeliculasSoloImagen(recyclerViewPeliculasHighestGrossing,unAdapterPeliculasHighestGrossing));


        peliculaController.obtenerListaDePeliculasTMDB(TMDBHelper.getHighestGrossingMovies(TMDBHelper.language_ENGLISH, 1,"2016"), getActivity(), new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieDB pelicuslaHighestGrossing = (ContainerMovieDB) resultado;

                unAdapterPeliculasHighestGrossing.setListaDePeliculas(pelicuslaHighestGrossing.getResult());
                unAdapterPeliculasHighestGrossing.notifyDataSetChanged();
            }
        });

        recyclerViewPeliculasHighestGrossing.setAdapter(unAdapterPeliculasHighestGrossing);
        //-------------------------FIN DE RECYCLER PARA HIGHESTGROSSED----------------------------------//



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

            ComunicadorFragmentActivity unComunicador = (ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEstaPelicula(peliculaTocada);


        }
    }
    public interface ComunicadorFragmentActivity{
        void clickearonEstaPelicula(MovieDB peliculaClickeada);

    }
}
