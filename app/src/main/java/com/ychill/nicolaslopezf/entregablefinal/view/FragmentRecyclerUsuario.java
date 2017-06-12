package com.ychill.nicolaslopezf.entregablefinal.view;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class FragmentRecyclerUsuario extends Fragment {

    private RecyclerView recyclerViewUsuario;
    private AdapterRecycleUsuarios unAdapterUsuarios;
    private Double userLatitude;
    private Double userLongitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View unaVistaADevolver = inflater.inflate(R.layout.fragment_recycle_solo, container, false);
        recyclerViewUsuario= (RecyclerView)unaVistaADevolver.findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);        recyclerViewUsuario.setLayoutManager(linearLayoutManager);
        final ArrayList<Usuario> usuariosDelAdapter = new ArrayList<>();
        unAdapterUsuarios = new AdapterRecycleUsuarios(getActivity(),usuariosDelAdapter,new ListenerUsuarios());
        recyclerViewUsuario.setAdapter(unAdapterUsuarios);
        //busca el usuario de firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            userLatitude = dataSnapshot.child("latitudeCoordinate").getValue(Double.class);
                            userLongitude = dataSnapshot.child("longitudeCoordinate").getValue(Double.class);
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userDataSnapshot : dataSnapshot.getChildren()){


                    Usuario usuario = userDataSnapshot.getValue(Usuario.class);
                    try{
                        Location locationFromFriend = new Location("");
                        locationFromFriend.setLongitude(usuario.getLongitudeCoordinate());
                        locationFromFriend.setLatitude(usuario.getLatitudeCoordinate());
                        Location myLocation = new Location("");
                        myLocation.setLongitude(userLongitude);
                        myLocation.setLatitude(userLatitude);

                        if(myLocation.distanceTo(locationFromFriend) < 20000 && !(usuario.getId().equals(user.getUid()))){

                            usuariosDelAdapter.add(usuario);
                            unAdapterUsuarios.notifyDataSetChanged();

                        }
                    }
                    catch (Exception e){

                        e.getStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return unaVistaADevolver;

    }

    public class ListenerUsuarios implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewUsuario.getChildAdapterPosition(view);
            Usuario usuarioTocado = unAdapterUsuarios.getUsuarioAtPosition(posicionTocada);

            ComunicadorFragmentActivity unComunicador = (ComunicadorFragmentActivity) getActivity();
            unComunicador.clickearonEsteUsuario(usuarioTocado);

        }
    }
    public interface ComunicadorFragmentActivity{
        public void clickearonEsteUsuario(Usuario usuarioClick);

    }
    public class ListenerPeliculasSoloImagen implements View.OnClickListener {

        private RecyclerView recyclerViewAUsar;
        private AdapterRecyclePeliculasFavoritos adapterAUsar;

        public ListenerPeliculasSoloImagen(RecyclerView recyclerViewAUsar, AdapterRecyclePeliculasFavoritos adapterAUsar) {
            this.recyclerViewAUsar = recyclerViewAUsar;
            this.adapterAUsar = adapterAUsar;
        }

        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewAUsar.getChildAdapterPosition(view);
            Pelicula peliculaTocada = adapterAUsar.getPeliculaAtPosition(posicionTocada);

            Log.d("imagen",peliculaTocada.getPoster());

            FragmentRecycleGridFavoritas.ComunicadorFavoritosActivity unComunicador = (FragmentRecycleGridFavoritas.ComunicadorFavoritosActivity) getActivity();
            unComunicador.clickearonEstaPeliculaDeFavoritos(peliculaTocada);


        }
    }
    public interface ComunicadorFavoritosActivity{
        void clickearonEstaPeliculaDeFavoritos(Pelicula peliculaClickeada);

    }



}
