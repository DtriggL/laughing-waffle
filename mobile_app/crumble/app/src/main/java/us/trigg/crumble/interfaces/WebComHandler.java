package us.trigg.crumble.interfaces;

import android.support.v4.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import us.trigg.crumble.Crumb;

/**
 * Created by trigglatour on 4/27/16.
 */
public abstract interface WebComHandler {
    public void onGetOwnedCrumbs(JSONObject json);
    public void onGetAllCrumbs (JSONObject json);
    public android.app.FragmentManager getFragmentManager();
}
