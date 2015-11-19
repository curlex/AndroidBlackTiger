package abt.androidblacktiger;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asma on 06/11/2015.
 */
public class LocationObject {
    double latitude = 0 , longitude = 0;
    String name = "";
    ArrayList<String> types;
    HashMap<String,String> translation;
    public LocationObject(){
        types = new ArrayList<String>();
        translation = new HashMap<String,String>();
    }
    public void setLatitude(String lat) {
        latitude = Double.parseDouble(lat);
    }
    public void setLongitude(String lng){
        longitude = Double.parseDouble(lng);
    }
    public void addType(String type1){
        types.add(type1);
    }
    public void setName(String name){
        this.name = name;
    }
    public LatLng getPosition(){
        return new LatLng(latitude,longitude);
    }
    public ArrayList<String> getTypes(){
        return types;
    }
    public double getLatitude(){
        return latitude;
    }
    public String getName(){
        return name;
    }
    public void setTranslatedWord(String word,String translated){
        translation.put(word,translated);
    }
    public String getPairsSet(){
        return translation.entrySet().toString();
    }
    public String getKey(String key){
        if(translation.containsKey(key))return translation.get(key);
        return "";
    }
    public double getLongitude() {
        return longitude;
    }
}
