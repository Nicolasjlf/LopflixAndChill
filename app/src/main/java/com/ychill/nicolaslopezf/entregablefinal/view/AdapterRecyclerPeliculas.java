package com.ychill.nicolaslopezf.entregablefinal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mora on 21/10/2016.
 */

public class AdapterRecyclerPeliculas extends RecyclerView.Adapter {
    private Context unContexto;
    private List<Pelicula> listaDePeliculas;
    private View.OnClickListener listener;

    public AdapterRecyclerPeliculas(Context unContexto, List<Pelicula> listaDePeliculas, View.OnClickListener listener) {
        this.unContexto = unContexto;
        this.listaDePeliculas = listaDePeliculas;
        this.listener = listener;
    }

    public void setListaDePeliculas(List<Pelicula> listaDePeliculas) {
        this.listaDePeliculas = listaDePeliculas;
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

        View vistaADevolver = inflater.inflate(R.layout.fragment_pelicula_item, parent, false);
        vistaADevolver.setOnClickListener(listener);
        PeliculasViewHolder unViewHolderDePeliculas = new PeliculasViewHolder(vistaADevolver);
        return unViewHolderDePeliculas;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Pelicula peliculaABindear = listaDePeliculas.get(position);
        PeliculasViewHolder unaPeliculaViewHolder = (PeliculasViewHolder) holder;
        unaPeliculaViewHolder.bindReceta(peliculaABindear);


    }

    @Override
    public int getItemCount() {
        return listaDePeliculas.size();
    }


    //VIEWHOLDERS CLASS QUE IDENTIFICA QUE ELEMENTOS NECESITAMOS
    private class PeliculasViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagenPelicula;
        private TextView nombrePelicula;
        private TextView dateOfRelease;
        private TextView nombreDirector;

        public PeliculasViewHolder(View itemView){
            super(itemView);
            imagenPelicula = (ImageView)itemView.findViewById(R.id.ivPelicula);
            nombrePelicula = (TextView)itemView.findViewById(R.id.tvNombrePelicula);



        }
        public void bindReceta(Pelicula unaPelicula){
            Picasso.with(unContexto).load(unaPelicula.getPoster()).
                    error(R.mipmap.ic_launcher).into(imagenPelicula);

            nombrePelicula.setText(unaPelicula.getTitle());
            dateOfRelease.setText(unaPelicula.getReleased());
            nombreDirector.setText("Directed by: " + unaPelicula.getDirectors());
        }
    }
}
