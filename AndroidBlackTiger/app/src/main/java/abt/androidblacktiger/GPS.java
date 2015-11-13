package abt.androidblacktiger;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GPS extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // Location updates intervals in sec
    private static final String TAG = MainActivity.class.getSimpleName();
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 100 meters
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private LocationRequest mLocationRequest;
    public static GoogleApiClient mGoogleApiClient;
    Location location; // location
    public static double latitude; // latitude
    public static double longitude; // longitude
    public static double poiLat = 0;
    public static double poilng= 0;
    public static String poi ="house"; //
    public static ArrayList <LocationObject> pointOfInterest ;
    private String translatedString;

    private SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    public GPS() {

    }

    /*
     Called befor service  onStart method is called.All Initialization part goes here
    */
    @Override
    public void onCreate() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(getString(R.string.preference_gps))) {
                    checkGPSSettings(sharedPreferences);
                }
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(changeListener);
        //  if (checkPlayServices()) {
        buildGoogleApiClient();
        createLocationRequest();
        // }
    }

    private void checkGPSSettings(SharedPreferences prefs) {
        boolean gpsOn = prefs.getBoolean(getString(R.string.preference_gps), true);
        if (gpsOn) {
            startLocationUpdates();
        } else {
            stopLocationUpdates();
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // GooglePlayServicesUtil.getErrorDialog(resultCode,PLAY_SERVICES_RESOLUTION_REQUEST).show();
                Toast.makeText(getApplicationContext(),
                        "UserRecoverableError.", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder( this )
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices.API)
                .build();
    }
 /*
  You need to write the code to be executed on service start. Sometime due to memory congestion DVM kill the 
  running service but it can be restarted when the memory is enough to run the service again.
 */


    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
//        // If we get killed, after returning from here, restart
//        // Building the GoogleApi client
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        else mGoogleApiClient.connect();
        Toast.makeText(this, "IS IT CONNECTED: "+mGoogleApiClient.isConnected(), Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        Toast.makeText(this, "updates stopping", Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
 /*
  Overriden method of the interface GooglePlayServicesClient.OnConnectionFailedListener .
  called when connection to the Google Play Service are not able to connect 
 */

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*
     This is overriden method of interface GooglePlayServicesClient.ConnectionCallbacks which is called
     when locationClient is connecte to google service.
     You can receive GPS reading only when this method is called.So request for location updates from this
     method rather than onStart()

    */
    @Override
    public void onConnected(Bundle arg0) {
        Log.i("info", "Location Client is Connected");
        checkGPSSettings(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Log.i("info", "Service Connect status :: " + isServicesConnected());
        Log.v("GPS", "Service Connect status :: " + isServicesConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /*
     Overrriden method of interface LocationListener called when location of gps device is changed.
     Location Object is received as a parameter.
     This method is called when location of GPS device is changed
    */
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();
        String loc = latitude+","+longitude;
        try {
            pointOfInterest = new GetLocations().execute(loc).get();
            poiLat = pointOfInterest.get(0).getLatitude();
            poilng = pointOfInterest.get(0).getLongitude();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if( !pointOfInterest.get(0).getTypes().isEmpty()){
            poi = pointOfInterest.get(0).getTypes().get(0);
            Translator translator = new Translator(getApplicationContext());
            try {
                translatedString = translator.execute(poi).get().get(0);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            Log.v(TAG, "Translation: "+ translatedString);
            Toast.makeText(getApplicationContext(),
                    poi +"  " + translatedString, Toast.LENGTH_LONG)
                    .show();

        }
        NewLocationNotification.notify(getApplicationContext(),pointOfInterest.get(0).getName()+" "+ poi, translatedString, poiLat,poilng);
        //send it to places api
        //updatePlaces();
    }

    /**
     * Verify that Google Play services is available before making a request.
     * @return true if Google Play services is available, otherwise false
     */
    private boolean isServicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(GPS.this);

        // If Google Play services is available
        return ConnectionResult.SUCCESS == resultCode;
    }
    /*
     Called when Sevice running in backgroung is stopped.
     Remove location upadate to stop receiving gps reading
    */
    @Override
    public void onDestroy() {
        Log.i("info", "Service is destroyed");
        Toast.makeText(this, "service stopping", Toast.LENGTH_LONG).show();
        stopLocationUpdates();
        super.onDestroy();
    }
}