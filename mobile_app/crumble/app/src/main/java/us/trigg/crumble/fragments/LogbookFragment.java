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

        logEntries.add("4/25/16 \nDropped: Hotz Hall Scavenger Hunt 1\nuser: meghan03");
        logEntries.add("4/25/16 \nDropped: Hotz Hall Scavenger Hunt 2\nuser: meghan03");
        logEntries.add("4/25/16 \nDropped: Hotz Hall Scavenger Hunt 3\nuser: meghan03");
        logEntries.add("4/10/16 \nDropped: The best fishing spot on Beaver Lake\nuser: meghan03");
        logEntries.add("3/29/16 \nDiscovered: hiking must see @ sunset\nuser: jcl006");
        logEntries.add("3/16/16 \nDropped: Biker's Ultimate Challenge Course \nuser: meghan03");
        logEntries.add("3/03/16 \nDiscovered: hiking must see @ sunrise\nuser: jcl006");
        logEntries.add("2/10/16 \nDiscovered: 11 things you never expected to find in Central Park\nuser: mseeram");

        logEntriesAdapter.notifyDataSetChanged();

        return rootView;

    }

}
