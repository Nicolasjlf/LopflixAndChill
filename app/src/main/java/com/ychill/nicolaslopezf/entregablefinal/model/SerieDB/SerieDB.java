package com.ychill.nicolaslopezf.entregablefinal.model.SerieDB;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nicolas Lopez F on 11/17/2016.
 */

public class SerieDB {

    private String id;
    @SerializedName("poster_path")
    private String poster;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SerieDB{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerieDB serieDB = (SerieDB) o;

        return id.equals(serieDB.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
