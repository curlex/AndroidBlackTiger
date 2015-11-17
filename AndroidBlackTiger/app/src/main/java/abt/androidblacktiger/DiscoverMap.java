package abt.androidblacktiger;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DiscoverMap extends FragmentActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnInfoWindowClickListener {
    GoogleMap mMap;
    EditText placeText;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    private ArrayList<LocationObject> nearbyLocations;
    private ArrayList<String> translatedStrings;
    private HashMap<LocationObject,String> comboWords;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    GPSTracker gps;
    private Location currlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.discovermap);
        translatedStrings = new ArrayList<>();
        nearbyLocations = new ArrayList<>();
        comboWords = new HashMap<>();
        placeText = (EditText) findViewById(R.id.placeText);
        Button btnFind = (Button) findViewById(R.id.btnFind);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        mGoogleApiClient.connect();
        gps = new GPSTracker(this);
        currlocation = gps.getLocation();
        setUpMapIfNeeded();
        btnFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currlocation = gps.getLocation();
                    String toPass = currlocation.getLatitude() + "," + currlocation.getLongitude();
                    nearbyLocations = new GetLocations().execute(toPass).get();
                    if (nearbyLocations != null) {
                        try {

                            for (int i = 0; i < nearbyLocations.size(); i++) {
                                translatedStrings.add(i, nearbyLocations.get(i).getName());
                                // Changed this to accommadate changes to Translator that were made
                                // to accomodate this but now I realist that's pointless as they
                                // still have to be added to nearbyLocations in the for loop?
                                Translator translator = new Translator(getApplicationContext());
                                ArrayList<String> translatedWords = translator.execute(nearbyLocations.get(i).getTypes()).get();
                                for (int j = 0; j < nearbyLocations.get(i).getTypes().size(); j++) {
                                    nearbyLocations.get(i).setTranslatedWord(nearbyLocations.get(i).getTypes().get(j), translatedWords.get(j));
                                }
                                addMarkers();
                            }

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        Log.v("DiscoverMap", "Translation: " + translatedStrings.toString());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    protected void startLocationUpdates() {
        currlocation = gps.getLocation();
        setUpMap();
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void setUpMap() {
        // Marker on load
        LatLng currLocation = new LatLng(currlocation.getLatitude(),currlocation.getLongitude());
        //Marker window with info
        Marker currentlocation = mMap.addMarker(new MarkerOptions()
                .position(currLocation)
                .title("You Are Here!"));
    }
    public void addMarkers(){
        mMap.clear();
        if(nearbyLocations.size()>0) {
            Marker[] markers = new Marker[nearbyLocations.size()];
            for (int i = 0; i < nearbyLocations.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                LocationObject googlePlace = nearbyLocations.get(i);
                double lat = googlePlace.getLatitude();
                double lng = googlePlace.getLongitude();
                String placeName = googlePlace.getName();
                String types = googlePlace.getPairsSet();
                String word = googlePlace.getTypes().get(0);
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                markerOptions.snippet(types+ "\n"+ word);
                markers[i] = mMap.addMarker(markerOptions);
            }
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            mMap.setOnInfoWindowClickListener(this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 70; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);

        }

    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,NewVocabActivity.class);
        intent.putExtra("word",marker.getTitle());
        intent.putExtra("translated",marker.getSnippet());
        intent.putExtra("locationa",marker.getPosition().latitude + marker.getPosition().longitude);
        startActivity(intent);
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
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
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnected(Bundle bundle) {
        //startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        onLocationChanged(location);
    }
}