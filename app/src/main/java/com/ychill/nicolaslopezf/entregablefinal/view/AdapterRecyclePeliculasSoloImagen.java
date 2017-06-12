package com.ychill.nicolaslopezf.entregablefinal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Lopez F on 10/31/2016.
 */

public class AdapterRecyclePeliculasSoloImagen extends RecyclerView.Adapter {

    private Context unContexto;
    private List<Pelicula> listaDePeliculas;
    private View.OnClickListener listener;

    public AdapterRecyclePeliculasSoloImagen(List<Pelicula> listaDePeliculas, Context unContexto) {
        this.listaDePeliculas = listaDePeliculas;
        this.unContexto = unContexto;
    }

    public AdapterRecyclePeliculasSoloImagen(Context unContexto, List<Pelicula> listaDePeliculas, View.OnClickListener listener) {
        this.unContexto = unContexto;
        this.listaDePeliculas = listaDePeliculas;
        this.listener = listener;
    }

    public Pelicula getPeliculaAtPosition(Integer position){
        return listaDePeliculas.get(position);
    }

    public void agregarPeliculasAlRecycle(ArrayList<Pelicula> peliculasAgregadas){
        listaDePeliculas.addAll(peliculasAgregadas);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(unContexto);
        View viewADevolver = inflater.inflate(R.layout.layout_solo_imagen,parent,false);
        viewADevolver.setOnClickListener(listener);
        PeliculasViewHolderSoloImagen peliculasViewHolderSoloImagen = new PeliculasViewHolderSoloImagen(viewADevolver);
        return peliculasViewHolderSoloImagen;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Pelicula peliculaABindear = listaDePeliculas.get(position);
        PeliculasViewHolderSoloImagen peliculasViewHolderSoloImagen = (PeliculasViewHolderSoloImagen) holder;
        peliculasViewHolderSoloImagen.bindPelicula(peliculaABindear);
    }

    @Override
    public int getItemCount() {
        return listaDePeliculas.size();
    }

    private class PeliculasViewHolderSoloImagen extends RecyclerView.ViewHolder{
        private ImageView imagenPelicula;


        public PeliculasViewHolderSoloImagen(View itemView){
            super(itemView);
            imagenPelicula = (ImageView) itemView.findViewById(R.id.layoutsoloimagen_imageview);
        }
        public void bindPelicula(Pelicula unaPelicula){

            Picasso.with(unContexto).load(unaPelicula.getPoster()).
                    error(R.mipmap.ic_launcher).into(imagenPelicula);

        }
    }
}
