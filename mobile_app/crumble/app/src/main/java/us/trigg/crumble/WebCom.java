package us.trigg.crumble;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import us.trigg.crumble.fragments.NoConnectionAlertFragment;
import us.trigg.crumble.interfaces.MyFragmentDialogInterface;
import us.trigg.crumble.interfaces.WebComHandler;

import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_CREATOR_ID;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_CRUMB_ID;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_LATITUDE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_LONGITUDE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_MESSAGE;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_RATING;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.COLUMN_TITLE;
import static us.trigg.crumble.WebConstants.OnlineLogbookTableContact.LOGBOOK_CONTENT;
import static us.trigg.crumble.WebConstants.OnlineLogbookTableContact.LOGBOOK_USER_ID;
import static us.trigg.crumble.WebConstants.OnlineUserTableContact.COLUMN_EMAIL;
import static us.trigg.crumble.WebConstants.OnlineUserTableContact.COLUMN_FIRST_NAME;
import static us.trigg.crumble.WebConstants.OnlineUserTableContact.COLUMN_LAST_NAME;
import static us.trigg.crumble.WebConstants.OnlineUserTableContact.COLUMN_USERNAME;
import static us.trigg.crumble.WebConstants.OnlineUserTableContact.COLUMN_USER_PASSWORD;
import static us.trigg.crumble.WebConstants.URL_ADD_CRUMB;
import static us.trigg.crumble.WebConstants.URL_ALL_CRUMBS;
import static us.trigg.crumble.WebConstants.URL_CRUMB_FIND;
import static us.trigg.crumble.WebConstants.URL_CRUMB_RATE;
import static us.trigg.crumble.WebConstants.URL_GET_CRUMB;
import static us.trigg.crumble.WebConstants.URL_GET_USER_CREATED_CRUMBS;
import static us.trigg.crumble.WebConstants.URL_GET_USER_FOUND_CRUMBS;
import static us.trigg.crumble.WebConstants.URL_USER_ADD;
import static us.trigg.crumble.WebConstants.URL_USER_LOGBOOK;
import static us.trigg.crumble.WebConstants.URL_USER_LOGBOOK_ADD;
import static us.trigg.crumble.WebConstants.URL_USER_LOGIN;

/**
 * Created by trigglatour on 4/27/16.
 */
public class WebCom {
    public static final String TAG = "WebCom";
    private WebComHandler webComHandler;
    private Context context;

    public WebCom(WebComHandler handler, Context c) {
        context = c;
        webComHandler = handler;
    }

