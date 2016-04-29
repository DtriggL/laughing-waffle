package us.trigg.crumble;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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
    private String creation_date;    // The date that the note was written
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
    public String getMessage(){
        return message;
    }
    public int getRatings() {
        return ratings;
    }
    public float getCreatorID() {
        return creatorId;
    }
    public int getCrumbID(){
        return crumb_id;
    }
    public String getCreationDate() {
        return creation_date;
    }
    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}

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
    public void setCrumbID(int id) {
        crumb_id = id;
    }
    public void setMessage(String m) {
        message = m;
    }
    public void setRating(Float r) {
        rating = r;
    }
    public void setRatings(int r) {
        ratings = r;
    }
    public void setTotalDiscovered(int td) {
        totalDiscovered = td;
    }
    public void setCreatorID(int cd) {
        creatorId = cd;
    }
    public void setCreationDate(String cd) {
        creation_date = cd;
    }
    public void setLatitude(String lat){latitude = lat;}
    public void setLongitude(String lng){longitude = lng;}
}


