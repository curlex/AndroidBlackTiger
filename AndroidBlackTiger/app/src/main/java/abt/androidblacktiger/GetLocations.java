package abt.androidblacktiger;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Asma on 02/11/2015.
 */
// must be created inside an activity
public class GetLocations extends AsyncTask<String,Void,ArrayList<LocationObject>> {
    private final static String LOG_TAG = GetLocations.class.getSimpleName();
    @Override
    protected void onPreExecute(){
        CharSequence message = "";

    }
    protected ArrayList<LocationObject> doInBackground(String... params) {
        // If there's no geometry, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String nearbyPlacesJsonStr = null;
        // new server key to be able to use Google Places Web Service API
        String key = "AIzaSyD50RlH8xh80ouLULgvNCiMntFVnFTxjuI";
        String distance = "100";  //in meters
        String type = "school";
        String d = "distance";
        try {
            // Construct the URL
            final String GOOGLE_NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
            final String LOCATION_PARAM = "location"; // latitude longitude
            final String RADIUS_PARAM = "radius";
            final String RANKBY_PARAM = "rankby";
            final String TYPE_PARAM= "type"; // list type|type|
            final String KEY_PARAM = "key";
            String[] location = params[0].split(",");
            Uri builtUri = Uri.parse(GOOGLE_NEARBY_SEARCH_URL).buildUpon()
                    .appendQueryParameter(LOCATION_PARAM,location[0]+","+location[1])
                    .appendQueryParameter(RADIUS_PARAM, distance)
                    //.appendQueryParameter(RANKBY_PARAM, d)
                    .appendQueryParameter(TYPE_PARAM, type)
                    .appendQueryParameter(KEY_PARAM, key)
                    .build();
            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
            // Create the request , and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            nearbyPlacesJsonStr = readStream(inputStream);
            Log.v(LOG_TAG, "Nearby JSON String: " + nearbyPlacesJsonStr);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the data, there's no point in attemping
            // to parse it.
            return null;
        }

        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getTypesFromJson(nearbyPlacesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the info.
        return null;
    }
    /**
     * Take the String representing the nearby in JSON Format and
     * pull out the data we need to construct the Strings needed for the displaying i.e tpye.
     */
    private static ArrayList<LocationObject> getTypesFromJson(String nearbyPlaces)throws JSONException {
        ArrayList<LocationObject> types = new ArrayList<LocationObject>();
        StringBuilder sb = new StringBuilder();
        try{
            Log.v(LOG_TAG, "The JSON response: " + nearbyPlaces);
            JSONObject jsonObject= new JSONObject( nearbyPlaces );
            if(jsonObject.has("results")){
                Log.v(LOG_TAG, "has results? : " + jsonObject.has("results"));
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                Log.v(LOG_TAG, "Parsing JSON: "+jsonArray.toString());
                for(int i = 0 ; i<jsonArray.length();i++){
                    if(jsonArray.getJSONObject(i).has("name")){
                        if (jsonArray.getJSONObject(i).has("types") && jsonArray.getJSONObject(i).has("geometry")) {
                            types.add(new LocationObject());
                            types.get(i).setName(jsonArray.getJSONObject(i).get("name").toString());
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                            for (int j = 0; j < typesArray.length(); j++) {
                                types.get(i).addType(typesArray.getString(j));
                                Log.v(LOG_TAG, "Parsing TYPES: "+ typesArray.getString(j));
                            }
                            String lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                            String lng = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
                            types.get(i).setLatitude(lat);
                            types.get(i).setLongitude(lng);
                            Log.v(LOG_TAG, "Parsing Geometry: " + lat + " "+lng);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<LocationObject>();
        }
        return types;
    }

    /**return the data from website as a string in the correct JSON format
     *
     */
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        String jsonParsed = "";
        StringBuffer data = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            jsonParsed = data.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonParsed;
    }

}
