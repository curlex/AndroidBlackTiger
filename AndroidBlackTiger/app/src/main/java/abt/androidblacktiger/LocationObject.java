package abt.androidblacktiger;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asma on 06/11/2015.
 */
public class LocationObject implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(name);
        dest.writeSerializable(types);
        dest.writeMap(translation);
    }
    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<LocationObject> CREATOR = new Parcelable.Creator<LocationObject>() {
        public LocationObject createFromParcel(Parcel pc) {
            return new LocationObject(pc);
        }
        public LocationObject[] newArray(int size) {
            return new LocationObject[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public LocationObject(Parcel dest){
        latitude = dest.readDouble();
        longitude =  dest.readDouble();
        name = dest.readString();
        types = (ArrayList<String>) dest.readSerializable();
        translation = dest.readHashMap(String.class.getClassLoader());
    }
}

