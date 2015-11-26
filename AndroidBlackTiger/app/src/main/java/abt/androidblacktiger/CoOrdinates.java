package abt.androidblacktiger;

/**
 * Created by Maria on 12/11/2015.
 * Custom class to keep lats & longs together
 */
public class CoOrdinates {
    protected double lat;
    protected double lng;

    CoOrdinates(double latitude, double longitude){
        this.lat = latitude;
        this.lng = longitude;
    }

    /**
     * Getters and Setters
     **/
    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public void setLat(double latitude){
        this.lat = latitude;
    }

    public void setLng(double longitude){
        this.lng = longitude;
    }
}
