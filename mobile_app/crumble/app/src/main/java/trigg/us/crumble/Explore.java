package trigg.us.crumble;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;

// TODO:
//  1. Override the MarkerCluster OnInfoWindowListener

public class Explore extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener  {

    private GoogleMap mMap;
    private ClusterManager<CrumbClusterItem> mClusterManager;
    private HashMap<Marker, Crumb> markers;
    private ImageButton buttonname;

    public static final String TAG = "Explore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "onCreate finished");

        buttonname = (ImageButton) findViewById(R.id.searchImageButton) ;
        buttonname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
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

        Log.d(TAG, "on Map Ready, about to set up clusterer");
        // Add a the markers from the hashmap to the map

        setUpClusterer();
        // Test Code:
        // Position the map.
        Log.d(TAG, "set up clusterer");
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(5, -5), 10));
        Log.d(TAG, "getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(5, -5), 10));");
    }

    /**
     * Downloads the database of markers from the server and stores them in a database structure.
     */
    public void download() {
        // Attempt to perform the download
        Log.d(TAG, "download");
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

    public GoogleMap getMap() {
        Log.d(TAG, "map is about to be returned");
        return mMap;
    }
    //-------------------------------------------------------------------------
    // Private Classes
    //-------------------------------------------------------------------------
    private class MyInfoWindow implements GoogleMap.OnInfoWindowClickListener,
        GoogleMap.InfoWindowAdapter {
        private View myInfoWindowView;

        public MyInfoWindow() {
            // Add your custom layout here
            //myInfoWindowView = getLayoutInflater().inflate(R.layout.my_custom_info_layout, null);
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            Toast.makeText(getApplicationContext(), "Info window clicked",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public View getInfoContents(Marker marker) {
            // Set the content of the view, then return the view.
            // Lookup the crumb in the marker hash table.
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }

    //-------------------------------------------------------------------------
    // Private Methods
    //-------------------------------------------------------------------------
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

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<CrumbClusterItem>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        Log.d(TAG, "in clustere, getMap() is run");
        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {
        // Initial test code: add ten objects to the cluster manager
        for (int i = 0; i < 10; i++) {
            double offset = i/60d;
            mClusterManager.addItem(new CrumbClusterItem(new Crumb(new LatLng(5,-5 + offset))));
        }
        Log.d(TAG, "All items added to map");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d(TAG, "on navigation item selected");
        if (id == R.id.explore) {
            Intent intent = new Intent(this, Explore.class);
            intent.putExtra(TAG, 1);
            Log.d(TAG, "explore icon clicked");
            startActivity(intent);
        } else if (id == R.id.logbook) {

        } else if (id == R.id.myPins) {

        } else if (id == R.id.nearMe) {

        } else if (id == R.id.options) {

        } else if (id == R.id.nav_share) {

        }
        Log.d(TAG, "explore activity finished, or drawer about to close");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
