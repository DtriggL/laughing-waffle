package us.trigg.crumble;

/**
 * Created by trigglatour on 4/12/16.
 */
public class WebConstants {

    // URLs
    public static final String SERVER_ADDR = "http://uaf132701.ddns.uark.edu/api/";

    public static final String URL_ALL_CRUMBS= SERVER_ADDR + "crumb/all";

    public static final String URL_ADD_CRUMB = SERVER_ADDR + "crumb/add";

    public static final String URL_GET_USER_CREATED_CRUMBS = SERVER_ADDR + "user/get/createdCrumbs/";

    // JSON Result Tags
    public static final String STATUS_TAG = "status";
    public static final String PAYLOAD_TAG = "data";

    public static class OnlineCrumbTableContact {
        private OnlineCrumbTableContact() {}

        // COLUMNS
        public static final String COLUMN_CRUMB_ID  = "crumb_id";
        public static final String COLUMN_LATITUDE  = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TOTAL_DISCOVERED = "total_discovered";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MESSAGE = "message";

    }
}
