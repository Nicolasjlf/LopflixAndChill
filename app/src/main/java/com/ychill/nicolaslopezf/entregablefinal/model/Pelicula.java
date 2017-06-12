package com.ychill.nicolaslopezf.entregablefinal.model;


import com.ychill.nicolaslopezf.entregablefinal.dao.PeliculaDAO;
import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicolas Lopez F on 10/21/2016.
 */

public class Pelicula extends PeliculaPadre {

    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("Rated")
    private String rated;
    @SerializedName("Released")
    private String released;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Genre")
    private String genre;
    @SerializedName("Actors")
    private String actors;
    @SerializedName("Director")
    private String directors;
    @SerializedName("Plot")
    private String plot;
    @SerializedName("Language")
    private String language;
    @SerializedName("Awards")
    private String awards;
    private String imdbRating;
    @SerializedName("Poster")
    private String poster;
    private String imdbID;

    public Pelicula(String imdbID, String title, String year, String rated, String released, String runtime, String genre, String actors, String directors, String plot, String language, String awards, String imdbRating, String poster) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.directors = directors;
        this.plot = plot;
        this.language = language;
        this.awards = awards;
        this.imdbRating = imdbRating;
        this.poster = poster;

    }

    public Pelicula(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pelicula pelicula = (Pelicula) o;

        if (title != null ? !title.equals(pelicula.title) : pelicula.title != null) return false;
        if (year != null ? !year.equals(pelicula.year) : pelicula.year != null) return false;
        if (rated != null ? !rated.equals(pelicula.rated) : pelicula.rated != null) return false;
        if (released != null ? !released.equals(pelicula.released) : pelicula.released != null)
            return false;
        if (runtime != null ? !runtime.equals(pelicula.runtime) : pelicula.runtime != null)
            return false;
        if (genre != null ? !genre.equals(pelicula.genre) : pelicula.genre != null) return false;
        if (actors != null ? !actors.equals(pelicula.actors) : pelicula.actors != null)
            return false;
        if (directors != null ? !directors.equals(pelicula.directors) : pelicula.directors != null)
            return false;
        if (plot != null ? !plot.equals(pelicula.plot) : pelicula.plot != null) return false;
        if (language != null ? !language.equals(pelicula.language) : pelicula.language != null)
            return false;
        if (awards != null ? !awards.equals(pelicula.awards) : pelicula.awards != null)
            return false;
        if (imdbRating != null ? !imdbRating.equals(pelicula.imdbRating) : pelicula.imdbRating != null)
            return false;
        if (poster != null ? !poster.equals(pelicula.poster) : pelicula.poster != null)
            return false;
        return imdbID != null ? imdbID.equals(pelicula.imdbID) : pelicula.imdbID == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (rated != null ? rated.hashCode() : 0);
        result = 31 * result + (released != null ? released.hashCode() : 0);
        result = 31 * result + (runtime != null ? runtime.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (actors != null ? actors.hashCode() : 0);
        result = 31 * result + (directors != null ? directors.hashCode() : 0);
        result = 31 * result + (plot != null ? plot.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (awards != null ? awards.hashCode() : 0);
        result = 31 * result + (imdbRating != null ? imdbRating.hashCode() : 0);
        result = 31 * result + (poster != null ? poster.hashCode() : 0);
        result = 31 * result + (imdbID != null ? imdbID.hashCode() : 0);
        return result;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", imdbID);

        return result;
    }
}



