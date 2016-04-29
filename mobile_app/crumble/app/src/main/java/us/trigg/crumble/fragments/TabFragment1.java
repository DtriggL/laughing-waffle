package us.trigg.crumble.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import us.trigg.crumble.Crumb;
import us.trigg.crumble.ListViewAdapter;
import us.trigg.crumble.R;
import us.trigg.crumble.WebCom;
import us.trigg.crumble.WebConstants;
import us.trigg.crumble.interfaces.WebComHandler;



import static us.trigg.crumble.WebConstants.PAYLOAD_TAG;

public class TabFragment1 extends Fragment implements WebComHandler {

    //private SQLiteAdapter mySQLiteAdapter;
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private ListView listView;


    public TabFragment1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        View rootView = inflater.inflate(R.layout.fragment_pins, container, false);
        //listContent = (ListView) view.findViewById(R.id.list_myPins);
        // return view;

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), list);
        listView = (ListView) view.findViewById(R.id.list_myPins);
        listView.setAdapter(adapter);

        HashMap<String,String> temp=new HashMap<>();
        temp.put("First", "Title");
        temp.put("Second", "Views");
        //temp.put("Third", "Rating1");
        temp.put("Fourth", "Rating");
        list.add(temp);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.SharedPreferencesKey), Context.MODE_PRIVATE);

        int u_id = sharedPreferences.getInt("user_id", 0);


        WebComHandler wcHandler = this;
        WebCom wc = new WebCom(wcHandler, this.getContext());
        wc.getOwnedCrumbs(u_id);

        //Log.d("USER_ID", Integer.toString(u_id));

       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(getActivity(), Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }

        });
       */

        Log.d("LIST1", Integer.toString(list.size()));


        //ListView listView=(ListView)view.findViewById(R.id.list_myPins);



        // ListViewAdapter adapter=new ListViewAdapter(getActivity(), list);
        //listView.setAdapter(adapter);


        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //uncomment when contract class is fixed
        /**
         mySQLiteAdapter = new SQLiteAdapter(getActivity());
         mySQLiteAdapter.openToWrite();
         mySQLiteAdapter.deleteAll();


         mySQLiteAdapter.close();

         /*
         *  Open the same SQLite database
         *  and read all it's content.
         *//*
        mySQLiteAdapter = new SQLiteAdapter(getActivity());
        mySQLiteAdapter.openToRead();

        Cursor cursor = mySQLiteAdapter.queueAll();
        getActivity().startManagingCursor(cursor);

        String[] from = new String[]{SQLiteAdapter.KEY_CONTENT};
        int[] to = new int[]{R.id.text};

        SimpleCursorAdapter cursorAdapter =
                new SimpleCursorAdapter(getActivity(), R.layout.row, cursor, from, to);

        listContent.setAdapter(cursorAdapter);

        mySQLiteAdapter.close();
        **/

    }

    @Override
    public void onGetOwnedCrumbs(JSONObject json) {
        Log.d("Tab", "Check1");

        JSONArray data = null;
        try {
            data = json.getJSONArray(PAYLOAD_TAG);

            for (int i = 0; i < data.length(); i++) {
                JSONObject mJSONCrumb = data.getJSONObject(i);
                Crumb crumb = new Crumb();

                String title = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_TITLE);
                String lat = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_LATITUDE);
                String lng = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_LONGITUDE);
                int crumb_id = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_CRUMB_ID);
                //String date = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_CREATION_DATE);
                //String message = mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_MESSAGE);
                // int creator_id = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_CREATOR_ID);
                //int ratings = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_RATINGS);
                int total = mJSONCrumb.getInt(WebConstants.OnlineCrumbTableContact.COLUMN_TOTAL_DISCOVERED);
                Float rating = Float.parseFloat(mJSONCrumb.getString(WebConstants.OnlineCrumbTableContact.COLUMN_RATING));

                if(title != null)
                    crumb.setTitle(title);
                /*if(lat != null)
                    crumb.setLatitude(lat);
                if(lng != null)
                    crumb.setLongitude(lng);
                if(crumb_id >= 0)
                    crumb.setCrumbID(crumb_id);
                if(date != null)
                    crumb.setCreationDate(date);
                if(message != null)
                    crumb.setMessage(message);
                if(creator_id >= 0)
                    crumb.setCreatorID(creator_id);
                if(ratings >= 0.0)
                    crumb.setRatings(ratings);*/
                if(total >= 0)
                    crumb.setTotalDiscovered(total);
                if(rating >= 0.0)
                    crumb.setRating(rating);


                Log.d("LIST2", Integer.toString(list.size()));

                HashMap<String,String> temp2 = new HashMap<>();
                temp2.put("First", title);
                temp2.put("Second", Integer.toString(total));
                temp2.put("Fourth", Float.toString(rating));
                list.add(temp2);

                ListViewAdapter adapter = new ListViewAdapter(getActivity(), list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("LIST3", Integer.toString(list.size()));

        //list.notify();


        String name = json.optString("name");
        int profileIconId = json.optInt("profileIconId");
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

}