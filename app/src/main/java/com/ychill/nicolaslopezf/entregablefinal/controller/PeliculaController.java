package com.ychill.nicolaslopezf.entregablefinal.controller;

import android.content.Context;
import android.util.Log;

import com.ychill.nicolaslopezf.entregablefinal.dao.PeliculaDAO;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.IDMBidModelling.ContainerMovieIMDBid;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDBTrailerContainer;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.model.SerieDB.ContainerSerieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 10/22/2016.
 */

public class PeliculaController {

    public void checkIfMovieIsInFirebase(final Pelicula pelicula, Context context, final ResultListener<Boolean> listenerFromView){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        FirebaseUser usuarioLogeado = mAuth.getCurrentUser();
        database.getReference("users").child(usuarioLogeado.getUid()).child("watchlist").child(pelicula.getImdbID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean isInFirebase;

                if(dataSnapshot.exists()){

                    isInFirebase = true;
                    listenerFromView.finish(isInFirebase);
                }
                else{
                    isInFirebase = false;
                    listenerFromView.finish(isInFirebase);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addMovieToFirebaseFavorites(Pelicula pelicula, Context context){

        // codigo que agrega una pelicula a la lista de favoritos de firbase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("users");
        FirebaseAuth mAuth;
        mAuth  = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        ArrayList<String> container = new ArrayList<>();
        container.add(pelicula.getImdbID());
        container.add(pelicula.getTitle());
        databaseReference.child(user.getUid()).child("watchlist").child(pelicula.getImdbID()).setValue(pelicula);

    }

    public void removeMovieFromFirebaseFavorites(Pelicula pelicula, Context context){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth;
        mAuth  = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        database.getReference("users").child(user.getUid()).child("watchlist").child(pelicula.getImdbID()).equalTo(pelicula.getImdbID()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().
                                setValue(null);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }


    public void obtenerPeliculaPorID(Context context, String imdbID, final ResultListener<Pelicula> resultListenerView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        peliculaDAO.damePeliculaPorID(context, new ResultListener<Pelicula>() {
            @Override
            public void finish(Pelicula resultado) {
               resultListenerView.finish(resultado);
            }
        }, imdbID);

    }

    public ArrayList<Pelicula> damePeliculasPorGenero(String genero, Context context){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        ArrayList<Pelicula> todasLasPeliculas = peliculaDAO.dameListaPeliculas(context);
        //ArrayList<Pelicula> peliculasPorGenero = busquedaPorGenero(todasLasPeliculas,genero);
        ArrayList<Pelicula> peliculasPorGenero = encontrarMismoGenero(todasLasPeliculas,genero);


        return peliculasPorGenero;
    }
    public void addPeliculaFavoritaToDatabase(Context context, Pelicula pelicula){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        if(!peliculaDAO.getAllPeliculasFavoritasFromDatabase().contains(pelicula)){
            peliculaDAO.addPeliculaFavoritaToDatabase(pelicula);
        }
    }

    public Boolean esFavorita(Pelicula pelicula, Context context){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        Boolean respuesta = false;
        if (peliculaDAO.getAllPeliculasFavoritasFromDatabase().contains(pelicula)){
            respuesta = true;
        }
        return respuesta;
    }

    public void changeFavoriteStatus(Context context, Pelicula pelicula, String imdbID){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        if(!esFavorita(pelicula,context)){
            //peliculaDAO.addIDsToTable(imdbID,tmdbID);
            peliculaDAO.addPeliculaFavoritaToDatabase(pelicula);
        }else{
            peliculaDAO.removePeliculaFavoritaFromDatabase(pelicula);
        }
    }




    //EL FOREACH QUE BUSCA LAS PELICULAS DEL GENERO QUE LE INDICAMOS COMO PARAMETRO. DEVUELVE UN NUEVO ARRAYLIST
    private ArrayList<Pelicula> busquedaPorGenero(ArrayList<Pelicula> peliculas, String genero){

        ArrayList<Pelicula> peliculasPorGenero = new ArrayList<>();



        for(Pelicula unaPelicula:peliculas){

            if(unaPelicula.getGenre().contains(genero)){

                peliculasPorGenero.add(unaPelicula);
            }
        }

        return peliculasPorGenero;
    }

    private ArrayList<Pelicula> encontrarMismoGenero(ArrayList<Pelicula> peliculas, String genero){
        ArrayList<Pelicula> peliculasPorGenero = new ArrayList<>();
        ArrayList<String> generos = new ArrayList<String>();
        generos.add("Comedy");
        generos.add("Animation");
        generos.add("Action");
        generos.add("Thriller");
        generos.add("War");
        generos.add("Horror");
        generos.add("Sci-Fi");
        generos.add("Family");

        for (String g: generos) {
            if (genero.contains(g)){
                for (Pelicula pelicula: peliculas) {
                    if (pelicula.getGenre().contains(g) && !peliculasPorGenero.contains(pelicula)){
                        peliculasPorGenero.add(pelicula);
                    }
                }
            }
        }
        return peliculasPorGenero;
    }

    public ArrayList<Pelicula> dameTodasLasPeliculas(Context context){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        ArrayList<Pelicula> arrayADevolver;
        arrayADevolver = peliculaDAO.dameListaPeliculas(context);
        return arrayADevolver;
    }


    //OBTENER PELICULAS DE TMDB
    public void obtenerListaDePeliculasTMDB(String TMDBurl, Context context, final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
//        String url = TMDBHelper.getPopularMovies(TMDBHelper.language_ENGLISH, 1);
        peliculaDAO.flasehadaCreadoraDeAsync(new ContainerMovieDB(), TMDBurl, new ResultListener<Object>() {
                    @Override
                    public void finish(Object resultado) {
                        listenerFromView.finish(resultado);
                    }
                }
        ,context);
    }

    //Para armar las peliculas similares con el tmdbID a partir del imdbID
    public void obtenerTMDBidConIMDBid(String imdbID, Context context, final ResultListener listenerFromView){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        peliculaDAO.flasehadaCreadoraDeAsync(new ContainerMovieIMDBid(), TMDBHelper.getMovieWithImdbID(imdbID),
                new ResultListener<Object>() {

                    @Override
                    public void finish(Object resultado) {
                        listenerFromView.finish(resultado);
                    }
                },context);
    }

    public void  obtenerPeliculaTMDB(String movieID,Context context, final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        String url = TMDBHelper.getMovieDetailURL(movieID,TMDBHelper.language_ENGLISH);
        peliculaDAO.flasehadaCreadoraDeAsync(new MovieDB(), url, new ResultListener<Object>() {

            @Override
            public void finish(Object resultado) {
                listenerFromView.finish(resultado);
            }
        },context);
    }


    public void obtenerPeliculasPorGenero(String genero, Context context ,final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
        String generoID = null;
        switch (genero){
            case "Action" : generoID= TMDBHelper.MOVIE_GENRE_ACTION;
                break;
            case "Comedy" : generoID= "35";
                break;
            case "Horror" : generoID= "27";
                break;
            case "Adventure" : generoID= "12";
                break;
            case "Animation" : generoID= "16";
                break;
            case "Crime" : generoID= "80";
                break;
            case "Documentary" : generoID= "99";
                break;
            case "Drama" : generoID= "18";
                break;
            case "Family" : generoID= "10751";
                break;
            case "History" : generoID= "36";
                break;
            case "Music" : generoID= "10402";
                break;
            case "Mistery" : generoID= "9648";
                break;
            case "Romance" : generoID= "10749";
                break;
            case "Sci-Fi" : generoID= "878";
                break;
        }
        String url = TMDBHelper.getMoviesByGenre(generoID,1,TMDBHelper.language_ENGLISH);
        peliculaDAO.flasehadaCreadoraDeAsync(new ContainerMovieDB(), url, new ResultListener<Object>() {
            @Override
            public void finish(Object resultado) {
                listenerFromView.finish(resultado);
            }
        },context);
    }

    public String obtenerTMDBidDeBaseDeDatos(Context context,String imdbID){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        return peliculaDAO.getTMDBidFromDataBase(imdbID);
    }

    public void obtenerTrailerDePeliculaTMDB(Context context,String tmdbID, final ResultListener listenerFromView){

        PeliculaDAO peliculaDAO = new PeliculaDAO(context);

        peliculaDAO.flasehadaCreadoraDeAsync(new MovieDBTrailerContainer(), TMDBHelper.getTrailerURL(tmdbID, TMDBHelper.language_ENGLISH),
                new ResultListener<Object>() {

                    @Override
                    public void finish(Object resultado) {
                        listenerFromView.finish(resultado);
                    }
                },context);
    }
    public void obtenerSeriesTMDB(String TMDBurl, Context context, final ResultListener listenerFromView){
        PeliculaDAO peliculaDAO = new PeliculaDAO(context);
//        String url = TMDBHelper.getPopularMovies(TMDBHelper.language_ENGLISH, 1);
        peliculaDAO.flasehadaCreadoraDeAsync(new ContainerSerieDB(), TMDBurl, new ResultListener<Object>() {
                    @Override
                    public void finish(Object resultado) {
                        listenerFromView.finish(resultado);
                    }
                }
                ,context);
    }
}
