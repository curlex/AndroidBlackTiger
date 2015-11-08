package abt.androidblacktiger;

import java.util.ArrayList;

/**
 * Created by Asma on 06/11/2015.
 */
public class LocationObject {
    double latitude = 0 , longitude = 0;
    ArrayList<String> types;
    public LocationObject(){
        types = new ArrayList<String>();
    }
    public void setLat(String lat) {
        latitude = Double.parseDouble(lat);
    }
    public void setLongitude(String lng){
        longitude = Double.parseDouble(lng);
    }
    public void addType(String type1){
        types.add(type1);
    }
    public ArrayList<String> getTypes(){
        return types;
    }
    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
