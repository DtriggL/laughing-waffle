package us.trigg.crumble;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// TODO:
//  1. Override the MarkerCluster OnInfoWindowListener

class explore extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<CrumbClusterItem> mClusterManager;
    private HashMap<Marker, Crumb> markers;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        setUpClusterer();
        // Test Code:
        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(5, -5), 10));
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
        } catch (Exception e) {

        }
    }

    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "explore Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://us.trigg.crumble/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "explore Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://us.trigg.crumble/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //-------------------------------------------------------------------------
    // Private Classes
    //-------------------------------------------------------------------------
    private class MyInfoWindow implements GoogleMap.OnInfoWindowClickListener,
            GoogleMap.InfoWindowAdapter {
        private View myInfoWindowView;

        public MyInfoWindow() {
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
            // Get the layout
            myInfoWindowView = getLayoutInflater().inflate(R.layout.info_window, null);
            // Set the text elements within the layout
            TextView title = (TextView) myInfoWindowView.findViewById(R.id.info_title);
            // TODO: Lookup crumb name and set as title
            // For now, set title to be "Name"
            title.setText("Name");

            TextView bites = (TextView) myInfoWindowView.findViewById(R.id.bites_num);
            // TODO: Lookup crumb bites and set here
            // For now, set bites to be 0
            bites.setText("0");

            TextView rating = (TextView) myInfoWindowView.findViewById(R.id.rating);
            rating.setText("0/5");

            // Set the images within the layout
            ImageView star = (ImageView) myInfoWindowView.findViewById(R.id.rating_img);
            star.setImageResource(R.drawable.star2);
            ImageView hiker = (ImageView) myInfoWindowView.findViewById(R.id.bites_img);
            hiker.setImageResource(R.drawable.hikingman);

            return myInfoWindowView;
        }
    }

    private class MyOnMarkerClick implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(getApplicationContext(), "Marker clicked",
                    Toast.LENGTH_SHORT).show();
            marker.showInfoWindow();
            return true;
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
        getMap().setOnMarkerClickListener(new MyOnMarkerClick());
        // Pass this function the custom layout.
        getMap().setInfoWindowAdapter(new MyInfoWindow());

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {
        Random gen = new Random();
        // Initial test code: add 200 randomly placed objects to the cluster manager
        for (int i = 0; i < 200; i++) {
            double lat = gen.nextDouble() * 90;
            double lon = gen.nextDouble() * 180;
            mClusterManager.addItem(new CrumbClusterItem(new Crumb(new LatLng(lat, lon))));
        }
    }


}
