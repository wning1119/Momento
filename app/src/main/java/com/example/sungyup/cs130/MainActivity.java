package com.example.sungyup.cs130;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.content.Intent;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.FirebaseDatabase;

import android.support.v4.app.ActivityCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    private Firebase mRef;
    private ArrayList<Marker> allMarkers = new ArrayList<Marker>();

    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 secs */
    private static final int REQUEST_LOCATION = 2;

    private static final int INITIAL_ZOOM = 15;
    private static final double POST_DISPLAY_RADIUS = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setImageResource(R.drawable.ic_action_add);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToNewPost();
            }
        });

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://cs130-f3703.firebaseio.com/");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void initializeCamera() {
        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM));
    }

    protected void initializePostMarkers() {
        mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                //MyPosts post;
                try {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        //post = child.getValue(MyPosts.class);
                        Integer id = child.child("id").getValue(Integer.class);
                        Long timestamp = child.child("timestamp").getValue(Long.class);
                        Integer timeout = child.child("timeout").getValue(Integer.class);
                        Integer favorite = child.child("favorite").getValue(Integer.class);
                        Double lat = child.child("latitude").getValue(Double.class);
                        Double lng = child.child("longitude").getValue(Double.class);

                        if(System.currentTimeMillis() - timestamp < (timeout + favorite)*3600000) {
                            LatLng tempLatLng = new LatLng(lat, lng);
                            //String printableDate = timestamp.toString();
                            Marker marker = mMap.addMarker(new MarkerOptions().position(tempLatLng));
                            marker.setTag(id);
                            allMarkers.add(marker);
                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(MainActivity.this, "nothing to see here", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });



        /*
        ArrayList<MyPosts> allPosts = posts();
        for (MyPosts post: allPosts) {
            LatLng newLatLng = new LatLng(post.getLatitude(), post.getLongitude());
            String printableDate = post.getTimestamp().toString();
            Marker marker = mMap.addMarker(new MarkerOptions().position(newLatLng).title("User ID: "
                    + post.getOwnerId()).snippet(printableDate));
            marker.setTag(post.getId());
        }
        */

//        double minLat = mCurrentLocation.getLatitude() - POST_DISPLAY_RADIUS;
//        double maxLat = mCurrentLocation.getLatitude() + POST_DISPLAY_RADIUS;
//        double minLng = mCurrentLocation.getLongitude() - POST_DISPLAY_RADIUS;
//        double maxLng = mCurrentLocation.getLongitude() + POST_DISPLAY_RADIUS;
//        ArrayList<MyPosts> nearbyPosts = DatabaseAPIs.searchPostsWithLocation(minLat, maxLat, minLng, maxLng);
//        for(MyPosts post : nearbyPosts) {
//            LatLng newLatLng = new LatLng(post.getLatitude(), post.getLongitude());
//            String printableDate = post.getTimestamp().toString();
//            Marker marker = mMap.addMarker(new MarkerOptions().position(newLatLng).title("User ID: " + post.getOwner()).snippet(printableDate));
//            marker.setTag(post);
//        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
        else {
            mMap.setMyLocationEnabled(true);
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            initializeCamera();
            initializePostMarkers();

            //start location updates
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //redundant code because the permissions system is extremely bad
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mMap.setMyLocationEnabled(true);
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    initializeCamera();
                    initializePostMarkers();

                    //start location updates
                    mLocationRequest = new LocationRequest();
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    mLocationRequest.setInterval(UPDATE_INTERVAL);
                    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                            mLocationRequest, this);
                }
            } else {
                Toast.makeText(this,
                        "Please enable location services to allow full app functionality",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
    }

    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        System.out.println("#markers = " + allMarkers.size());
        for(Marker marker : allMarkers) {
            marker.remove();
        }
        allMarkers.clear();

        mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                //MyPosts post;
                int count = 0;
                try {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        //post = child.getValue(MyPosts.class);
                        Integer id = child.child("id").getValue(Integer.class);
                        Long timestamp = child.child("timestamp").getValue(Long.class);
                        Integer timeout = child.child("timeout").getValue(Integer.class);
                        Integer favorite = child.child("favorite").getValue(Integer.class);
                        Double lat = child.child("latitude").getValue(Double.class);
                        Double lng = child.child("longitude").getValue(Double.class);

                        /*
                        //boolean duplicate = false;
                        for(Marker marker : allMarkers) {
                            if(marker.getTag() == id) {
                                continue;
                            }
                        }
                        */
                        if(System.currentTimeMillis() - timestamp < (timeout + favorite)*3600000) {
                            LatLng tempLatLng = new LatLng(lat, lng);
                            //String printableDate = timestamp.toString();
                            Marker marker = mMap.addMarker(new MarkerOptions().position(tempLatLng));
                            marker.setTag(id);
                            allMarkers.add(marker);
                            count++;
                            System.out.println(count);
                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(MainActivity.this, "nothing to see here", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    public ArrayList<MyPosts> posts() {
        final ArrayList<MyPosts> posts = new ArrayList<>();
        mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                for(com.firebase.client.DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    posts.add(postSnapshot.getValue(MyPosts.class));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return posts;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    public boolean onMarkerClick(final Marker marker) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        //Post post = (Post) marker.getTag();
        //Toast.makeText(this, "user id #" + post.getUser().getID() + " posted: " + post.getText(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "this is a marker", Toast.LENGTH_LONG).show();

        /*
        double currentLat = mCurrentLocation.getLatitude();
        double currentLng = mCurrentLocation.getLongitude();
        ArrayList<Post> postsToDisplay = DatabaseAPIs.searchPostsWithLocation(currentLat - POST_DISPLAY_RADIUS,
                currentLat + POST_DISPLAY_RADIUS, currentLng - POST_DISPLAY_RADIUS, currentLng + POST_DISPLAY_RADIUS);
        */

//        ArrayList<MyPosts> postsToView = (ArrayList<MyPosts>)marker.getTag();
//        LatLng pos = marker.getPosition();
        goToViewPostActivity((Integer)marker.getTag());
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.action_allPosts:
                return true;
            case R.id.action_favorites:
                goToFavoriteActivity();
                return true;
            case R.id.action_myPosts:
                goToMyPostsActivity();
                return true;
            case R.id.action_logout:
                goToLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnClickedViewPost(View view){
        Intent intent = new Intent(this, activity_viewposts.class);
        startActivity(intent);
    }

    private void goToFavoriteActivity(){
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    private void goToMyPostsActivity(){
        Intent intent = new Intent(this, MyPostsActivity.class);
        startActivity(intent);
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToNewPost(){
        Intent intent = new Intent(this, NewPostActivity.class);
        intent.putExtra("latitude", mCurrentLocation.getLatitude());
        intent.putExtra("longitude", mCurrentLocation.getLongitude());
        startActivity(intent);
    }

    private void goToViewPostActivity(int id) {
        Intent intent = new Intent(this, activity_viewposts.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
