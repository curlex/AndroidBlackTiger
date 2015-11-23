package abt.androidblacktiger;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
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

public class GPS extends Service implements LocationListener, CallbackReceiver,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // Location updates intervals in sec
    private static final String TAG = MainActivity.class.getSimpleName();
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = UPDATE_INTERVAL+120000; // add 2 mins to the update tnerval
    private static int DISPLACEMENT = 10; // 100 meters
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private LocationRequest mLocationRequest;
    public static GoogleApiClient mGoogleApiClient;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    double poiLat = 0;
    double poilng= 0;
    String poi ="house";
    ArrayList <LocationObject> pointOfInterest ;
    private String translatedString;

    private SharedPreferences.OnSharedPreferenceChangeListener changeListener;
    private HandlerThread handlerThread;
    private Looper looper;

    public GPS() {

    }

    /*
     Called befor service  onStart method is called.All Initialization part goes here
    */
    @Override
    public void onCreate() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(getString(R.string.preference_gps))) {
                    checkGPSSettings(sharedPreferences);
                }
                else if(key.equals(getString(R.string.preference_frequency))){
                    String interval = preferences.getString(getString(R.string.preference_frequency), "100");
                    int inval = Integer.parseInt(interval)*60000;
                    changeUpdateFreq(inval);
                }
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(changeListener);
        //  if (checkPlayServices()) {
        buildGoogleApiClient();
        createLocationRequest();
    }

    private void checkGPSSettings(SharedPreferences prefs) {
        boolean gpsOn = prefs.getBoolean(getString(R.string.preference_gps), true);
        if (gpsOn) {
            startLocationUpdates();
        } else {
            stopLocationUpdates();
        }
    }

    public void changeUpdateFreq(int update){
        UPDATE_INTERVAL = update;
        FASTEST_INTERVAL = update + 120000; // update plus 2 mins
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        Toast.makeText(GPS.this, "Frequency Changed", Toast.LENGTH_LONG).show();
        Log.v("GPS", "Frequency changed to: " + update);
        startLocationUpdates();
    }
    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(GPS.this,"UserRecoverableError.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(GPS.this,"This device is not supported", Toast.LENGTH_LONG).show();

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
        Toast.makeText(GPS.this, "Service starting", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
        handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();
        looper = handlerThread.getLooper();
        Toast.makeText(GPS.this, "IS IT CONNECTED: " + mGoogleApiClient.isConnected(), Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this, looper);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        Toast.makeText(GPS.this, "Updates Stopping", Toast.LENGTH_LONG).show();
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
        Toast.makeText(GPS.this, "Location changed!", Toast.LENGTH_LONG).show();
        String loc = latitude+","+longitude;
        Log.v("GPS: ","locationChanged call to asynctask");
        new GetLocations(getApplicationContext(), this).execute(loc);

    }
    private void setUpLocationDetail(){
        if(pointOfInterest!= null) {
            HistoryDBHandler db = ABTApplication.db;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String language = prefs.getString(getString(R.string.preference_language), "en");
            int i = 0;
            do {
                Log.v("GPS","Get this word or dont show again");
                poi = pointOfInterest.get(i).getTypes().get(0);
                poiLat = pointOfInterest.get(i).getLatitude();
                poilng = pointOfInterest.get(i).getLongitude();
                translatedString = pointOfInterest.get(i).getKey(pointOfInterest.get(i).getTypes().get(0));
                Log.v("GPS","Cur Word: "+poi);
                if(db.findWord(poi,language)==null) break;
                Log.v("GPS", "Show again? "+db.findWord(poi,language).getAgain());
                i++;
            }while(!db.findWord(poi,language).getAgain() && i<pointOfInterest.size());
            if(i>=pointOfInterest.size())i--;
            if(db.findWord(poi,language)==null || db.findWord(poi,language).getAgain()) {
                Log.v("GPS", "Creating notification!");
                NewLocationNotification.notify(getApplicationContext(), pointOfInterest.get(i).getName(), poi, translatedString, poiLat, poilng);
                Log.v("GPS", "Finished setting up and created notifications");
            }
        }
        Log.v("GPS","pointOfInterst is null? "+ (pointOfInterest==null));
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
        Toast.makeText(GPS.this, "Service Stopping", Toast.LENGTH_LONG).show();
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public void receiveData(ArrayList<LocationObject> result) {
        pointOfInterest = result;
        setUpLocationDetail();
    }
}