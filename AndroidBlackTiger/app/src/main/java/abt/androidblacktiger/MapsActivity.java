package abt.androidblacktiger;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

//import com.google.android.gms.location.LocationServices;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener{
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double longitude;
    private double latitude;
    private double poiLat;
    private double poiLng;
    private String translatedString ="" ;
    Translator t;
    GPS gps;
    private String poi ="";
    private Location currL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        GPSTracker gps = new GPSTracker(this);
        currL = gps.getLocation();
        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Intent results = getIntent();
        poi = results.getStringExtra(getString(R.string.word_intent_word));//change to current location
        poiLat = results.getDoubleExtra(getString(R.string.word_intent_Latitude), latitude);//change to current location
        poiLng =results.getDoubleExtra(getString(R.string.word_intent_Longitude),longitude);//change to current location;
        translatedString = results.getStringExtra(getString(R.string.word_intent_translation));//change to current location
        setUpMap();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Marker on load
        LatLng currLocation = new LatLng(currL.getLatitude(),currL.getLongitude());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //Marker window with info
        Marker curLoc = mMap.addMarker(new MarkerOptions()
                .position(currLocation)
                .title("You are here"));
        if(poiLat!=0 && poiLng!=0) {
            mMap.clear();
            LatLng poiLocation = new LatLng(poiLat,poiLng);
            Marker poiLoc = mMap.addMarker(new MarkerOptions()
                    .position(poiLocation)
                    .title(poi)
                    .snippet(translatedString));
            builder.include(poiLoc.getPosition());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(poiLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(poiLocation));
        }

        builder.include(curLoc.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 70; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.d(TAG, location.toString());
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Location Changed!");
        mMap.addMarker(options);
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,NewVocabActivity.class);
        intent.putExtra("word",marker.getTitle());
        intent.putExtra("translated",marker.getSnippet());
        intent.putExtra("locationa", marker.getPosition().latitude + marker.getPosition().longitude);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {

        }
        else {
            handleNewLocation(location);
        };
        Log.i(TAG, "Location services connected.");
    }
    private void handleNewLocation(Location location) {
        mMap.clear();
        Log.d(TAG, location.toString());
        setUpMap();

    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
