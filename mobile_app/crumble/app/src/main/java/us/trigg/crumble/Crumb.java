package us.trigg.crumble;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONException;
import org.json.JSONObject;

import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_CREATE_DATE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_CREATOR_ID;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_CRUMB_ID;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_LATITUDE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_LONGITUDE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_MESSAGE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_RATING;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_RATINGS;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_TITLE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_TOTAL_DISCOVERED;

/**
 * Created by trigglatour on 3/1/16.
 */
public class Crumb implements ClusterItem {
    //-----------------------------------------------------------------
    // Constants
    //-----------------------------------------------------------------
    public static final String TAG = "Crumb";
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

    public String getMessage() {return message;}

    public String getTitle(){
        return title;
    }

    public int getTotalDiscovered() {
        return totalDiscovered;
    }

    public float getRating() {
        return rating;
    }
    public int getCrumb_id() {return crumb_id;}

    //-----------------------------------------------------------------
    // Setters
    //-----------------------------------------------------------------
    public void setTitle(String t) { title = t; }

    public void setLocation(String lat, String lng) { latitude = lat; longitude = lng; }

    public void setCrumb_id(int id) {
        crumb_id = id;
    }

    public void setTotalDiscovered(int td) {
        totalDiscovered = td;
    }

    public void setRating(float r) {
        rating = r;
    }
    public void setMessage(String string) {
        message = string;
    }
    public void setCreation_date(String date) {
        creation_date = date;
    }
    public void setRatings(int num) { ratings = num; }
    public void setCreatorId(int id) {creatorId = id; }
    public void setByJson(JSONObject jsonCrumb) {
        try {
            // Set the crumb_id
            String crumb_id_str = jsonCrumb.getString(COLUMN_CRUMB_ID);
            this.setCrumb_id(Integer.parseInt(crumb_id_str));

            // Set the title
            String title_str = jsonCrumb.getString(COLUMN_TITLE);
            this.setTitle(title_str);

            // Set total discovered
            String total_discovered_str = jsonCrumb.getString(COLUMN_TOTAL_DISCOVERED);
            this.setTotalDiscovered(Integer.parseInt(total_discovered_str));

            // Set rating
            String rating_str = jsonCrumb.getString(COLUMN_RATING);
            this.setRating(Float.parseFloat(rating_str));

            // Set the location
            String latitude_str = jsonCrumb.getString(COLUMN_LATITUDE);
            String longitude_str = jsonCrumb.getString(COLUMN_LONGITUDE);
            this.setLocation(latitude_str, longitude_str);

            // Set the message
            String message_str = jsonCrumb.getString(COLUMN_MESSAGE);
            this.setMessage(message_str);
            Log.d(TAG, "message in set by JSON in Crumb" + message_str);

            // Set the creation_date
            String creation_date_str = jsonCrumb.getString(COLUMN_CREATE_DATE);
            this.setCreation_date(creation_date_str);

            // Set the total ratings
            String ratings_str = jsonCrumb.getString(COLUMN_RATINGS);
            this.setRatings(Integer.parseInt(ratings_str));

            // Set the creatorId
            String creator_id_str = jsonCrumb.getString(COLUMN_CREATOR_ID);
            this.setCreatorId(Integer.parseInt(creator_id_str));

        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse crumb from json.");
        }
    }

    //-----------------------------------------------------------------
    // Misc. Public Methods
    //-----------------------------------------------------------------
    public void addLocalRating(float newRating) {
        // Compute the new overall rating
        rating = ((ratings * rating) + newRating)/(ratings + 1);
        // Increment the total number of ratings
        ratings++;
    }
}


