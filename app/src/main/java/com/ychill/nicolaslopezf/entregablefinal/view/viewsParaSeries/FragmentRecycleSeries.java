package com.ychill.nicolaslopezf.entregablefinal.view.viewsParaSeries;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.model.SerieDB.ContainerSerieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.SerieDB.SerieDB;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/17/2016.
 */

public class FragmentRecycleSeries extends Fragment {

    private RecyclerView recyclerViewSeries1;
    private AdapterRecyclerSeries unAdapterSeries1;

    private RecyclerView recyclerViewSeries2;
    private AdapterRecyclerSeries unAdapterSeries2;

    private RecyclerView recyclerViewSeries3;
    private AdapterRecyclerSeries unAdapterSeries3;


    private PeliculaController peliculaController;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View unaVistaADevolver = inflater.inflate(R.layout.fragment_listas_de_pelis, container, false);

        //CONTROLLER SE USA PARA TODOS LOS RECYCLERS
        peliculaController = new PeliculaController();


        //-------------------------COMIENZO DE RECYCLER PARA TRENDING----------------------------------//

        TextView textViewAiringToday = (TextView) unaVistaADevolver.findViewById(R.id.listasDePelis_textoHighestGrossing);
        textViewAiringToday.setText("AiringToday");

        recyclerViewSeries1 = (RecyclerView) unaVistaADevolver.findViewById(R.id.listasDePelis_recyclerTrending);
        LinearLayoutManager linearLayoutManagerTrending = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeries1.setLayoutManager(linearLayoutManagerTrending);

        recyclerViewSeries2 = (RecyclerView) unaVistaADevolver.findViewById(R.id.listasDePelis_recyclerTopRated);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeries2.setLayoutManager(linearLayoutManager2);

        //TENGO QUE SETEAR EL LISTENER POR SEPARADO PORQUE SI EN LA MISMA LINEA CREO EL NEW ADAPTER Y LO SETEO AL LISTENER POR CONSTRUCTOR TIRA NULLPOINTER.
        unAdapterSeries1 = new AdapterRecyclerSeries(getActivity());
        unAdapterSeries2 = new AdapterRecyclerSeries(getActivity());
        unAdapterSeries3 = new AdapterRecyclerSeries(getActivity());

        peliculaController.obtenerSeriesTMDB(TMDBHelper.getTVPopular(TMDBHelper.language_ENGLISH, 1), getActivity(), new ResultListener() {
        @Override
        public void finish(Object resultado) {
            ContainerSerieDB containerSerieDB = (ContainerSerieDB) resultado;
            ArrayList<SerieDB> series = containerSerieDB.getResults();
            unAdapterSeries1.setListaDeSeries(series);
            unAdapterSeries1.notifyDataSetChanged();

        }
        });

        recyclerViewSeries1.setAdapter(unAdapterSeries1);


        peliculaController.obtenerSeriesTMDB(TMDBHelper.getTVTopRated(TMDBHelper.language_ENGLISH, 1), getActivity(), new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerSerieDB containerSerieDB = (ContainerSerieDB) resultado;
                ArrayList<SerieDB> series = containerSerieDB.getResults();
                unAdapterSeries2.setListaDeSeries(series);
                unAdapterSeries2.notifyDataSetChanged();
            }
        });

        recyclerViewSeries2.setAdapter(unAdapterSeries2);

        recyclerViewSeries3 = (RecyclerView) unaVistaADevolver.findViewById(R.id.listasDePelis_recyclerHighestGrossing);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSeries3.setLayoutManager(linearLayoutManager3);



        peliculaController.obtenerSeriesTMDB(TMDBHelper.getTVAiringToday(TMDBHelper.language_ENGLISH, 1), getActivity(), new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerSerieDB containerSerieDB = (ContainerSerieDB) resultado;
                ArrayList<SerieDB> series = containerSerieDB.getResults();
                unAdapterSeries3.setListaDeSeries(series);
                unAdapterSeries3.notifyDataSetChanged();

            }
        });
        recyclerViewSeries3.setAdapter(unAdapterSeries3);

        return unaVistaADevolver;
    }

    public class ListenerSeriesSoloImagen implements View.OnClickListener {

        private RecyclerView recyclerViewAUsar;
        private AdapterRecyclerSeries adapterAUsar;

        public ListenerSeriesSoloImagen(RecyclerView recyclerViewAUsar, AdapterRecyclerSeries adapterAUsar) {
            this.recyclerViewAUsar = recyclerViewAUsar;
            this.adapterAUsar = adapterAUsar;
        }

        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewAUsar.getChildAdapterPosition(view);
            SerieDB serieTocada = adapterAUsar.getSerieAtPosition(posicionTocada);

            Log.d("imagen",serieTocada.getPoster());

            ComunicadorFragmentActivity unComunicador = (ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEstaSerie(serieTocada);


        }
    }
    public interface ComunicadorFragmentActivity{
        void clickearonEstaSerie(SerieDB serieClickeada);

    }
}
