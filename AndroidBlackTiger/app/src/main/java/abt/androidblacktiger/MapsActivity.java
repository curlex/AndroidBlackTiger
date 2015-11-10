package abt.androidblacktiger;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

//import com.google.android.gms.location.LocationServices;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        gps = new GPS();
       // if(gps.canGetLocation()) { // gps enabled} // return boolean true/false
//            latitude = gps.getLatitude(); // returns latitude
//            longitude = gps.getLongitude(); // returns longitude
            poiLat = gps.poiLat;
            poiLng = gps.poilng;
            setUpMap();
       // }
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
        LatLng currLocation = new LatLng(latitude,longitude);
        LatLng poiLocation = new LatLng(poiLat,poiLng);
        //Marker window with info
        Marker mylocation = mMap.addMarker(new MarkerOptions()
                .position(currLocation)
                .title("I am here")
                .snippet("Je suis ici"));
        if(gps!=null) {
            try {
                System.out.println(gps.poi);
                translatedString = new Translator().execute(new TranslatorParams(getApplicationContext(), gps.poi)).get().get(0);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            Marker poilocation = mMap.addMarker(new MarkerOptions()
                    .position(poiLocation)
                    .title(gps.poi)
                    .snippet(translatedString));

        }

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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
        Log.d(TAG, location.toString());
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("New Location!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
