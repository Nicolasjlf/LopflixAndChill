package com.ychill.nicolaslopezf.entregablefinal.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.ContainerPeliculaFireBase;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.model.Trackt.WrapperPeliculaTrckt;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.WrapperPeliculas;
import com.ychill.nicolaslopezf.entregablefinal.utils.HTTPConnectionManager;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 10/22/2016.
 */



public class PeliculaDAO extends SQLiteOpenHelper{

    //CONSTANTES PARA LOS NOMBRES DE LA BD Y LOS CAMPOS
    private static final String DATABASENAME = "WatchlistDB";
    private static final Integer DATABASEVERSION = 1;

    //TABLA PELICULA CON SUS CAMPOS
    private static final String TABLEWATCHLIST = "Pelicula";
    private static final String IMDBID = "ImdbID";
    private static final String TITLE = "title";
    private static final String YEAR = "year";
    private static final String RATED = "rated";
    private static final String RELEASED = "released";
    private static final String RUNTIME = "runtime";
    private static final String GENRE = "genre";
    private static final String ACTORS = "actors";
    private static final String DIRECTORS = "directors";
    private static final String PLOT = "plot";
    private static final String LANGUAGE = "language";
    private static final String AWARDS = "awards";
    private static final String IMDBRATING= "imdbrating";
    private static final String POSTER = "poster";

    //TABLA IMDBID Y TMDB

    private static final String IDDATABASE_NAME = "idDatabase";
    private static final String TMDB_ID = "tmdbID";




