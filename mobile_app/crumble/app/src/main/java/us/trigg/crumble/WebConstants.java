package us.trigg.crumble;

/**
 * Created by trigglatour on 4/12/16.
 */
public class WebConstants {
    public static final String SERVER_ADDR = "http://uaf132701.ddns.uark.edu/api/";

    public static final String URL_ALL_CRUMBS= SERVER_ADDR + "crumb/all";

    public static final String URL_ADD_CRUMB = SERVER_ADDR + "crumb/add";

    public static class OnlineCrumbTableContact {
        private OnlineCrumbTableContact() {}

        // COLUMNS
        public static final String COOUMN_CRUMB_ID  = "crumb_id";
        public static final String COLUMN_LATITUDE  = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MESSAGE = "message";

    }
}
