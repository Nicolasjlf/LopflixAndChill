package com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santiagoiraola on 11/10/16.
 */

public class AdapterRecyclerSoloImagen extends RecyclerView.Adapter {

    private Context unContexto;
    private List<MovieDB> listaDePeliculas;
    private View.OnClickListener listener;


    public AdapterRecyclerSoloImagen(Context unContexto) {
        this.unContexto = unContexto;
        this.listener = listener;
        this.listaDePeliculas = new ArrayList<>();
    }

    public void setListaDePeliculas(List<MovieDB> listaDePeliculas) {
        this.listaDePeliculas = listaDePeliculas;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public MovieDB getPeliculaAtPosition(Integer position){
        return listaDePeliculas.get(position);
    }

    public void agregarPeliculasAlRecycle(ArrayList<MovieDB> peliculasAgregadas){
        listaDePeliculas.addAll(peliculasAgregadas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(unContexto);

        View vistaADevolver = inflater.inflate(R.layout.layout_solo_imagen, parent, false);

        vistaADevolver.setOnClickListener(listener);
        PeliculasSoloImagenViewHolder unViewHolderDePeliculas = new PeliculasSoloImagenViewHolder(vistaADevolver);
        return unViewHolderDePeliculas;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieDB peliculaABindear = listaDePeliculas.get(position);
        PeliculasSoloImagenViewHolder unaPeliculaViewHolder = (PeliculasSoloImagenViewHolder) holder;
        unaPeliculaViewHolder.bindImagen(peliculaABindear);
    }

    @Override
    public int getItemCount() {
        return listaDePeliculas.size();
    }


    private class PeliculasSoloImagenViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagenPelicula;


        public PeliculasSoloImagenViewHolder(View itemView){
            super(itemView);
            imagenPelicula = (ImageView)itemView.findViewById(R.id.layoutsoloimagen_imageview);
        }
        public void bindImagen(MovieDB unaPelicula){
            Picasso.with(unContexto).load(TMDBHelper.getImagePoster(TMDBHelper.IMAGE_SIZE_W185, unaPelicula.getPoster())).
                    error(R.mipmap.ic_launcher).into(imagenPelicula);
        }
    }

}
