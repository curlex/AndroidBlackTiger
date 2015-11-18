package abt.androidblacktiger;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by User on 18/11/2015.
 */
public class MarkerSetUp extends AsyncTask<Object,Void,Object[]> {
    GoogleMap mMap;
    Context c;
    public MarkerSetUp(Context context) {
        c = context;
    }

    @Override
    protected Object[] doInBackground(Object... params) {
        mMap = (GoogleMap) params[0];
        ArrayList<LocationObject> locations = (ArrayList<LocationObject>)params[1];
        MarkerOptions markerOptions[] = new MarkerOptions[locations.size()];
        for (int i = 0; i <locations.size(); i++) {
            markerOptions[i] = new MarkerOptions();
            LocationObject googlePlace = locations.get(i);
            double lat = googlePlace.getLatitude();
            double lng = googlePlace.getLongitude();
            String placeName = googlePlace.getName();
            String types = googlePlace.getPairsSet();
            String word = googlePlace.getTypes().get(0);
            LatLng latLng = new LatLng(lat, lng);
            markerOptions[i].position(latLng);
            markerOptions[i].title(placeName);
            markerOptions[i].snippet(types + "\n" + word);
//            markers[i] =

        }
        Object o[]  = new Object[2];
        o[0] = markerOptions;
        o[1] = mMap;
        return o;
    }
    protected void onPostExecute(Object p[]) {
        mMap.clear();
        MarkerOptions list[] = (MarkerOptions [] )p[0];
        for (int i = 0; i < list.length; i++) {
            mMap.addMarker(list[i]);
        }
    }
}
