package us.trigg.crumble;

/**
 * Created by trigglatour on 4/12/16.
 */
public class WebConstants {

    // URLs
    public static final String SERVER_ADDR = "http://uaf132701.ddns.uark.edu/api/";

    public static final String URL_ALL_CRUMBS= SERVER_ADDR + "crumb/all";

    public static final String URL_ADD_CRUMB = SERVER_ADDR + "crumb/add";

    public static final String URL_GET_CRUMB = SERVER_ADDR + "crumb/";

    public static final String URL_GET_USER_CREATED_CRUMBS = SERVER_ADDR + "user/get/createdCrumbs/";

    public static final String URL_GET_USER_FOUND_CRUMBS = SERVER_ADDR + "user/get/discoveredCrumbs/";

    public static final String URL_USER_LOGIN = SERVER_ADDR + "user/login";

    public static final String URL_CRUMB_FIND = SERVER_ADDR + "crumb/find/";

    public static final String URL_USER_LOGBOOK = SERVER_ADDR + "user/logbook/";

    public static final String URL_USER_LOGBOOK_ADD = SERVER_ADDR + "user/logbook/add";

    public static final String URL_USER_ADD = SERVER_ADDR + "user/add";

    // JSON Result Tags
    public static final String STATUS_TAG = "status";
    public static final String PAYLOAD_TAG = "data";

    // JSON Status Field Values
    public static final String STATUS_FOUND = "FOUND";
    public static final String STATUS_NOT_FOUND = "NOT-FOUND";

    public static class OnlineCrumbTableContact {
        private OnlineCrumbTableContact() {}

        // COLUMNS
        public static final String COLUMN_CRUMB_ID  = "crumb_id";
        public static final String COLUMN_LATITUDE  = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TOTAL_DISCOVERED = "total_discovered";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RATINGS = "ratings";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_CREATOR_ID = "creator_id";
        public static final String COLUMN_CREATE_DATE = "creation_date";

    }

    public static class OnlineUserTableContact {
        private OnlineUserTableContact() {}

        // Columns
        public static final String COLUMN_USER_PASSWORD = "password";
        public static final String COLUMN_USERNAME = "user_name";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_USER_ID = "user_id";
    }

    public static class OnlineLogbookTableContact {
        private OnlineLogbookTableContact() {
        }
        // Columns
        public static final String LOGBOOK_USER_ID = "user_id";
        public static final String LOGBOOK_CONTENT = "content";
        public static final String LOGBOOK_ENTRY_ID = "entry_id";
    }

    public static class OnlineDiscoveredTableContact {
        private OnlineDiscoveredTableContact() {}

        public static final String DISCOVERD_CRUMB_ID = "c_id";
        public static final String DISCOVERED_USER_ID = "u_id";
    }
}
