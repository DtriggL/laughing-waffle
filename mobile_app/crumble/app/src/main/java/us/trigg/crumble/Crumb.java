package us.trigg.crumble;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Date;

/**
 * Created by trigglatour on 3/1/16.
 */
public class Crumb implements ClusterItem {
    //-----------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------
    private int crumb_id;
    private String title;
    private int total_discovered;          // Number of people who found the crumb
    private String latittude;
    private String longitude;
    private String message;        // The message attached to the crumb
    private Date creation_date;  // The date that the note was written
    private float rating; // Average rating for the crumb
    private float ratings; // Number of ratings left for a crumb
    private int creator_id;


    //-----------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------
    public Crumb() {

    }

    //-----------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------
    @Override
    public LatLng getPosition() {
        return new LatLng(Location.convert(latittude), Location.convert(longitude));
    }
    public String getTitle(){
        return title;
    }

    //-----------------------------------------------------------------
    // Setters
    //-----------------------------------------------------------------
    public void setTitle(String t) {
        title = t;
    }
    public void setLocation(String lat, String longi) {
        latittude = lat;
        longitude = longi;
    }
    public void setCrumb_id(int id) {
        crumb_id = id;
    }
}


