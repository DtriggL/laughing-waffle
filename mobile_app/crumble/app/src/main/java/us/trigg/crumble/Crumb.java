package us.trigg.crumble;

import android.hardware.camera2.TotalCaptureResult;
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
    private int totalDiscovered;   // Number of people who found the crumb
    private String latitude;
    private String longitude;
    private String message;        // The message attached to the crumb
    private Date creation_date;    // The date that the note was written
    private float rating; // Average rating for the crumb
    private int ratings;  // Number of ratings left for a crumb
    private int creatorId;


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
        return new LatLng(Location.convert(latitude), Location.convert(longitude));
    }
    public String getTitle(){
        return title;
    }
    public int getTotalDiscovered() {
        return totalDiscovered;
    }
    public float getRating() {
        return rating;
    }

    //-----------------------------------------------------------------
    // Setters
    //-----------------------------------------------------------------
    public void setTitle(String t) {
        title = t;
    }
    public void setLocation(String lat, String longi) {
        latitude = lat;
        longitude = longi;
    }
    public void setCrumb_id(int id) {
        crumb_id = id;
    }

    //-----------------------------------------------------------------
    // Misc. Public Methods
    //-----------------------------------------------------------------
    public void addRating(int newRating) {
        // Compute the new overall rating
        rating = ((ratings * rating) + newRating)/(ratings + 1);
        // Increment the total number of ratings
        ratings++;
    }
}


