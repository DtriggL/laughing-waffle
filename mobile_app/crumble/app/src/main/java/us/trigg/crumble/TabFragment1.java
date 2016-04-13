package us.trigg.crumble;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class TabFragment1 extends Fragment {

    //private SQLiteAdapter mySQLiteAdapter;
    ListView listContent;
    Random rand = new Random();


    public TabFragment1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        listContent = (ListView) view.findViewById(R.id.list_myPins);

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
}
