package us.trigg.crumble;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

/**
 * Created by trigglatour on 3/1/16.
 */
public class Crumb {
    private String name;        // Name of the crumb
    private int bites;          // Number of people to find the crumb
    private LatLng position;
    private String note;        // The note attached to the crumb
    private Date date_created;  // The date that the note was written

    //-----------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------
    public Crumb(LatLng pos) {
        position = pos;
    }

    public Crumb() {

    }

    //-----------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------
    public LatLng getPosition() {
        return position;
    }
}


