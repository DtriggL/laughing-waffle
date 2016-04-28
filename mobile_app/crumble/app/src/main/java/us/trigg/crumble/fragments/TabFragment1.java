package us.trigg.crumble.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import us.trigg.crumble.ListViewAdapter;
import us.trigg.crumble.R;

public class TabFragment1 extends Fragment {

    //private SQLiteAdapter mySQLiteAdapter;
    private ArrayList<HashMap<String, String>> list;

    ListView listContent;
    Random rand = new Random();


    public TabFragment1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        //listContent = (ListView) view.findViewById(R.id.list_myPins);
        // return view;

        ListView listView=(ListView)view.findViewById(R.id.list_myPins);

        list=new ArrayList<HashMap<String,String>>();

        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put("First", "Hotz Hall Scavenger Hunt 1");
        temp.put("Second", "Feeling hungry? Finding the first clue will be a treat!");
        temp.put("Third", "5");
        temp.put("Fourth", "4/25/16");
        list.add(temp);

        HashMap<String,String> temp2=new HashMap<String, String>();
        temp2.put("First", "Hotz Hall Scavenger Hunt 2");
        temp2.put("Second", "The next clue can be found\n in the box making a musical sound.");
        temp2.put("Third", "5");
        temp2.put("Fourth", "4/25/16");
        list.add(temp2);

        HashMap<String,String> temp3=new HashMap<String, String>();
        temp3.put("First", "Hotz Hall Scavenger Hunt 3");
        temp3.put("Second", "The more I dry, the wetter I become.\n" +
                            "Find me and you're almost done!");
        temp3.put("Third", "5");
        temp3.put("Fourth", "4/25/16");
        list.add(temp3);

        HashMap<String,String> temp4=new HashMap<String, String>();
        temp3.put("First", "The best fishing spot on Beaver Lake");
        temp3.put("Second", "This was a great spot to find a lot of Striped Bass!\n" +
                "You can always find a massive school here.");
        temp3.put("Third", "2");
        temp3.put("Fourth", "4/10/16");
        list.add(temp3);

        HashMap<String,String> temp5=new HashMap<String, String>();
        temp3.put("First", "Biker's Ultimate Challenge Course");
        temp3.put("Second", "Guaranteed biker's high. Ruts, drop-offs," +
                "narrow raodways!\n This trail has everything. Start at a\n" +
                "a beautiful lake and end up at the\n top of a mountian with" +
                "a fantastic view!");
        temp3.put("Third", "4.2");
        temp3.put("Fourth", "3/16/16");
        list.add(temp3);

        ListViewAdapter adapter=new ListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(getActivity(), Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }

        });

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