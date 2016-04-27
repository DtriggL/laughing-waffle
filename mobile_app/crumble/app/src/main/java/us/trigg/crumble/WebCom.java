package us.trigg.crumble;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import static us.trigg.crumble.WebConstants.*;

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
        WebRequest request = new WebRequest(URL_GET_USER_CREATED_CRUMBS + user_id_str, "GET");
        request.showProgressDialog(true);
        request.execute(null, null, null);
        // Result posted to parent with onGetOwnedCrumb from the AsyncTask
    }

    public void getAllCrumbsBrief() {
        WebRequest request = new WebRequest(URL_ALL_CRUMBS, "GET");
        request.showProgressDialog(true);
        request.execute(null, null, null);
    }

    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    private class WebRequest extends AsyncTask<String, String, String> {

        private String url;
        private String method;
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
        public WebRequest(String requestUrl, String httpMethod){
            super();
            url = requestUrl;
            method = httpMethod;
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
                        url, method, null);
                // json success tag
                status = mJson.getString(TAG_SUCCESS);

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
            }
        }
    }
}
