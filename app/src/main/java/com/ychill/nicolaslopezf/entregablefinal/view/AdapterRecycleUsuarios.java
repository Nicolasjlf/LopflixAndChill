package com.ychill.nicolaslopezf.entregablefinal.view;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class AdapterRecycleUsuarios  extends RecyclerView.Adapter{
    private Context unContexto;
    private ArrayList<Usuario> listaDeUsuarios;
    private View.OnClickListener listener;

    public AdapterRecycleUsuarios(Context unContexto, ArrayList<Usuario> listaDeUsuarios, View.OnClickListener listener) {
        this.unContexto = unContexto;
        this.listener = listener;
        this.listaDeUsuarios = listaDeUsuarios;

    }

    public AdapterRecycleUsuarios(Context unContexto, final ArrayList<Usuario> listaDeUsuarios) {
        this.unContexto = unContexto;
        this.listaDeUsuarios = listaDeUsuarios;    }

    public ArrayList<Usuario> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    public void setListaDeUsuarios(ArrayList<Usuario> listaDeUsuarios) {
        this.listaDeUsuarios = listaDeUsuarios;
    }
    public Usuario getUsuarioAtPosition(Integer position){
        return listaDeUsuarios.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(unContexto);

        View vistaADevolver = inflater.inflate(R.layout.fragment_pelicula_item, parent, false);
        vistaADevolver.setOnClickListener(listener);
        AdapterRecycleUsuarios.UsuarioViewHolder unViewHolderUsuario = new AdapterRecycleUsuarios.UsuarioViewHolder(vistaADevolver);
        return unViewHolderUsuario;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Usuario UserABindear = listaDeUsuarios.get(position);
        UsuarioViewHolder unUsuarioViewHolder = (AdapterRecycleUsuarios.UsuarioViewHolder) holder;
        unUsuarioViewHolder.bindReceta(UserABindear);




    }

    @Override
    public int getItemCount() {
        return listaDeUsuarios.size();
    }

    private class UsuarioViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagenUsuario;
        private TextView nombreUsuario;


        public UsuarioViewHolder(View itemView){
            super(itemView);
            imagenUsuario = (ImageView)itemView.findViewById(R.id.ivPelicula);
            nombreUsuario = (TextView)itemView.findViewById(R.id.tvNombrePelicula);



        }
        public void bindReceta(final Usuario unUsuario){

            nombreUsuario.setText(unUsuario.getNombre());


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://lopflix-940b2.appspot.com");

            storageRef.child(unUsuario.getId() + "_profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(unContexto).load(uri.toString()).into(imagenUsuario);
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Picasso.with(unContexto).load(unUsuario.getFoto()).into(imagenUsuario);

                    // Handle any errors
                }
            });

        }
    }
}