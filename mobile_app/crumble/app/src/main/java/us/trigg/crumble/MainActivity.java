package us.trigg.crumble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "Explore Activity";
    public static final String TAG = "Main Activity";

    ArrayList logEntries;
    ArrayAdapter logEntriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        logEntries = new ArrayList<String>();
        logEntriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                logEntries);
        ListView logBookListView = (ListView) findViewById(R.id.logbookListView);
        logBookListView.setAdapter(logEntriesAdapter);

        logEntries.add("2/16/16 Discovered: The best fishing spot on Beaver Lake\nuser: tdlatour");
        logEntries.add("3/02/16 Discovered: hiking must see @ sunset\nuser: jcl006");
        logEntries.add("3/03/16 Discovered: hiking must see @ sunrise\nuser: jcl006");
        logEntries.add("3/10/16 Discovered: 11 things you never expected to find in Central Park\nuser: mseeram");

        logEntriesAdapter.notifyDataSetChanged();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d(TAG, "on navigation item selected");
        if (id == R.id.explore) {
            Intent intent = new Intent(this, Explore.class);
            intent.putExtra(EXTRA_MESSAGE, 1);
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
