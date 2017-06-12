package com.ychill.nicolaslopezf.entregablefinal.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.ychill.nicolaslopezf.entregablefinal.R;
import com.ychill.nicolaslopezf.entregablefinal.controller.PeliculaController;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.ContainerMovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.MovieDB.MovieDB;
import com.ychill.nicolaslopezf.entregablefinal.model.PeliculaIMDB.Pelicula;
import com.ychill.nicolaslopezf.entregablefinal.model.Usuario.Usuario;
import com.ychill.nicolaslopezf.entregablefinal.utils.ResultListener;
import com.ychill.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.internal.TwitterApi;

import java.util.zip.Inflater;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements FragmentRecyclerSoloImagen.ComunicadorFragmentActivity,FragmentRecycleGridFavoritas.ComunicadorFavoritosActivity,FragmentRecyclerUsuario.ComunicadorFragmentActivity, FragmentUsuarioDetalle.InterfaceHuboConeccion,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    //------------------------------GOOGLEMAPS-----------------------------------------//
    private static final String TAG = GoogleMapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    // A request object to store parameters for requests to the FusedLocationProviderApi.
    private LocationRequest mLocationRequest;
    // The desired interval for location updates. Inexact. Updates may be more or less frequent.
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // The fastest rate for active location updates. Exact. Updates will never be more frequent
    // than this value.
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located.
    private Location mCurrentLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    //------------------------------GOOGLEMAPS-----------------------------------------//

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "o0AwMxuktney6F0OxNZXhup35";
    private static final String TWITTER_SECRET = "bkItOFtZmpTPmBDWP3zEylH2iYeOUL1eLy6a3AVaV444eXra0w";


    DrawerLayout drawerLayout;
    private FragmentManager unFragmentManager;
    PeliculaController peliculaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        peliculaController = new PeliculaController();
        peliculaController.obtenerPeliculasPorGenero("sci-fi", this, new ResultListener() {
            @Override
            public void finish(Object resultado) {
                ContainerMovieDB containerMovieDB = (ContainerMovieDB) resultado;
            }
        });
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        FragmentViewPager fragmentViewPager = new FragmentViewPager();
        unFragmentManager = getSupportFragmentManager();


        FragmentTransaction unaTransaction = unFragmentManager.beginTransaction();
        unaTransaction.replace(R.id.acaVaElFragmentPelicula, fragmentViewPager);
        unaTransaction.commit();



        //SETEAR NAVIGATION VIEW Y SU LISTENER
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationViewPorGeneroListener());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

