package us.trigg.crumble;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import us.trigg.crumble.fragments.LogbookFragment;
import us.trigg.crumble.fragments.LoginFragment;
import us.trigg.crumble.fragments.NoConnectionAlertFragment;
import us.trigg.crumble.fragments.PinsFragment;
import us.trigg.crumble.interfaces.MyFragmentDialogInterface;
import us.trigg.crumble.interfaces.WebComHandler;

import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact;
import static us.trigg.crumble.WebConstants.PAYLOAD_TAG;
import static us.trigg.crumble.WebConstants.STATUS_TAG;

// TODO:
// 1. (DONE) Fix server code to send right information
// 2. Update the HUD correctly
// 3. Be able to stop a route
// 4. (DONE) Don't want to be able to route to multiple crumbs
// 5. Download crumb content when routed to it
// 6. (DONE) Don't want crumb that you're routed to to be clustered

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMyLocationButtonClickListener,
        MyFragmentDialogInterface,
        WebComHandler {

    // Constants
    public static final String EXTRA_MESSAGE = "Explore Activity";
    public static final String TAG = "Main Activity";
    public static final int FAB_GO = 1;
    public static final int FAB_ADD = 0;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final float DISTANCE_REQUIRED = 10; // Distance in meters

    private ClusterManager<Crumb> mClusterManager;
    private MyClusterRender mClusterRenderer;

    private GoogleMap mMap;

    // Locaiton API client
    protected GoogleApiClient mGoogleApiClient;
    // Location Request
    protected LocationRequest mLocationRequest;

    // Location
    protected Location mCurrentLocation;

    // Sensor Variables
    private SensorManager mSensorManager;
    Sensor magnetometer;

    // Fragments
    // Alert Fragment
    private NoConnectionAlertFragment alert;
    SupportMapFragment sMapFragment;

    //fab
    private FloatingActionButton fab;

    // Web Communications Module
    WebCom myWebCom;

    // UI Elements
    // HUD
    private TextView heading;
    private TextView altitude;
    private TextView distance;

    // Route Variables
    Crumb toCrumb;
    Crumb selectedCrumb;
    boolean routed;
    Polyline routeLine;

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

        // Setup the view elements
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        heading = (TextView) findViewById(R.id.heading);
        altitude = (TextView) findViewById(R.id.altitude);
        distance = (TextView) findViewById(R.id.distance);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frag_manager_frame, new LoginFragment()).commit();

        sMapFragment = SupportMapFragment.newInstance();
        sMapFragment.getMapAsync(this);

        // Web Communications Module Initialization
        myWebCom = new WebCom(this, this);

        // Initialization of sensors
        setupSensorManager();

        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        sFm.beginTransaction().add(R.id.map, sMapFragment).commit();


        // Start a fused API to get the user's current location
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Create the location request
        createLocationRequest();

        // Initialize route attributes
        toCrumb = null;
        routed = false;
        hideHUD();
        routeLine = null;
        selectedCrumb = null;

        setupFAB();
    }

    @Override
    public void onStart() {
        // Connect to the Play API
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        if (routed) {
            showHUD();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

        // Set up the map padding
        mMap.setPadding(0, 60, 0, 0);

        setUpClusterer();

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setOnMapClickListener(new MyOnMapClickListener());

        // Get all of the crumbs from the server and add them to the map
        //new GetAllCrumbs().execute(null, null, null);
        myWebCom.getAllCrumbsBrief();
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
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        // Get the current location services settings
                        startLocationUpdates();
                        // Setup my location on the map
                        try {
                            mMap.setMyLocationEnabled(true);
                        } catch (SecurityException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Location Services not enabled.",
                                    Toast.LENGTH_LONG).show();
                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        break;
                }
            }
        });


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    //-----------------------------------------------------------------------------------
    // WebComHandler Event Handlers
    //-----------------------------------------------------------------------------------
    @Override
    public void onGetOwnedCrumbs(JSONObject json) {}

    @Override
    public void onGetAllCrumbs(JSONObject json) {
        addCrumbs(json);
    }

    @Override
    public void onGetFoundCrumbs(JSONObject json){}

    @Override
    public void onUserLogin(JSONObject json){}

    @Override
    public void onUserAdd(JSONObject json) {}

    @Override
    public void onGetUserLogbook(JSONObject json) {}

    @Override
    public void onAddLogbookEntry(JSONObject josn) {}

    @Override
    public void onFindCrumb(JSONObject json) {}

    @Override
    public void onGetCrumb(JSONObject json) {}

    @Override
    public void onAddCrumb(JSONObject json) {}

    //-----------------------------------------------------------------------------------
    // Floating Action Button Visibility
    //-----------------------------------------------------------------------------------
    public void showFloatingActionButton() {
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }
    public void setFloatingActionButtonFunction(int function) {
        switch (function) {
            case FAB_GO:
                // Set the src icon
                Drawable go_icon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.go);
                fab.setImageDrawable(go_icon);
                // Set the on click
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Route to the selected crumb
                        if (selectedCrumb != null) {
                            routeTo(selectedCrumb);
                        }
                    }
                });

                break;
            case FAB_ADD:
                // Set the src icon
                Drawable add_icon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.add_crumb);
                fab.setImageDrawable(add_icon);
                // Set the on click
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                        LinearLayout layout = new LinearLayout(MainActivity.this);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        final EditText titleBox = new EditText(MainActivity.this);
                        titleBox.setHint("Crumb Title");
                        layout.addView(titleBox);

                        final EditText contentBox = new EditText(MainActivity.this);
                        contentBox.setHint("Crumb Message");
                        layout.addView(contentBox);

                        alert.setTitle("Add Crumb");
                        //alert.setMessage("Message");

                        alert.setView(layout);

                        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //insert crumb with title=stringTitle and message=stringContent
                                if(mCurrentLocation != null) {
                                    String stringTitle = titleBox.getText().toString();
                                    String stringContent = contentBox.getText().toString();
                                    String lat = Double.toString(mCurrentLocation.getLatitude());
                                    String lng = Double.toString(mCurrentLocation.getLongitude());

                                    new CreateNewCrumb().execute(stringTitle, stringContent, lat, lng);
                                }
                                else{
                                    Log.d(TAG, "Location null in FAB onClick");
                                    Toast.makeText(getParent(), "No location determined. Make sure location service is enabled", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                        alert.show();
                    }
                });
                break;
            default:
                break;
        }
        // Set the icon
        // Set the onclick listener
    }

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

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        //On every menu button click, the map fragment get's hidden
        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();
        if (id != R.id.nav_explore) {
            hideHUD();
        }

        if (id == R.id.nav_explore) {
            showFloatingActionButton();
            if (routed) {
                showHUD();
            }
            //The first time, map fragment needs to be added. After that, it just needs to be shown.
            if (!sMapFragment.isAdded())
                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
            else
                sFm.beginTransaction().show(sMapFragment).commit();

        } else if (id == R.id.nav_logbook) {
            hideFloatingActionButton();
            fm.beginTransaction().replace(R.id.frag_manager_frame, new LogbookFragment()).commit();
        }

        else if (id == R.id.nav_myPins) {
            hideFloatingActionButton();
            fm.beginTransaction().replace(R.id.frag_manager_frame, new PinsFragment()).commit();
        }
        else if (id == R.id.nav_nearMe) { }
        else if (id == R.id.nav_login) {
            hideFloatingActionButton();
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
    private void setupSensorManager() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            // Success! There's a magnetometer.
            magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        } else {
            // Failure! No magnetometer.
        }
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private float getDistanceToCrumb(Location location, Crumb crumb) {
        Location crumbLocation = new Location("");
        LatLng crumbPosition = crumb.getPosition();
        crumbLocation.setLatitude(crumbPosition.latitude);
        crumbLocation.setLongitude(crumbPosition.longitude);
        return location.distanceTo(crumbLocation);
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void setupFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setFloatingActionButtonFunction(FAB_ADD);
        fab.show();
    }
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void hideHUD() {
        TextView headingTitle = (TextView) findViewById(R.id.heading_title);
        TextView altitudeTitle = (TextView) findViewById(R.id.altitude_title);
        TextView distanceTitle = (TextView) findViewById(R.id.distance_title);
        headingTitle.setVisibility(View.INVISIBLE);
        heading.setVisibility(View.INVISIBLE);
        altitudeTitle.setVisibility(View.INVISIBLE);
        altitude.setVisibility(View.INVISIBLE);
        distanceTitle.setVisibility(View.INVISIBLE);
        distance.setVisibility(View.INVISIBLE);

    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void showHUD() {
        TextView headingTitle = (TextView) findViewById(R.id.heading_title);
        TextView altitudeTitle = (TextView) findViewById(R.id.altitude_title);
        TextView distanceTitle = (TextView) findViewById(R.id.distance_title);
        headingTitle.setVisibility(View.VISIBLE);
        heading.setVisibility(View.VISIBLE);
        altitudeTitle.setVisibility(View.VISIBLE);
        altitude.setVisibility(View.VISIBLE);
        distanceTitle.setVisibility(View.VISIBLE);
        distance.setVisibility(View.VISIBLE);
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void routeTo(Crumb crumb) {
        // Check to see if we were already routed and end it
        if ((routed) && (routeLine != null)) {
            routeLine.remove();
        }
        toCrumb = crumb;
        // Set the crumb so that it will not be clustered
        Marker marker = mClusterRenderer.getMarker(toCrumb);
        if (marker != null) {

        }
        // TODO: Download additional crumb details and save them to the crumb
        routed = true;
        // Turn on the HUD
        showHUD();
        // Draw the a PolyLine to the crumb
        LatLng userPosition = new LatLng(
                mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude()
        );
        Log.v(TAG, "Routed User Position is : " + userPosition.toString());
        routeLine = this.mMap.addPolyline(new PolylineOptions()
                .add(toCrumb.getPosition(), userPosition)
                .color(R.color.route_purple)
        );
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void endRoute() {
        routed = false;
        toCrumb = null;
        routeLine.remove();
    }

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
                            if (json.getString(STATUS_TAG).compareTo("FOUND") == 0) {
                                // Loop through the results in data[] to add the crumbs
                                JSONArray data = json.getJSONArray(PAYLOAD_TAG);
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
                                    crumb.setCrumb_id(mJSONCrumb.getInt(OnlineCrumbTableContact.COLUMN_CRUMB_ID));
                                    crumb.setTotalDiscovered(mJSONCrumb.getInt(OnlineCrumbTableContact.COLUMN_TOTAL_DISCOVERED));
                                    crumb.setRating(Float.parseFloat(mJSONCrumb.getString(OnlineCrumbTableContact.COLUMN_RATING)));
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
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Crumb>(this, mMap);
        // Initialize the cluster renderer
        mClusterRenderer = new MyClusterRender(this, mMap, mClusterManager);
        mClusterManager.setRenderer(mClusterRenderer);

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new myInfoWindowAdapter());
        mClusterManager.setOnClusterItemClickListener(new ClusterItemClickListener());
    }
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    new MyLocationListener());
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(),
                    "Location Services not enabled.",
                    Toast.LENGTH_LONG).show();
        }
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

            // Set the selected crumb
            selectedCrumb = crumb;

            // Change the icon on the FAB
            setFloatingActionButtonFunction(FAB_GO);

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
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            Log.v(TAG, "Location Updated: " + location.toString());
            updateMap();
            updateHUD();
        }

        private void updateMap() {
            if (routed) {
                // If so, call drawRoute and update distance, bearing, and altitude. drawRoute(); updateHUD();
                // If distance is less than required, launch the crumb display fragment.
                drawRoute();
                checkFound();
            }
        }

        private void checkFound() {
            if (getDistanceToCrumb(mCurrentLocation, toCrumb) < DISTANCE_REQUIRED){
                // Mark the crumb as found online
                // Display the activity
            }
        }
        private void drawRoute() {
            // Draw a line on the map from the user's current position to the position of
            // toCrumb
            LatLng userPosition = new LatLng(
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude()
            );
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            points.add(toCrumb.getPosition());
            points.add(userPosition);
            if (routeLine != null) {
                // Set the new points of the polyLine
                routeLine.setPoints(points);
            }
        }

        private void updateHUD() {
            // Set the altitude
            double alt = 3.28084 * mCurrentLocation.getAltitude();
            if (alt != 0) {
                String alt_str = String.format("%.2f", altitude);
                altitude.setText(alt_str);
            }
            heading.setText(Float.toString(mCurrentLocation.getBearing()));
            distance.setText(Float.toString(mCurrentLocation.getSpeed()));
        }
    }

    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------
    private class CreateNewCrumb extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        private JSONParser jsonParser = new JSONParser();
        public static final String TAG_SUCCESS = "success";


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Creating Crumb...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating crumb
         */
        protected String doInBackground(String... args) {
            String title = args[0];
            String message = args[1];
            String lat = args[2];
            String lng = args[3];

            Log.d(TAG, title);
            Log.d(TAG, message);
            Log.d(TAG, lat);
            Log.d(TAG, lng);

            //building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("creator_id","1"));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("latitude", lat));
            params.add(new BasicNameValuePair("longitude", lng));
            params.add(new BasicNameValuePair("message", message));

            JSONObject json = null;
            try {
                json = jsonParser.makeHttpRequest(WebConstants.URL_ADD_CRUMB,
                        "POST", params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
                String success = json.optString(TAG_SUCCESS);

                if (success != null) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }


            return null;
        }

        /**
         * After completing background task dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }

    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------
    private class MyOnMapClickListener implements GoogleMap.OnMapClickListener {
        @Override
        public void onMapClick(LatLng latLng) {
            // Set the function of the floating action button to be add crumb
            setFloatingActionButtonFunction(FAB_ADD);
        }
    }

    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------
    private class MyClusterRender extends DefaultClusterRenderer<Crumb> {

        public static final int MIN_CLUSTER_SIZE = 4;

        public MyClusterRender(Context context, GoogleMap map, ClusterManager<Crumb> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<Crumb> cluster) {
            // If it contains the toCrumb, don't render as a cluster
            if ((cluster.getItems().contains(toCrumb)) && routed) {
                return false;
            } else {
                return cluster.getSize() > MIN_CLUSTER_SIZE;
            }
        }
    }

    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------
    private class MySensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
        
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

}
