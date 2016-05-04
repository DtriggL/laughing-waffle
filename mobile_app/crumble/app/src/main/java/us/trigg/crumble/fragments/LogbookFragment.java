package us.trigg.crumble.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import us.trigg.crumble.ListViewAdapter;
import us.trigg.crumble.R;
import us.trigg.crumble.WebCom;
import us.trigg.crumble.WebConstants;
import us.trigg.crumble.interfaces.WebComHandler;

import static us.trigg.crumble.WebConstants.PAYLOAD_TAG;

public class LogbookFragment extends Fragment implements WebComHandler {

    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logbook, container, false);

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), list);
        listView = (ListView) rootView.findViewById(R.id.logbookListView);
        listView.setAdapter(adapter);

        HashMap<String,String> temp=new HashMap<>();
        temp.put("First", "Activity");
        //temp.put("Second", "Views");
        //temp.put("Third", "Rating");
        list.add(temp);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.SharedPreferencesKey), Context.MODE_PRIVATE);

        int u_id = sharedPreferences.getInt("user_id", 0);

        WebComHandler wcHandler = this;
        WebCom wc = new WebCom(wcHandler, this.getContext());
        wc.getUserLogbook(u_id);


        return rootView;

    }

    @Override
    public void onGetOwnedCrumbs(JSONObject json) {

    }

    @Override
    public void onGetAllCrumbs(JSONObject json) {

    }

    @Override
    public void onGetFoundCrumbs(JSONObject json) {

    }

    @Override
    public void onUserLogin(JSONObject json) {

    }

    @Override
    public void onUserAdd(JSONObject json) {

    }

    @Override
    public void onGetUserLogbook(JSONObject json) {
        JSONArray data = null;
        try {
            data = json.getJSONArray(PAYLOAD_TAG);

            for (int i = 0; i < data.length(); i++) {
                JSONObject mJSONLog = data.getJSONObject(i);

                //String title = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_TITLE);
                //String lat = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_LATITUDE);
                //String lng = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_LONGITUDE);
                //int crumb_id = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_CRUMB_ID);
                //String date = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_CREATION_DATE);
                //String message = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_MESSAGE);
                // int creator_id = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_CREATOR_ID);
                //int ratings = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_RATINGS);
                //int total = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_TOTAL_DISCOVERED);
                //Float rating = Float.parseFloat(mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_RATING));

                String content = mJSONLog.getString(WebConstants.OnlineLogbookTableContact.LOGBOOK_CONTENT);

                HashMap<String,String> temp = new HashMap<>();
                temp.put("First", content);
                //temp.put("Second", "");
                //temp.put("Third", "");
                list.add(temp);

                ListViewAdapter adapter = new ListViewAdapter(getActivity(), list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddLogbookEntry(JSONObject json) {

    }

    @Override
    public void onFindCrumb(JSONObject json) {

    }

    @Override
    public void onGetCrumb(JSONObject json) {

    }

    @Override
    public void onAddCrumb(JSONObject json) {

    }

    @Override
    public android.support.v4.app.FragmentManager getMyFragmentManager() {
        return null;
    }

    @Override
    public void onRateCrumb(JSONObject json) {

    }
}
