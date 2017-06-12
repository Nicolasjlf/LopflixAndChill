package com.ychill.nicolaslopezf.entregablefinal.view;

/**
 * Created by santiagoiraola on 12/4/16.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ychill.nicolaslopezf.entregablefinal.R;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class UserSettingsActivity extends AppCompatActivity {


    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);


        final ImageView imagenUsuario = (ImageView) findViewById(R.id.user_imageView_imagenUsuario);
        final EditText nombreUsuario = (EditText) findViewById(R.id.user_editText_nombre);
        final EditText edadUsuario = (EditText) findViewById(R.id.user_editText_edad);
        final EditText descUsuario = (EditText) findViewById(R.id.user_editText_desc);
        final EditText mailUsuario = (EditText) findViewById(R.id.user_editText_mail);
        final Button saveChanges = (Button) findViewById(R.id.user_saveChanges);

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        final FirebaseUser usuarioLogeado = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String id = usuarioLogeado.getUid();

        database.getReference("users").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        if (dataSnapshot.exists()) {


                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReferenceFromUrl("gs://lopflix-940b2.appspot.com");

                            storageRef.child(id + "_profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ImageView imageView = (ImageView) findViewById(R.id.imageView);

                                    Picasso.with(UserSettingsActivity.this).load(uri.toString()).into(imageView);
                                    Picasso.with(UserSettingsActivity.this).load(uri.toString()).into(imagenUsuario);
                                    // Got the download URL for 'users/me/profile.png'
                                    // Pass it to Picasso to download, show in ImageView and caching

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Picasso.with(UserSettingsActivity.this).load(usuario.getFoto()).into(imagenUsuario);

                                    // Handle any errors
                                }
                            });



                            // Picasso.with(UserSettingsActivity.this).load(usuario.getFoto()).into(imagenUsuario);
                            edadUsuario.setText(usuario.getEdad());
                            nombreUsuario.setText(usuario.getNombre());
                            descUsuario.setText(usuario.getDescripcion());
                            mailUsuario.setText(usuario.getEmail());

                        }
                        else {

                        }

                    }






                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final String id = usuarioLogeado.getUid();
                final FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReferenceFromUrl("gs://lopflix-940b2.appspot.com");
                final StorageReference imagesRef = storageRef.child(id + "_profilePic.jpg");


                saveChanges.setText("Guardando...");
                saveChanges.setClickable(false);

                // Get the data from an ImageView as bytes

                try {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = imagesRef.putBytes(data);


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(UserSettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UserSettingsActivity.this, "Image has been saved.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserSettingsActivity.this,MainActivity.class);
                            startActivity(intent);


                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    Intent intent = new Intent(UserSettingsActivity.this,MainActivity.class);
                    startActivity(intent);
                }


//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final String id = usuarioLogeado.getUid();
                database.getReference("users").child(id).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    //el anterior por si es un gato
                                    final Usuario usuario = new Usuario(usuarioLogeado.getEmail(), id);
                                    usuario.setFoto(usuarioLogeado.getPhotoUrl().toString());
                                    usuario.setNombre(usuarioLogeado.getDisplayName());

                                    //los cambios
                                    usuario.setEdad(edadUsuario.getText().toString());
                                    usuario.setNombre(nombreUsuario.getText().toString());
                                    usuario.setEmail(mailUsuario.getText().toString());
                                    usuario.setDescripcion(descUsuario.getText().toString());

                                    dataSnapshot.child("nombre").getRef().setValue(usuario.getNombre());
                                    dataSnapshot.child("edad").getRef().setValue(usuario.getEdad());
                                    dataSnapshot.child("email").getRef().setValue(usuario.getEmail());
                                    dataSnapshot.child("descripcion").getRef().setValue(usuario.getDescripcion());

                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReferenceFromUrl("gs://lopflix-940b2.appspot.com");
                                    storageRef.child(id + "_profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Got the download URL for 'users/me/profile.png'
                                            // Pass it to Picasso to download, show in ImageView and caching


                                            dataSnapshot.child("foto").getRef().setValue(uri.toString());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });


                                }
                                else {

                                }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });

            }
        });

    }

    public void chooseImage(View view){
        EasyImage.openChooserWithGallery(UserSettingsActivity.this, "Selecciona una imagen", 666);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(UserSettingsActivity.this, "Fallo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Toast.makeText(UserSettingsActivity.this, "Todo ok", Toast.LENGTH_SHORT).show();

                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                ImageView imagenUsuario = (ImageView) findViewById(R.id.user_imageView_imagenUsuario);
                imagenUsuario.setVisibility(View.GONE);
            }
        });
    }



    public void volverAObras(View view){
        Intent intent = new Intent(UserSettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }



    public void cambiarFoto(View view){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://lopflix-940b2.appspot.com");

    }
}