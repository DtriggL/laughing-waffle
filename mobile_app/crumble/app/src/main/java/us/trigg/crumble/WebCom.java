package us.trigg.crumble;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static us.trigg.crumble.WebConstants.*;
import static us.trigg.crumble.WebConstants.OnlineUserTableContact.*;
import static us.trigg.crumble.WebConstants.OnlineLogbookTableContact.*;
import static us.trigg.crumble.WebConstants.OnlineCrumbTableContact.*;

import us.trigg.crumble.fragments.NoConnectionAlertFragment;
import us.trigg.crumble.interfaces.WebComHandler;

/**
 * Created by trigglatour on 4/27/16.
 */
public class WebCom {
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
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void userLogin(String username, String password) {
        List <NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(COLUMN_USERNAME, username));
        params.add(new BasicNameValuePair(COLUMN_USER_PASSWORD, password));

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
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
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

        WebRequest request = new WebRequest(URL_ADD_CRUMB, "POST", params);
        request.showProgressDialog(false);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }


    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private class WebRequest extends AsyncTask<String, String, String> {

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
                alert = new NoConnectionAlertFragment();
                alert.show(webComHandler.getFragmentManager(),"Alert Dialog");

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
            if (url.compareTo(URL_GET_USER_CREATED_CRUMBS) == 0) {
                webComHandler.onGetOwnedCrumbs(json);
            } else if (url.compareTo(URL_ALL_CRUMBS) == 0) {
                webComHandler.onGetAllCrumbs(json);
            } else if (url.compareTo(URL_GET_USER_FOUND_CRUMBS) == 0) {
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
            } else if (url.compareTo(URL_GET_CRUMB) == 0) {
                webComHandler.onGetCrumb(json);
            } else if (url.compareTo(URL_ADD_CRUMB) == 0) {
                webComHandler.onAddCrumb(json);
            }
        }
    }
}
