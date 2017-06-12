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
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Like;
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 12/6/2016.
 */

public class FragmentRecyclerUsuariosConection extends Fragment {

    private RecyclerView recyclerViewUsuario;
    private AdapterRecycleUsuarios unAdapterUsuarios;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View unaVistaADevolver = inflater.inflate(R.layout.fragment_recycle_solo, container, false);
        recyclerViewUsuario= (RecyclerView)unaVistaADevolver.findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);        recyclerViewUsuario.setLayoutManager(linearLayoutManager);
        final ArrayList<Usuario> usuariosDelAdapter = new ArrayList<>();
        unAdapterUsuarios = new AdapterRecycleUsuarios(getActivity(),usuariosDelAdapter,new FragmentRecyclerUsuariosConection.ListenerUsuarios());
        recyclerViewUsuario.setAdapter(unAdapterUsuarios);

        //busca el usuario de firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users").child(user.getUid()).child("likes").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot like: dataSnapshot.getChildren()) {
                                String leGusto = like.getValue(String.class);

                                database.getReference("users").child(leGusto).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                        database.getReference("users").child(usuario.getId()).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot likeBack: dataSnapshot.getChildren()){
                                                    String meGusta = likeBack.getValue(String.class);
                                                    if (meGusta.equals(user.getUid()) && !usuario.getId().equals(user.getUid())){
                                                        usuariosDelAdapter.add(usuario);
                                                        unAdapterUsuarios.notifyDataSetChanged();                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });


        return unaVistaADevolver;

    }

    public class ListenerUsuarios implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Integer posicionTocada = recyclerViewUsuario.getChildAdapterPosition(view);
            Usuario usuarioTocado = unAdapterUsuarios.getUsuarioAtPosition(posicionTocada);

            FragmentRecyclerUsuario.ComunicadorFragmentActivity unComunicador = (FragmentRecyclerUsuario.ComunicadorFragmentActivity) getActivity();
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

