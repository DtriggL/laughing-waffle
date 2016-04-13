package us.trigg.crumble;

import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerClickListener {

    public static final String EXTRA_MESSAGE = "Explore Activity";
    public static final String TAG = "Main Activity";

    private ClusterManager<CrumbClusterItem> mClusterManager;
    private HashMap<Marker, Crumb> markers;

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    //Location
    protected Location mLastLocation;
    //Addresses
    protected boolean mAddressRequsted;
    protected String mAddressOutput;

    SupportMapFragment sMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frag_manager_frame, new us.trigg.crumble.fragments.LoginFragment()).commit();

        sMapFragment = SupportMapFragment.newInstance();
        sMapFragment.getMapAsync(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        //On every menu button click, the map fragment get's hidden
        if(sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

        if (id == R.id.nav_explore) {

            //The first time, map fragment needs to be added. After that, it just needs to be shown.
            if (!sMapFragment.isAdded())
                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
            else
                sFm.beginTransaction().show(sMapFragment).commit();

        }
        else if (id == R.id.nav_logbook) {

            fm.beginTransaction().replace(R.id.frag_manager_frame, new us.trigg.crumble.fragments.LogbookFragment()).commit();

        }
        else if (id == R.id.nav_myPins) { }
        else if (id == R.id.nav_nearMe) { }
        else if (id == R.id.nav_options) { }
        else if (id == R.id.nav_share) { }

        Log.d(TAG, "explore activity finished, or drawer about to close");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onConnected(Bundle bundle) { }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

}
