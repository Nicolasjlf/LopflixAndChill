package com.ychill.nicolaslopezf.entregablefinal.view.viewsParaSeries;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.SerieDB.SerieDB;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.AdapterRecyclerSoloImagen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Lopez F on 11/17/2016.
 */

//
//peliculaController.obtenerSeriesTMDB(TMDBHelper.getTVPopular(TMDBHelper.language_ENGLISH, 1), this, new ResultListener() {
//@Override
//public void finish(Object resultado) {
//        ContainerSerieDB containerSerieDB = (ContainerSerieDB) resultado;
//        ArrayList<SerieDB> series = containerSerieDB.getResults();
//        Toast.makeText(ActivityLogin.this, series.toString(), Toast.LENGTH_LONG).show();
//
//        }
//        });

public class AdapterRecyclerSeries extends RecyclerView.Adapter {

    private Context unContexto;
    private List<SerieDB> listaDeSeries;
    private View.OnClickListener listener;


    public AdapterRecyclerSeries(Context unContexto) {
        this.unContexto = unContexto;
        this.listener = listener;
        this.listaDeSeries = new ArrayList<>();
    }

    public void setListaDeSeries(List<SerieDB> listaDePeliculas) {
        this.listaDeSeries = listaDePeliculas;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public SerieDB getSerieAtPosition(Integer position){
        return listaDeSeries.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(unContexto);

        View vistaADevolver = inflater.inflate(R.layout.layout_solo_imagen, parent, false);

        vistaADevolver.setOnClickListener(listener);
        AdapterRecyclerSeries.SeriesSoloImagenViewHolder unViewHolderDePeliculas = new AdapterRecyclerSeries.SeriesSoloImagenViewHolder(vistaADevolver);
        return unViewHolderDePeliculas;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SerieDB peliculaABindear = listaDeSeries.get(position);
        AdapterRecyclerSeries.SeriesSoloImagenViewHolder unaPeliculaViewHolder = (AdapterRecyclerSeries.SeriesSoloImagenViewHolder) holder;
        unaPeliculaViewHolder.bindImagen(peliculaABindear);
    }

    @Override
    public int getItemCount() {
        return listaDeSeries.size();
    }


    private class SeriesSoloImagenViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagenPelicula;


        public SeriesSoloImagenViewHolder(View itemView){
            super(itemView);
            imagenPelicula = (ImageView)itemView.findViewById(R.id.layoutsoloimagen_imageview);
        }
        public void bindImagen(SerieDB unaSerie){
            Picasso.with(unContexto).load(TMDBHelper.getImagePoster(TMDBHelper.IMAGE_SIZE_W185, unaSerie.getPoster())).
                    error(R.mipmap.ic_launcher).into(imagenPelicula);
        }
    }
    {
}}