    public void getOwnedCrumbs(int user_id) {
        String user_id_str = Integer.toString(user_id);
        // Make an Async task to handle the request
        WebRequest request = new WebRequest(URL_GET_USER_CREATED_CRUMBS + user_id_str, "GET", null);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void getAllCrumbsBrief() {
        WebRequest request = new WebRequest(URL_ALL_CRUMBS, "GET", null);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void getFoundCrumbs(int user_id) {
        String user_id_str = Integer.toString(user_id);
        WebRequest request = new WebRequest(URL_GET_USER_FOUND_CRUMBS + user_id_str, "GET", null);
        Log.d("GETFOUNDCRUMB", URL_GET_USER_FOUND_CRUMBS + user_id_str);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void userLogin(String username, String password) {
        List <NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(COLUMN_USERNAME, username));
        params.add(new BasicNameValuePair(COLUMN_USER_PASSWORD, password));

        Log.v(TAG, "Password is: " + password);
        WebRequest request = new WebRequest(URL_USER_LOGIN, "POST", params);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void addUser(
            String username, String password, String first_name,
            String last_name, String email )
    {
        List <NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(COLUMN_USERNAME, username));
        params.add(new BasicNameValuePair(COLUMN_USER_PASSWORD, password));
        params.add(new BasicNameValuePair(COLUMN_FIRST_NAME, first_name));
        params.add(new BasicNameValuePair(COLUMN_LAST_NAME, last_name));
        params.add(new BasicNameValuePair(COLUMN_EMAIL, email));

        WebRequest request = new WebRequest(URL_USER_ADD, "POST", params);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void getUserLogbook(int user_id) {
        String user_id_str = Integer.toString(user_id);

        WebRequest request = new WebRequest(URL_USER_LOGBOOK + user_id_str, "GET", null);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void addLogbookEntry(int user_id, String content) {
        // Note, we're submitting a post request with a String encoded integer,
        // not sure how the server likes that
        String user_id_str = Integer.toString(user_id);
        List <NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(LOGBOOK_USER_ID, user_id_str));
        params.add(new BasicNameValuePair(LOGBOOK_CONTENT, content));

        WebRequest request = new WebRequest(URL_USER_LOGBOOK_ADD, "POST", params);
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void findCrumb(int user_id, int crumb_id) {
        String user_id_str = Integer.toString(user_id);
        String crumb_id_str = Integer.toString(crumb_id);
        String url = URL_CRUMB_FIND + crumb_id_str + "/" + user_id_str;

        WebRequest request = new WebRequest(url, "GET", null);
        request.showProgressDialog(false);
        request.execute(null, null, null);
        // Result posted to parent
    }

    public void getCrumb(int crumb_id) {
        String crumb_id_str = Integer.toString(crumb_id);
        String url = URL_GET_CRUMB + crumb_id_str;

        WebRequest request = new WebRequest(url, "GET", null);
        request.showProgressDialog(false);
        request.execute(null, null, null);
    }

    public void addCrumb(int creator_id, String title, String latitude,
                                String longitude, String message) {
        String creator_id_str = Integer.toString(creator_id);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(COLUMN_CREATOR_ID, creator_id_str));
        params.add(new BasicNameValuePair(COLUMN_TITLE, title));
        params.add(new BasicNameValuePair(COLUMN_LATITUDE, latitude));
        params.add(new BasicNameValuePair(COLUMN_LONGITUDE, longitude));
        params.add(new BasicNameValuePair(COLUMN_MESSAGE, message));
        Log.d(TAG, message + " add Crumb in WebCom message");

        WebRequest request = new WebRequest(URL_ADD_CRUMB, "POST", params);
        request.showProgressDialog(false);
        request.execute(null, null, null);
        // Result posted to parent
    }

    public void rateCrumb(int crumb_id, float rating) {
        String crumb_id_str = Integer.toString(crumb_id);
        String rating_str = Float.toString(rating);
        List <NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(COLUMN_RATING, rating_str));
        params.add(new BasicNameValuePair(COLUMN_CRUMB_ID, crumb_id_str));

        WebRequest request = new WebRequest(URL_CRUMB_RATE, "POST", params);
        request.showProgressDialog(false);
        request.execute(null, null, null);
        // Result posted to parent
    }


    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private class WebRequest extends AsyncTask<String, String, String> implements MyFragmentDialogInterface {

        private String url;
        private String method;
        private List<NameValuePair> parameters;

        // Progress Dialog
        private ProgressDialog pDialog;
        // No connection dialog
        NoConnectionAlertFragment alert;
        private boolean showPDialog;

        // JSON parser class
        private JSONParser jsonParser = new JSONParser();

        // JSON Object
        private JSONObject mJson;

        // Status flag
        private String status;

        // Status results
        public static final String TAG_SUCCESS = "status";
        public static final String TAG_PAYLOAD = "data";

        // Constructor
        public WebRequest(String requestUrl, String httpMethod, List<NameValuePair> params){
            super();
            url = requestUrl;
            method = httpMethod;
            parameters = params;
            showPDialog = false;
        }

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Retreiving crumbs...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if (showPDialog) {
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Get crumbs by making HTTP request
                mJson = jsonParser.makeHttpRequest(
                        url, method, parameters);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // There's not an internet connection
                // Throw up a dialog that lets the user retry
//                alert = new NoConnectionAlertFragment();
//                alert.show(webComHandler.getMyFragmentManager(),"Alert Dialog");

            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (showPDialog) {
                // dismiss the dialog once product deleted
                pDialog.dismiss();
            }
            (new Runnable() {
                public void run() {
                    /**
                     * Call function in UI thread to update the  map
                     * */

                    postResult(mJson, url);
                }
            }).run();
        }
        public void showProgressDialog(boolean show) {
            showPDialog = show;
        }

        //-----------------------------------------------------------------------------------
        // postResult
        //
        // Posts the result to the correct result handler depending on the URL selected.
        //-----------------------------------------------------------------------------------
        protected void postResult(JSONObject json, String url) {
            String url_user_created_crumbs = url.substring(0, url.length() - 2);
            String url_get_crumbParsed = URL_GET_CRUMB.substring(0, URL_GET_CRUMB.length() - 2);
            Log.d(TAG, "post Result in here Manoj" + url_user_created_crumbs + " " + url_get_crumbParsed);

            if (url.contains(URL_GET_USER_CREATED_CRUMBS)) {
                webComHandler.onGetOwnedCrumbs(json);
            } else if (url.contains(URL_CRUMB_RATE)) {
                webComHandler.onRateCrumb(json);
            } else if (url.compareTo(URL_ALL_CRUMBS) == 0) {
                webComHandler.onGetAllCrumbs(json);
            } else if (url.contains(URL_GET_USER_FOUND_CRUMBS)) {
                webComHandler.onGetFoundCrumbs(json);
            } else if (url.compareTo(URL_USER_LOGIN) == 0) {
                webComHandler.onUserLogin(json);
            } else if (url.compareTo(URL_USER_ADD) == 0) {
                webComHandler.onUserAdd(json);
            } else if (url.compareTo(URL_USER_LOGBOOK) == 0) {
                webComHandler.onGetUserLogbook(json);
            } else if (url.compareTo(URL_USER_LOGBOOK_ADD) == 0) {
                webComHandler.onAddLogbookEntry(json);
            } else if (url.compareTo(URL_CRUMB_FIND) == 0) {
                webComHandler.onFindCrumb(json);
            } else if (url.contains(URL_GET_CRUMB)) {
                webComHandler.onGetCrumb(json);
                Log.d(TAG, " webComHandler.onGetCrumb(json); is getting called" + json.toString());
            } else if (url.compareTo(URL_ADD_CRUMB) == 0) {
                webComHandler.onAddCrumb(json);
            }
        }

        @Override
        public void doPositiveClick() {
            //alert.dismiss();
        }

        @Override
        public void doNegativeClick() {

        }
    }
}