//         agrega la foto del usuario a el circle image view
//        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.navigationheader_circleimage);
//        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
//        FirebaseUser usuarioAAgregar = mAuth.getCurrentUser();
//        Picasso.with(this).load(usuarioAAgregar.getPhotoUrl().toString()).into(circleImageView);


        //-------------------------------------------GOOGLEMAPS-----------------------------------------//

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();




    }


    private class NavigationViewPorGeneroListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.toString().equals("Log Out")){
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                startActivity(intent);
            }

            if (item.toString().equals("Settings")){
                Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(MainActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                unFragmentManager = getSupportFragmentManager();

                FragmentPeliculasGenero fragmentPeliculasGeneron = new FragmentPeliculasGenero();
                Bundle bundle = new Bundle();
                bundle.putString("genero", item.toString());
                fragmentPeliculasGeneron.setArguments(bundle);

                FragmentTransaction unaTransaction = unFragmentManager.beginTransaction();
                unaTransaction.replace(R.id.acaVaElFragmentPelicula, fragmentPeliculasGeneron).addToBackStack(null);
                unaTransaction.commit();
            }


            drawerLayout.closeDrawers();
            return true;
        }
    }
    public void clickearonEstaPelicula(MovieDB peliculaClickeada) {

        peliculaController.obtenerPeliculaTMDB(peliculaClickeada.getId(), this, new ResultListener() {
            @Override
            public void finish(Object resultado) {

                FragmentPeliculaDetallada fragmentPeliculaDetallada = new FragmentPeliculaDetallada();
                MovieDB movieDB = (MovieDB) resultado;
                Log.d("movieDB",movieDB.toString());
                Bundle unBundle = new Bundle();
                unBundle.putString("imdbID", movieDB.getImdbID());
                unBundle.putString("tmdbID",movieDB.getId());
                fragmentPeliculaDetallada.setArguments(unBundle);

                FragmentTransaction unaTransaccion = unFragmentManager.beginTransaction();
                unaTransaccion.replace(R.id.acaVaElFragmentPelicula, fragmentPeliculaDetallada).addToBackStack("backButton");
                unaTransaccion.commit();
            }
        });
    }    public void clickearonEsteUsuario(Usuario usuarioClickeado) {


                FragmentUsuarioDetalle fragmentUsuarioDetalle = new FragmentUsuarioDetalle();

                Bundle unBundle = new Bundle();
                unBundle.putString("idClick", usuarioClickeado.getId());

                fragmentUsuarioDetalle.setArguments(unBundle);

                FragmentTransaction unaTransaccion = unFragmentManager.beginTransaction();
                unaTransaccion.replace(R.id.acaVaElFragmentPelicula, fragmentUsuarioDetalle).addToBackStack("backButton");
                unaTransaccion.commit();
            }



    @Override
    public void clickearonEstaPeliculaDeFavoritos(Pelicula peliculaClickeada) {

        FragmentPeliculaDetallada fragmentPeliculaDetallada = new FragmentPeliculaDetallada();
        Bundle unBundle = new Bundle();
        unBundle.putString("imdbID",peliculaClickeada.getImdbID());
        //unBundle.putString("tmdbID",peliculaController.obtenerTMDBidDeBaseDeDatos(this,peliculaClickeada.getImdbID()));

        fragmentPeliculaDetallada.setArguments(unBundle);

        FragmentTransaction fragmentTransaction = unFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.acaVaElFragmentPelicula, fragmentPeliculaDetallada).addToBackStack("backButton");
        fragmentTransaction.commit();
    }

    @Override
    public void huboConeccion() {
        FragmentLove fragmentLove = new FragmentLove();
        FragmentTransaction fragmentTransaction = unFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.add(R.id.acaVaElFragmentPelicula, fragmentLove).addToBackStack("backButton");
        fragmentTransaction.commit();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                unFragmentManager.popBackStack();
            }
        }.start();

    }

    //-------------------------------------------------GOOGLEMAPS---------------------------------------------------//


    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        /*
         * Sets the desired interval for active location updates. This interval is
         * inexact. You may not receive updates at all if no location sources are available, or
         * you may receive them slower than requested. You may also receive updates faster than
         * requested if other applications are requesting location at a faster interval.
         */
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        /*
         * Sets the fastest rate for active location updates. This interval is exact, and your
         * application will never receive updates faster than this value.
         */
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Get the device location and nearby places when the activity is restored after a pause.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            getDeviceLocation();
        }
    }

    /**
     * Stop location updates when the activity is no longer in focus, to reduce battery consumption.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Gets the device's current location and builds the map
     * when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        getDeviceLocation();
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }

    /**
     * Handles the callback when location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {

            mLocationPermissionGranted = true;
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         * Also request regular updates about the device location.
         */
        if (mLocationPermissionGranted) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            try{
                database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    dataSnapshot.child("latitudeCoordinate").getRef().setValue(mCurrentLocation.getLatitude());
                                    dataSnapshot.child("longitudeCoordinate").getRef().setValue(mCurrentLocation.getLongitude());
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    Toast.makeText(this, "PERMISSION WAS GRANTED", Toast.LENGTH_SHORT).show();
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */

    @SuppressWarnings("MissingPermission")
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mCurrentLocation = null;
        }
    }

}


