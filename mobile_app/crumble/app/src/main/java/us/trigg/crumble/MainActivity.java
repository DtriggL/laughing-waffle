package us.trigg.crumble;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import us.trigg.crumble.fragments.LogbookFragment;
import us.trigg.crumble.fragments.LoginFragment;
import us.trigg.crumble.fragments.NoConnectionAlertFragment;
import us.trigg.crumble.interfaces.MyFragmentDialogInterface;

import static us.trigg.crumble.WebConstants.*;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMyLocationButtonClickListener,
        MyFragmentDialogInterface {

    public static final String EXTRA_MESSAGE = "Explore Activity";
    public static final String TAG = "Main Activity";

    private ClusterManager<Crumb> mClusterManager;
    private DefaultClusterRenderer<Crumb> mClusterRenderer;
    private HashMap<Integer, Crumb> crumbMarkerHashMap;

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    //Location
    protected Location mLastLocation;
    //Addresses
    protected boolean mAddressRequsted;
    protected String mAddressOutput;

    private NoConnectionAlertFragment alert;

    SupportMapFragment sMapFragment;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //-----------------------------------------------------------------------------------
    // Life-cycle Event Handlers
    //-----------------------------------------------------------------------------------
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
        fm.beginTransaction().replace(R.id.frag_manager_frame, new LoginFragment()).commit();

        sMapFragment = SupportMapFragment.newInstance();
        sMapFragment.getMapAsync(this);

        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        sFm.beginTransaction().add(R.id.map, sMapFragment).commit();

        // Start a fused API to get the user's current location
    }


    //-----------------------------------------------------------------------------------
    // Google Map Event Handlers
    //-----------------------------------------------------------------------------------

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

        Log.d(TAG, "on Map Ready, about to set up clusterer");

        setUpClusterer();

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Get all of the crumbs from the server and add them to the map
        new GetAllCrumbs().execute(null, null, null);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    //-----------------------------------------------------------------------------------
    // Google API Event Handlers
    //-----------------------------------------------------------------------------------
    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    //-----------------------------------------------------------------------------------
    // Fused Location API event handlers
    //-----------------------------------------------------------------------------------


    //-----------------------------------------------------------------------------------
    // Navigation Drawer Event Handlers
    //-----------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

        if (id == R.id.nav_explore) {

            //The first time, map fragment needs to be added. After that, it just needs to be shown.
            if (!sMapFragment.isAdded())
                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
            else
                sFm.beginTransaction().show(sMapFragment).commit();

        } else if (id == R.id.nav_logbook) {

            fm.beginTransaction().replace(R.id.frag_manager_frame, new LogbookFragment()).commit();
        }

        else if (id == R.id.nav_myPins) { }
        else if (id == R.id.nav_nearMe) { }
        else if (id == R.id.nav_login) {
            fm.beginTransaction().replace(R.id.frag_manager_frame, new us.trigg.crumble.fragments.LoginFragment()).commit();
        }
        else if (id == R.id.nav_share) { }

        Log.d(TAG, "explore activity finished, or drawer about to close");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-----------------------------------------------------------------------------------
    // Alert Dialog Handlers
    //-----------------------------------------------------------------------------------
    @Override
    public void doPositiveClick() {
        // Hide the alert
        alert.dismiss();
        // Retry
    }

    //-----------------------------------------------------------------------------------
    // Public Methods
    //-----------------------------------------------------------------------------------
    public GoogleMap getMap() {
        Log.d(TAG, "map is about to be returned");
        return mMap;
    }


    //-----------------------------------------------------------------------------------
    // Private Methods
    //-----------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void addCrumbs(JSONObject json) {
        if (json != null) {
            Log.d(TAG, "In add crumbs.");
            // 1. Wait for the mMap object to be non-null
            synchronized (mMap) {
                try {
                    if (mMap == null) {
                        Thread.sleep(20);
                    } else {
                        Log.d(TAG, "Adding crumbs to the map.");
                        // 2. Add all of the information from the data JSON object to the map as crumbs
                        try {
                            // Check to see if the request was a success
                            if (json.getString(GetAllCrumbs.TAG_SUCCESS).compareTo("FOUND") == 0) {
                                // Loop through the results in data[] to add the crumbs
                                JSONArray data = json.getJSONArray(GetAllCrumbs.TAG_PAYLOAD);
                                Log.d(TAG, "Data length is: " + data.length());
                                for (int i = 0; i < data.length(); i++) {
                                    // Got the object
                                    JSONObject mJSONCrumb = data.getJSONObject(i);
                                    // DEBUG
                                    //Log.d(TAG, mJSONCrumb.toString());
                                    // Create a Crumb object
                                    Crumb crumb = new Crumb();
                                    crumb.setTitle(mJSONCrumb.getString(OnlineCrumbTableContact.COLUMN_TITLE));
                                    String lat = mJSONCrumb.getString(OnlineCrumbTableContact.COLUMN_LATITUDE);
                                    String longi = mJSONCrumb.getString(OnlineCrumbTableContact.COLUMN_LONGITUDE);
                                    // We will set the location of the crumb later on after we determine that these values are valid

                                    //DEBUG
                                    Log.d(TAG, "Title is: " + crumb.getTitle());
                                    Log.d(TAG, "Latitude is: " + lat);
                                    Log.d(TAG, "Longitude is: " + longi);

                                    // Create a marker from the crumb
                                    // Attempt to convert the stored string into a coordinate (double)
                                    LatLng pos = null;
                                    boolean conversionSuccess;
                                    try {
                                        pos = new LatLng(Location.convert(lat), Location.convert(longi));
                                        conversionSuccess = true;
                                    } catch (IllegalArgumentException e) {
                                        conversionSuccess = false;
                                        Log.e(TAG, "Unable to add crumb becase of illeagal format.");
                                    } catch (NullPointerException e) {
                                        conversionSuccess = false;
                                        Log.e(TAG, "Unable to add crumb becase of null pointer.");
                                    }
                                    // If the location conversion was a success, add the crumb to the cluster manager
                                    // and consequentially to the map.
                                    if (conversionSuccess == true && pos != null) {
                                        crumb.setLocation(lat, longi);
                                        synchronized (mClusterManager) {
                                            mClusterManager.addItem(crumb);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }
                } catch (InterruptedException e) {

                }
            }

        }
    }

    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Crumb>(this, mMap);
        // Initialize the cluster renderer
        mClusterRenderer = new DefaultClusterRenderer<Crumb>(this, mMap, mClusterManager);
        mClusterManager.setRenderer(mClusterRenderer);

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new myInfoWindowAdapter());
        mClusterManager.setOnClusterItemClickListener(new ClusterItemClickListener());
    }


    //-----------------------------------------------------------------------------------
    // Private Classes
    //-----------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private class ClusterItemClickListener implements ClusterManager.OnClusterItemClickListener<Crumb> {

        @Override
        public boolean onClusterItemClick(Crumb crumb) {
            Log.v(TAG, "Cluster item " + crumb.getTitle() + " clicked.");
            // Lookup the marker associated with this crumb
            Marker marker = mClusterRenderer.getMarker(crumb);
            // If the marker is gone
            if (marker == null) {
                Log.v(TAG, "No marker for this crumb.");
                // Return false, and don't do anything!
                return false;
            }
            // Show it's infoWindow and populate it with the crumb data
            else {
                // Set the data in the info window
                Log.v(TAG, "Found a marker!");
                marker.showInfoWindow();
                return true;
            }
        }
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private class myInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContents;

        public myInfoWindowAdapter() {
            myContents = getLayoutInflater().inflate(R.layout.crumb_view, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            // Set the data in the inflator
            Crumb crumb = mClusterRenderer.getClusterItem(marker);
            if (crumb != null) {
                // Get the views
                TextView title = (TextView) myContents.findViewById(R.id.info_title);
                TextView bites = (TextView) myContents.findViewById(R.id.bites_num);
                TextView rating = (TextView) myContents.findViewById(R.id.rating);

                // Set the content of the views
                title.setText(crumb.getTitle());
                bites.setText(Integer.toString(crumb.getTotalDiscovered()));
                rating.setText(Float.toString(crumb.getRating()));

                // DEBUG
                Log.v(TAG, "Just showed infoWindow for crumb " + crumb.getTitle());
            } else {
                Log.v(TAG, "There was no crumb...");
            }

            return myContents;
        }

    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private class GetAllCrumbs extends AsyncTask<String, String, String> {

        // Progress Dialog
        private ProgressDialog pDialog;

        // JSON parser class
        private JSONParser jsonParser = new JSONParser();

        // JSON Object
        private JSONObject mJson;

        // Status flag
        private String status;

        // Status results
        public static final String TAG_SUCCESS = "status";
        public static final String TAG_PAYLOAD = "data";

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Retreiving crumbs...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Get crumbs by making HTTP request
                mJson = jsonParser.makeHttpRequest(
                        WebConstants.URL_ALL_CRUMBS, "GET", null);
                // json success tag
                status = mJson.getString(TAG_SUCCESS);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // There's not an internet connection
                // Throw up a dialog that lets the user retry
                alert = new NoConnectionAlertFragment();
                alert.show(getFragmentManager(),"Alert Dialog");

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            (new Runnable() {
                public void run() {
                    /**
                     * Call function in UI thread to update the  map
                     * */
                    addCrumbs(mJson);
                }
            }).run();
        }
    }


    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
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

}
