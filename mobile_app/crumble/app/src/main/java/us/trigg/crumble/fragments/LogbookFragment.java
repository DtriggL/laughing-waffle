package us.trigg.crumble.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import us.trigg.crumble.R;

/**
 * Created by ManojSeeram on 4/11/16.
 */
public class LogbookFragment extends Fragment {

    ArrayList logEntries;
    ArrayAdapter logEntriesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logbook, container, false);

        logEntries = new ArrayList<String>();
        logEntriesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                logEntries);
        ListView logBookListView = (ListView) rootView.findViewById(R.id.logbookListView);
        logBookListView.setAdapter(logEntriesAdapter);

        logEntries.add("2/16/16 Discovered: The best fishing spot on Beaver Lake\nuser: tdlatour");
        logEntries.add("3/02/16 Discovered: hiking must see @ sunset\nuser: jcl006");
        logEntries.add("3/03/16 Discovered: hiking must see @ sunrise\nuser: jcl006");
        logEntries.add("3/10/16 Discovered: 11 things you never expected to find in Central Park\nuser: mseeram");

        logEntriesAdapter.notifyDataSetChanged();

        return rootView;

    }

}
