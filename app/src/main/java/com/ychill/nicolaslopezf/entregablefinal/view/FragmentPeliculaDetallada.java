package com.ychill.nicolaslopezf.entregablefinal.view;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.dao.PeliculaDAO;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.IDMBidModelling.ContainerMovieIMDBid;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDBTrailerContainer;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.model.WrapperPeliculas;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.ychill.nicolaslopezf.entregablefinal.view.YouTube.YouTubeFragment;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.AdapterRecyclerSoloImagen;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;

/**
 * Created by mora on 24/10/2016.
 */

public class FragmentPeliculaDetallada extends Fragment {


    private Pelicula pelicula;
    private RecyclerView recyclerViewPeliculas;
    private AdapterRecyclerSoloImagen unAdapterPelicula;
    private FragmentManager unFragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        Bundle bundleRecibido = getArguments();

        final View viewADevolverInflado = inflater.inflate(R.layout.fragment_pelicula_detalle, container, false);

        recyclerViewPeliculas = (RecyclerView) viewADevolverInflado.findViewById(R.id.fragmentpeliculadetalle_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPeliculas.setLayoutManager(linearLayoutManager);

        final YouTubeFragment youTubeFragment = new YouTubeFragment();


        recyclerViewPeliculas = (RecyclerView) viewADevolverInflado.findViewById(R.id.fragmentpeliculadetalle_recycleView);
        final TextView textViewNombre = (TextView) viewADevolverInflado.findViewById(R.id.textNombrePeliculaItem);
        final ImageView imageViewFoto = (ImageView) viewADevolverInflado.findViewById(R.id.imageViewPeliculaItem);
        final TextView textViewActores = (TextView) viewADevolverInflado.findViewById(R.id.textActoresPeliculaItem);
        final TextView textViewDescripcion = (TextView) viewADevolverInflado.findViewById(R.id.textDescripcionPeliculaItem);
        final CheckBox checkBoxRating = (CheckBox) viewADevolverInflado.findViewById(R.id.textScorePeliculaItem);
        final com.github.clans.fab.FloatingActionButton flotingActionB = (com.github.clans.fab.FloatingActionButton) viewADevolverInflado.findViewById(R.id.buttonFav);

        final PeliculaController peliculaController = new PeliculaController();


        final String imdbID = bundleRecibido.getString("imdbID");






        // toDo cambiar para que lo haga el controller
        PeliculaDAO peliculaDAO = new PeliculaDAO(getActivity());
        peliculaDAO.flasehadaCreadoraDeAsync(new Pelicula(), "http://www.omdbapi.com/?apikey=dighouse&i="+ imdbID, new ResultListener<Object>() {

            @Override
            public void finish(Object resultado) {
                pelicula = (Pelicula) resultado;

                peliculaController.checkIfMovieIsInFirebase(pelicula, getActivity(), new ResultListener<Boolean>() {
                    @Override
                    public void finish(Boolean esFavorita) {
                        if(!esFavorita){
                            flotingActionB.setColorNormal(Color.parseColor("#FFF8E1"));

                        }
                        else{
                            flotingActionB.setColorNormal(Color.parseColor("#FF8F00"));


                        }
                    }
                });


                flotingActionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PeliculaController peliculaController = new PeliculaController();

                        peliculaController.checkIfMovieIsInFirebase(pelicula, getActivity(), new ResultListener<Boolean>() {
                            @Override
                            public void finish(Boolean esFavorita) {
                                if(!esFavorita){
                                    peliculaController.addMovieToFirebaseFavorites(pelicula,getActivity());
                                    Toast.makeText(getActivity(), pelicula.getTitle()+  " fue agregado a tu lista de favorito", Toast.LENGTH_SHORT).show();
                                    flotingActionB.setColorNormal(Color.parseColor("#FF8F00"));

                                }
                                else{
                                    peliculaController.removeMovieFromFirebaseFavorites(pelicula,getActivity());
                                    Toast.makeText(getActivity(), pelicula.getTitle()+  " fue removida de tu lista de favorito", Toast.LENGTH_SHORT).show();
                                    flotingActionB.setColorNormal(Color.parseColor("#FFF8E1"));


                                }
                            }
                        });
                    }
                });

                textViewNombre.setText(pelicula.getTitle() );
                textViewActores.setText("Actors: \n" + pelicula.getActors());
                textViewDescripcion.setText(pelicula.getPlot());
                checkBoxRating.setText(pelicula.getImdbRating() + "/10");
                Picasso.with(getContext()).load(pelicula.getPoster()).into(imageViewFoto);


//                ArrayList<Pelicula> peliculasDelRecycle = peliculasController.damePeliculasPorGenero(genero,getContext());
//                peliculasDelRecycle.remove(pelicula);
//                long seed = System.nanoTime();
//                Collections.shuffle(peliculasDelRecycle, new Random(seed));

                // TODO: 11/13/16 AGREGAR LISTENER AL ADAPTER

                //recyclerViewPeliculas.setAdapter(unAdapterPelicula);

            }
        },getActivity());

        unAdapterPelicula = new AdapterRecyclerSoloImagen(getActivity());
        unAdapterPelicula.setListener(new ListenerPeliculasSoloImagen(recyclerViewPeliculas,unAdapterPelicula));

        peliculaController.obtenerTMDBidConIMDBid(imdbID, getActivity(), new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieIMDBid tmdbIDContainer = (ContainerMovieIMDBid) resultado;

                String tmdbID = tmdbIDContainer.getMovieResult().get(0).getTmdbID();

                peliculaController.obtenerListaDePeliculasTMDB(TMDBHelper.getSimilarMovies(tmdbID, TMDBHelper.language_ENGLISH, 1), getActivity(),
                        new ResultListener() {
                            @Override
                            public void finish(Object resultado) {
                                ContainerMovieDB peliculasSimilares = (ContainerMovieDB) resultado;

                                unAdapterPelicula.setListaDePeliculas(peliculasSimilares.getResult());
                                unAdapterPelicula.notifyDataSetChanged();
                            }
                        });

                peliculaController.obtenerTrailerDePeliculaTMDB(getActivity(), tmdbID, new ResultListener() {
                    @Override
                    public void finish(Object resultado) {
                        MovieDBTrailerContainer movieDBTrailerContainer = (MovieDBTrailerContainer) resultado;
                        Log.d("trailer",movieDBTrailerContainer.toString());
                        String url;
                        if(movieDBTrailerContainer.getTrailerArrayList().size() > 0){
                            url = movieDBTrailerContainer.getTrailerArrayList().get(0).getTrailerUrl();
                        }
                        else {
                            url = "zX5XasRcGBg";
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("url",url);
                        youTubeFragment.setArguments(bundle);

                        unFragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentPeliculaDetalle_alojadorDeYoutube,youTubeFragment);
                        fragmentTransaction.commit();

                    }
                });

            }
        });

        recyclerViewPeliculas.setAdapter(unAdapterPelicula);

