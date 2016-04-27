package us.trigg.crumble.interfaces;

import java.util.ArrayList;

import us.trigg.crumble.Crumb;

/**
 * Created by trigglatour on 4/27/16.
 */
public abstract interface WebComHandler {
    public void onGetOwnedCrumbs(ArrayList<Crumb> crumbs);
}
