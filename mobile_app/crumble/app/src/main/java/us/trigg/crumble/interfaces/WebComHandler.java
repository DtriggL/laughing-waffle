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
    public void onGetFoundCrumbs (JSONObject json);
    public void onUserLogin(JSONObject json);
    public void onUserAdd(JSONObject json);
    public void onGetUserLogbook(JSONObject json);
    public void onAddLogbookEntry(JSONObject json);
    public void onFindCrumb(JSONObject json);
    public void onGetCrumb(JSONObject json);
    public void onAddCrumb(JSONObject json);
    public android.support.v4.app.FragmentManager getMyFragmentManager();
}