//        peliculaController.obtenerPeliculaPorID(getActivity(), imdbID, new ResultListener<Pelicula>() {
//            @Override
//            public void finish(Pelicula resultado) {
//                textViewNombre.setText(resultado.getTitle());
//                Picasso.with(getContext()).load(resultado.getPoster()).placeholder(R.mipmap.ic_launcher).into(imageViewFoto);
//            }
//        });




        return viewADevolverInflado;

    }
    public class ListenerPeliculasSoloImagen implements View.OnClickListener {

        private RecyclerView recyclerViewAUsar;
        private AdapterRecyclerSoloImagen adapterAUsar;

        public ListenerPeliculasSoloImagen(RecyclerView recyclerViewAUsar, AdapterRecyclerSoloImagen adapterAUsar) {
            this.recyclerViewAUsar = recyclerViewAUsar;
            this.adapterAUsar = adapterAUsar;
        }

        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewAUsar.getChildAdapterPosition(view);
            MovieDB peliculaTocada = adapterAUsar.getPeliculaAtPosition(posicionTocada);
            Log.d("imagen",peliculaTocada.getPoster());

            FragmentRecyclerSoloImagen.ComunicadorFragmentActivity unComunicador = (FragmentRecyclerSoloImagen.ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEstaPelicula(peliculaTocada);


        }
    }


    public interface ComunicadorFragmentActivity{
        void clickearonEstaPelicula(MovieDB peliculaClickeada);

    }
}
