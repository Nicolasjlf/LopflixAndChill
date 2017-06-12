package com.ychill.nicolaslopezf.entregablefinal.model.MovieDB;

/**
 * Created by Nicolas Lopez F on 11/8/2016.
 */

public class Genre {
    public String id;
    public String name;

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        return id != null ? id.equals(genre.id) : genre.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
