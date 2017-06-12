package com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB;

import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 10/22/2016.
 */

public class WrapperPeliculas {
    @SerializedName("peliculas")
    private ArrayList<Pelicula> listaDePeliculas;

    public ArrayList<Pelicula> getListaDePeliculas() {
        return listaDePeliculas;
    }
}