    //EL CONSTRUCTOR CREA LA BASE DE DATOS
    public PeliculaDAO(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        //CREA LA ESTRUCTURA DE LA BASE DE DATOS, ES DECIR LA TABLA

        String createTable = "CREATE TABLE " + TABLEWATCHLIST + "("
                + IMDBID + " TEXT PRIMARY KEY, " +
                TITLE + " TEXT, " +
                YEAR + " TEXT, " +
                RATED + " TEXT, " +
                RELEASED + " TEXT, " +
                RUNTIME + " TEXT, " +
                GENRE + " TEXT, " +
                ACTORS + " TEXT, " +
                DIRECTORS + " TEXT, " +
                PLOT + " TEXT, " +
                LANGUAGE + " TEXT, " +
                AWARDS + " TEXT, " +
                IMDBRATING + " TEXT, " +
                POSTER + " TEXT"   +")";

        String createIDTable = "CREATE TABLE " + IDDATABASE_NAME + "(" +
                IMDBID + " TEXT PRIMARY KEY, " +
                TMDB_ID + " TEXT" + ")";

        database.execSQL(createTable);
        database.execSQL(createIDTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addIDsToTable(String imdbID, String tmdbID){

        SQLiteDatabase database = getWritableDatabase();

        ContentValues row = new ContentValues();

        row.put(IMDBID, imdbID);
        row.put(TMDB_ID,tmdbID);

        database.insert(IDDATABASE_NAME,null,row);
    }

    public String getTMDBidFromDataBase(String imdbID){

        SQLiteDatabase database = getReadableDatabase();

        String tmdbID = null;

        String findID = "SELECT * FROM " + IDDATABASE_NAME + " WHERE " + IMDBID + " = " + "'" + imdbID + "'";

        Cursor cursor = database.rawQuery(findID,null);


        while (cursor.moveToNext()){

            tmdbID = cursor.getString(cursor.getColumnIndex(TMDB_ID));
        }



        return tmdbID;
    }

    //CONSTRUIR UN METODO QUE AGREGUE UN POST A LA BASE DE DATOS
    public void addPeliculaFavoritaToDatabase(Pelicula pelicula){

        //PIDE UNA CONEXION DE ESCRITURA A LA BD
        SQLiteDatabase database = getWritableDatabase();

        //GENERA LA FILA DE LA TABLA
        ContentValues row = new ContentValues();


            //Obtengo los datos y los cargo en el row
            row.put(IMDBID, pelicula.getImdbID());
            row.put(TITLE, pelicula.getTitle());
            row.put(YEAR, pelicula.getYear());
            row.put(RATED, pelicula.getRated());
            row.put(RELEASED, pelicula.getReleased());
            row.put(RUNTIME, pelicula.getRuntime());
            row.put(GENRE, pelicula.getGenre());
            row.put(ACTORS, pelicula.getActors());
            row.put(DIRECTORS, pelicula.getDirectors());
            row.put(PLOT, pelicula.getPlot());
            row.put(LANGUAGE, pelicula.getLanguage());
            row.put(AWARDS, pelicula.getAwards());
            row.put(IMDBRATING, pelicula.getImdbRating());
            row.put(POSTER, pelicula.getPoster());


            //INSERTA EN LA DATABASE EN LA TABLA POST LA FILA CREADA
            database.insert(TABLEWATCHLIST, null, row);
            database.close();
        }
    public void removePeliculaFavoritaFromDatabase(Pelicula pelicula){

        SQLiteDatabase database = getWritableDatabase();
        String deletePelicula = "DELETE FROM " + TABLEWATCHLIST + " WHERE " + IMDBID +" = " + "'" + pelicula.getImdbID() +"'";

        database.execSQL(deletePelicula);

        database.close();

        }

    public ArrayList<Pelicula> getAllPeliculasFavoritasFromDatabase(){

        SQLiteDatabase database = getReadableDatabase();

        ArrayList<Pelicula> peliculaList = new ArrayList<>();

        String select = "SELECT * FROM " + TABLEWATCHLIST;

        Cursor cursor = database.rawQuery(select, null);

        //MIENTRAS HAYA FILAS PARA LEER
        while(cursor.moveToNext()){

            //LEER LA PELICULA
            Pelicula pelicula = new Pelicula();

            pelicula.setImdbID(cursor.getString(cursor.getColumnIndex(IMDBID)));
            pelicula.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            pelicula.setYear(cursor.getString(cursor.getColumnIndex(YEAR)));
            pelicula.setRated(cursor.getString(cursor.getColumnIndex(RATED)));
            pelicula.setReleased(cursor.getString(cursor.getColumnIndex(RELEASED)));
            pelicula.setRuntime(cursor.getString(cursor.getColumnIndex(RUNTIME)));
            pelicula.setGenre(cursor.getString(cursor.getColumnIndex(GENRE)));
            pelicula.setActors(cursor.getString(cursor.getColumnIndex(ACTORS)));
            pelicula.setDirectors(cursor.getString(cursor.getColumnIndex(DIRECTORS)));
            pelicula.setPlot(cursor.getString(cursor.getColumnIndex(PLOT)));
            pelicula.setLanguage(cursor.getString(cursor.getColumnIndex(LANGUAGE)));
            pelicula.setAwards(cursor.getString(cursor.getColumnIndex(AWARDS)));
            pelicula.setImdbRating(cursor.getString(cursor.getColumnIndex(IMDBRATING)));
            pelicula.setPoster(cursor.getString(cursor.getColumnIndex(POSTER)));

            peliculaList.add(pelicula);
        }

        cursor.close();
        database.close();
        return peliculaList;
    }



    
    public void damePeliculaPorID(Context context,ResultListener<Pelicula> resultListener, String imdbID){
        LectorArchivoJsonAsync lectorArchivoJsonAsync = new LectorArchivoJsonAsync(imdbID,resultListener, context);
        lectorArchivoJsonAsync.execute();
    }

    public ArrayList<Pelicula> dameListaPeliculas(Context context){
        WrapperPeliculas listPeliculaADevolver = null;
        try{
            AssetManager manager = context.getAssets();
            InputStream listaDePeliculasJson = manager.open("Peliculas.json");
            BufferedReader bufferReaderIn = new BufferedReader(new InputStreamReader(listaDePeliculasJson));
            Gson gson = new Gson();
            listPeliculaADevolver = gson.fromJson(bufferReaderIn, WrapperPeliculas.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listPeliculaADevolver.getListaDePeliculas();

    }

    public void dameTrendingPelisTrakt (Context context, ResultListener<WrapperPeliculaTrckt> peliculaTrcktResultListener){

        LectorArchivoJsonTraktAsync lectorArchivoJsonTraktAsync = new LectorArchivoJsonTraktAsync(peliculaTrcktResultListener,context);
        lectorArchivoJsonTraktAsync.execute();
    }


//------------------------------------------CLASES ASINCRONAS START HERE ----------------------------//


//    public Pelicula damepeliIMDB(String idIMDB){
//
//        Pelicula pelicula = null;
//
//        String urlIMDB = "http://www.omdbapi.com/?i=";
//        PeliculaDAO peliculaDAO = new PeliculaDAO();
//
//        peliculaDAO.flasehadaCreadoraDeAsync(new Pelicula(), urlIMDB + idIMDB, new ResultListener<Object>() {
//
//            @Override
//            public void finish(Object resultado) {
//
//                pelicula = (Pelicula) resultado;
//            }
//        });
//
//    }




    public void flasehadaCreadoraDeAsync(Object object, String urlAPI, ResultListener<Object> listener, Context context){

        LectorArchivoJsonAsyncARRUINABLE lectorAsync = new LectorArchivoJsonAsyncARRUINABLE(listener,context,urlAPI,object);

        lectorAsync.execute();

    }

    private class LectorArchivoJsonAsyncARRUINABLE extends AsyncTask<String,Void,Object>
    {

        private Context context;
        private ResultListener<Object> listenerController;
        private String urlAPI;
        private Object elObject;

        public LectorArchivoJsonAsyncARRUINABLE(ResultListener<Object> listenerController, Context context, String urlAPI, Object elObject) {

            this.listenerController = listenerController;
            this.context = context;
            this.urlAPI = urlAPI;
            this.elObject = elObject;

        }


        @Override
        protected Object doInBackground(String... params) {
            Object pelicula = null;

            try{
                HTTPConnectionManager httpConnectionManager = new HTTPConnectionManager();
                InputStream inputStream = httpConnectionManager.getRequestStream(urlAPI);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new Gson();
                pelicula = gson.fromJson(bufferedReader, elObject.getClass());
            }catch (Exception e){
                e.printStackTrace();
            }
            return (elObject.getClass().cast(pelicula));
        }

        @Override
        protected void onPostExecute(Object o) {
            listenerController.finish(elObject.getClass().cast(o));
        }
    }

    private class LectorArchivoJsonAsync extends AsyncTask<String,Void,Pelicula>
    {
        private String imdbID;
        private Context context;
        private ResultListener<Pelicula> listenerController;

        public LectorArchivoJsonAsync(String imdbID, ResultListener<Pelicula> listenerController, Context context) {
            this.imdbID = imdbID;
            this.listenerController = listenerController;
            this.context = context;
        }


        @Override
        protected Pelicula doInBackground(String... params) {
            Pelicula pelicula = null;
            try{

                HTTPConnectionManager httpConnectionManager = new HTTPConnectionManager();
                InputStream inputStream = httpConnectionManager.getRequestStream("http://www.omdbapi.com/?i=" + imdbID);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new Gson();
                pelicula = gson.fromJson(bufferedReader, Pelicula.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return pelicula;
        }

        @Override
        protected void onPostExecute(Pelicula pelicula) {
            listenerController.finish(pelicula);
        }
    }

    private class LectorArchivoJsonTraktAsync extends AsyncTask<String,Void,WrapperPeliculaTrckt>
    {
        private Context context;
        private ResultListener<WrapperPeliculaTrckt> listenerController;

        public LectorArchivoJsonTraktAsync(ResultListener<WrapperPeliculaTrckt> listenerController, Context context) {

            this.listenerController = listenerController;
            this.context = context;
        }


        @Override
        protected WrapperPeliculaTrckt doInBackground(String... params) {

            WrapperPeliculaTrckt peliculaTrckt = null;

            try{
                HTTPConnectionManager httpConnectionManager = new HTTPConnectionManager();
                InputStream inputStream = httpConnectionManager.getRequestStream("https://api.myjson.com/bins/4sblo");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new Gson();
                peliculaTrckt = gson.fromJson(bufferedReader, WrapperPeliculaTrckt.class);

            }catch (Exception e){
                e.printStackTrace();
            }
            return peliculaTrckt;
        }

        @Override
        protected void onPostExecute(WrapperPeliculaTrckt wrapperPeliculaTrckt) {
            listenerController.finish(wrapperPeliculaTrckt);
        }
    }

    public ContainerPeliculaFireBase readFavoritesFromFirebase(){
        FirebaseDatabase mDatabase;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ContainerPeliculaFireBase containerPeliculaFireBase = dataSnapshot.getValue(ContainerPeliculaFireBase.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //return containerPeliculaFireBase;
        return null;

    }

    public void AddUserToFirebase(UserInfo user){
        FirebaseDatabase mDatabase;
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mDatabase.getReference();
        databaseReference.child("usuarios").child("usuarioTest").setValue(user);
        }

    }





