package us.trigg.crumble.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Random;

import us.trigg.crumble.R;

public class TabFragment2 extends Fragment {

    //private SQLiteAdapter mySQLiteAdapter;
    ListView listContent;
    Random rand = new Random();


    public TabFragment2(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        listContent = (ListView) view.findViewById(R.id.list_myPins);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //uncomment when contract class is fixed
    /*
        mySQLiteAdapter = new SQLiteAdapter(getActivity());
        mySQLiteAdapter.openToWrite();
        mySQLiteAdapter.deleteAll();

        for(int i = 0; i < rand.nextInt(50)+10; ++i){
            mySQLiteAdapter.insert(new Integer(rand.nextInt(1000)).toString());
        }


        mySQLiteAdapter.close();


        Open the same SQLite database
        and read all it's content.

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
        */
    }
}