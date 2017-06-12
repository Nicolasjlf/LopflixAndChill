package com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.IDMBidModelling;

import com.google.gson.annotations.SerializedName;

/**
 * Created by santiagoiraola on 11/25/16.
 */

public class MovieResult {

    @SerializedName("id")
    private String tmdbID;

    @SerializedName("original_title")
    private String movieTitle;


    public String getTmdbID() {
        return tmdbID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}
