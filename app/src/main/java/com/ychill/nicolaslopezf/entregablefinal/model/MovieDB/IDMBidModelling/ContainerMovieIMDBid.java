package com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.IDMBidModelling;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by santiagoiraola on 11/25/16.
 */

public class ContainerMovieIMDBid {


    @SerializedName("movie_results")
    private ArrayList<MovieResult> movieResult;


    public ArrayList<MovieResult> getMovieResult() {
        return movieResult;
    }
}
