package us.trigg.crumble;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by trigglatour on 3/4/16.
 */
public class CrumbClusterItem implements ClusterItem  {
    private Crumb crumb;

    CrumbClusterItem( Crumb c ) {
        crumb = c;
    }

    @Override
    public LatLng getPosition() {
        return crumb.getPosition();
    }
}
