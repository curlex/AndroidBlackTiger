package abt.androidblacktiger;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
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
import java.util.concurrent.ExecutionException;

public class DiscoverMap extends FragmentActivity implements com.google.android.gms.location.LocationListener, CallbackReceiver,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnInfoWindowClickListener {
    GoogleMap mMap;
    EditText placeText;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    private ArrayList<LocationObject> nearbyLocations;
    private ArrayList<String> translatedStrings;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private Location currlocation;
    private AsyncTask getLocations = null;
    MarkerOptions markers[] = null ;
    private String engWord;
    private String translatedWord;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available

        Intent intent = getIntent();
        title = intent.getStringExtra(getString(R.string.word_intent_title));
        engWord = intent.getStringExtra(getString(R.string.word_intent_word));
        translatedWord = intent.getStringExtra(getString(R.string.word_intent_translation));
        latitude = intent.getDoubleExtra(getString(R.string.word_intent_Latitude), 0);
        longitude = intent.getDoubleExtra(getString(R.string.word_intent_Longitude), 0);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.discovermap);
        translatedStrings = new ArrayList<>();
        nearbyLocations = new ArrayList<>();
        placeText = (EditText) findViewById(R.id.placeText);
        Button btnFind = (Button) findViewById(R.id.btnFind);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        mGoogleApiClient.connect();
        setUpMapIfNeeded();
        if(longitude!=0 && latitude!=0){
            addLocation();
        }

        final DiscoverMap dm = this;
        btnFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.v("DiscoverMap", "Listener launched");
                    currlocation = getLastLocation();
                    Log.v("DiscoverMap", "Location got");
                    String toPass = currlocation.getLatitude() + "," + currlocation.getLongitude();
                    getLocations = new GetLocations(getApplicationContext(), dm).execute(toPass);
            }
        });
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("Locations")){
                nearbyLocations = savedInstanceState.getParcelableArrayList("Locations");
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelableArrayList("Locations", nearbyLocations);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private  Location getLastLocation(){
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        currlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
        if(nearbyLocations.size()>0){
            try {
                markers = new SetUpMarkers().execute(nearbyLocations).get();
                addMarkers();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    public void addLocation(){
        LatLng currLocation = new LatLng(latitude,longitude);
        //Marker window with info
        Marker currentlocation = mMap.addMarker(new MarkerOptions()
                .position(currLocation)
                .title(title)
                .snippet(engWord+"\n"+translatedWord));
    }
    public void addMarkers(){
        // Update the map with new view of all the markers
        if(nearbyLocations != null) {
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            mMap.setOnInfoWindowClickListener(DiscoverMap.this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LocationObject Nearbypoi : nearbyLocations) {
                builder.include(Nearbypoi.getPosition());
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
        intent.putExtra(getString(R.string.word_intent_word),marker.getSnippet().split("\n")[0]);
        intent.putExtra(getString(R.string.word_intent_translation),marker.getSnippet().split("\n")[1]);
        intent.putExtra(getString(R.string.word_intent_Latitude),marker.getPosition().latitude);
        intent.putExtra(getString(R.string.word_intent_Longitude),marker.getPosition().longitude);
        startActivity(intent);
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        currlocation = location;
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
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mGoogleApiClient.isConnected()){stopLocationUpdates();}
        if(getLocations != null) getLocations.cancel(true);
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    protected void onDestroy(){
        super.onDestroy();
        stopLocationUpdates();
    }
    @Override
    public void onMyLocationChange(Location location) {
        onLocationChanged(location);
    }

    @Override
    public void receiveData(ArrayList<LocationObject> result) {
            mMap.clear();
        nearbyLocations = result;
            try {
                markers = new SetUpMarkers().execute(result).get();
                addMarkers();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
    }

    //Populates map with markers in another thread and updates UI thread for every marker
    private class SetUpMarkers extends AsyncTask<ArrayList<LocationObject>,MarkerOptions, MarkerOptions[]> {
        @Override
        protected MarkerOptions[] doInBackground(ArrayList<LocationObject>... params) {
            if(params[0] != null) {
                MarkerOptions markers[] = new MarkerOptions[params[0].size()];
                if (params[0].size() > 0) {
                    for (int i = 0; i < params[0].size(); i++) {
                        markers[i] = new MarkerOptions();
                        LocationObject googlePlace = params[0].get(i);
                        String placeName = googlePlace.getName();
                        String types = googlePlace.getTypes().get(0);
                        String word = googlePlace.getKey(types);
                        markers[i].position(googlePlace.getPosition());
                        markers[i].title(placeName);
                        markers[i].snippet(types + "\n" + word);
                        publishProgress(markers[i]);
                    }
                }
                return markers;
            }
            else return null;
        }

        protected void onProgressUpdate(MarkerOptions... progress) {
            mMap.addMarker(progress[0]);
        }

        @Override
        protected void onPostExecute(MarkerOptions [] markers){
            super.onPostExecute(markers);
            //addMarkers();
        }
    }
}