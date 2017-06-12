package com.ychill.nicolaslopezf.entregablefinal.model.MovieDB;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by santiagoiraola on 11/13/16.
 */

public class MovieDBTrailerContainer {

    private String id;
    @SerializedName("results")
    private ArrayList<TrailerDB> trailerArrayList;


    public String getId() {
        return id;
    }

    public ArrayList<TrailerDB> getTrailerArrayList() {
        return trailerArrayList;
    }

    @Override
    public String toString() {
        return "MovieDBTrailerContainer{" +
                "id='" + id + '\'' +
                ", trailerArrayList=" + trailerArrayList +
                '}';
    }
}
