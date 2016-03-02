package us.trigg.crumble;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class explore extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Marker, String> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Add a the markers from the hashmap to the map
    }

    /**
     * Downloads the database of markers from the server and stores them in a database structure.
     */
    public void download() {
        // Attempt to perform the download
        try {
            // Open a connection to the PHP server.
            // Get the response code
            // Open an input stream as well as a buffered input reader
            try {
                // if (responseCode == HttpURLConnection.HTTP_OK) {
                // Attempt to parse the data from the server

                /* Add the crumb objects, but only add the coordinates, the rating,
                 * the name, and the number of visits to the local database structure.
                 */

                // Perhaps add a post method to add the crumbs to the hashmap

                // Post: Notify the UI thread that the markers are ready to be loaded.

            } catch (Exception e) {

            } finally {
                // Close the input streams
            }
        } catch(Exception e) {

        }
    }

    private void downloadCrumbs() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                download();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
