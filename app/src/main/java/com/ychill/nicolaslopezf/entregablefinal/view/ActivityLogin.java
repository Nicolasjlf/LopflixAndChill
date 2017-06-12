package com.ychill.nicolaslopezf.entregablefinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.todddavies.components.progressbar.ProgressWheel;
import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.utils.TMDBHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.fabric.sdk.android.Fabric;
import com.facebook.FacebookSdk;
/**
 * Created by Nicolas Lopez F on 11/13/2016.
 */

public class ActivityLogin  extends AppCompatActivity {

    TMDBHelper tmdbHelper = new TMDBHelper();




    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "oLl1QskkkJO3g28l9rSP3tsbP";
    private static final String TWITTER_SECRET = "chc1vN6KfJGP7GHQyr41Zm0UJ0RIrZkQ8nNOV1WHPkLOrovBhm";
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager facebookCallBackManager;

    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;
    private TwitterLoginButton loginButton;
    private LoginButton loginButtonFacebook;

    ArrayList<MovieDB> backgroundList;



    protected void onCreate(Bundle savedInstanceState){
        final ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;


        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        mAuth  = FirebaseAuth.getInstance();

        final PeliculaController peliculaController = new PeliculaController();
        imageView1 = (ImageView) findViewById(R.id.activityLogin_imageView11);
        imageView2 = (ImageView) findViewById(R.id.activityLogin_imageView12);
        imageView3 = (ImageView) findViewById(R.id.activityLogin_imageView13);
        imageView4 = (ImageView) findViewById(R.id.activityLogin_imageView21);
        imageView5 = (ImageView) findViewById(R.id.activityLogin_imageView22);
        imageView6 = (ImageView) findViewById(R.id.activityLogin_imageView23);
        imageView7 = (ImageView) findViewById(R.id.activityLogin_imageView31);
        imageView8 = (ImageView) findViewById(R.id.activityLogin_imageView32);
        imageView9 = (ImageView) findViewById(R.id.activityLogin_imageView33);



        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
            startActivity(intent);
        }

       // FACEBOOK//
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_button_facebook);
        loginButtonFacebook.setReadPermissions("email");
        facebookCallBackManager = CallbackManager.Factory.create();
        // If using in a fragment

        // Other app specific specialization

        // Callback registration
        loginButtonFacebook.registerCallback(facebookCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(intent);



            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        //FACEBOOK FIN //


        peliculaController.obtenerListaDePeliculasTMDB(TMDBHelper.getPopularMovies(TMDBHelper.language_ENGLISH,1),this,
                new ResultListener() {
            @Override
            public void finish(Object resultado) {


                ContainerMovieDB peliculasTrending = (ContainerMovieDB) resultado;

                if(peliculasTrending != null){
                    backgroundList = peliculasTrending.getResult();
                    long seed = System.nanoTime();
                    Collections.shuffle(backgroundList, new Random(seed));
                    insertMovieOntoImageView(0,imageView1);
                    insertMovieOntoImageView(1,imageView2);
                    insertMovieOntoImageView(2,imageView3);
                    insertMovieOntoImageView(3,imageView4);
                    insertMovieOntoImageView(4,imageView5);
                    insertMovieOntoImageView(5,imageView6);
                    insertMovieOntoImageView(6,imageView7);
                    insertMovieOntoImageView(7,imageView8);
                    insertMovieOntoImageView(8,imageView9);
                }


            }

        });
        //--------------------------------LOGIN TWITTER------------------------------------

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("twitter", "onAuthStateChanged:signed_in:" + user.getUid());
                    try{
                        logUserToFirebaseDatabase(mAuth.getCurrentUser());
                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                        startActivity(intent);
                    }
                    catch (Exception e){
                        e.getStackTrace();
                    }

                } else {
                    // User is signed out
                    Log.d("twitter", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model

                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                handleTwitterSession(session);
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);


                startActivity(intent);

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("facebook", "signInWithCredential:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("facebook", "signInWithCredential", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });


        //-------------------------------- TODO FACEBOOK LOGIN --------------------------------------------------------


    }

    public void checkIfConnectedWithFacebook(){

        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("facebook.com")) {

                Toast.makeText(this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }




    }
    public void insertMovieOntoImageView(int movieNumber,ImageView imageView){
        Picasso.with(ActivityLogin.this).load(TMDBHelper.getImagePoster(TMDBHelper.IMAGE_SIZE_H632, backgroundList.get(movieNumber).getPoster())).
                error(R.mipmap.ic_launcher).into(imageView);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
        facebookCallBackManager.onActivityResult(requestCode,resultCode,data);
    }

    //-------------------------------------------LOGIN TWITTER-----------------------------------------------------

    private void handleTwitterSession(TwitterSession session) {


        Log.d("twitter", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("twitter", "signInWithCredential:onComplete:" + task.isSuccessful());
                        FirebaseUser usuarioAAgregar = mAuth.getCurrentUser();

//                        logUserToFirebaseDatabase(usuarioAAgregar);
//                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
//                        startActivity(intent);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("twitter", "signInWithCredential", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    public void loginAsGuest(View view){
        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
        startActivity(intent);
    }


    public void logUserToFirebaseDatabase(final FirebaseUser user){



        final String id = user.getUid();
        Log.d("facebookuser1",id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {

                            Usuario usuario = new Usuario(user.getEmail(), id);
                            usuario.setFoto(user.getPhotoUrl().toString());
                            Log.d("facebookuser2", usuario.getId());
                            usuario.setNombre(user.getDisplayName());
                            dataSnapshot.getRef().
                                    setValue(usuario);

                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

    }
}
