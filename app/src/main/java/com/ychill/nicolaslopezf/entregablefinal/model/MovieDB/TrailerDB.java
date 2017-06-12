package com.ychill.nicolaslopezf.entregablefinal.model.MovieDB;

import com.google.gson.annotations.SerializedName;

/**
 * Created by santiagoiraola on 11/13/16.
 */

public class TrailerDB {

    @SerializedName("key")
    private String trailerUrl;

    @SerializedName("site")
    private String trailerSite;


    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    @Override
    public String toString() {
        return "TrailerDB{" +
                "trailerUrl='" + trailerUrl + '\'' +
                ", trailerSite='" + trailerSite + '\'' +
                '}';
    }
}
