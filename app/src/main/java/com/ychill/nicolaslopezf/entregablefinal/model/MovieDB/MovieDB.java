package com.ychill.nicolaslopezf.entregablefinal.model.MovieDB;

import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.PeliculaPadre;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/8/2016.
 */

public class MovieDB extends PeliculaPadre {

    public String id;
    private String title;
    @SerializedName("vote_average")
    private String rated;
    @SerializedName("release_date")
    private String released;
    @SerializedName("genre_ids")
    private ArrayList<String> genreid;
    @SerializedName("overview")
    private String plot;
    @SerializedName("poster_path")
    private String poster;

    //estos atributos solo te lo da pedidos especificos de la pelicula
    private String imdb_id;
   // @SerializedName("vote_average")
    //private String rating;
    private ArrayList<Genre> genre;


    @Override
    public String toString() {
        return "MovieDB{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", imdbID='" + imdb_id + '\'' +
                ", genre=" + genre +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public ArrayList<String> getGenreid() {
        return genreid;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getImdbID() {
        return imdb_id;
    }

    public ArrayList<Genre> getGenre() {
        return genre;
    }
}
