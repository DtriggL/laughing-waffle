package us.trigg.crumble;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Josh on 4/24/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    public ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.pins_column_row, parent, false);

            txtFirst=(TextView) convertView.findViewById(R.id.crumb_title);
            txtSecond=(TextView) convertView.findViewById(R.id.crumb_message);
            txtThird=(TextView) convertView.findViewById(R.id.crumb_rating);

        }

        HashMap<String, String> map=list.get(position);
        if(position==0){
            if(map.get("First") != null) {
                SpannableString spanString1 = new SpannableString(map.get("First"));
                spanString1.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString1.length(), 0);
                txtFirst.setText(spanString1);
            }

            if(map.get("Second") != null) {
                SpannableString spanString2 = new SpannableString(map.get("Second"));
                spanString2.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString2.length(), 0);
                txtSecond.setText(spanString2);
            }
            if(map.get("Third") != null) {
                SpannableString spanString4 = new SpannableString(map.get("Third"));
                spanString4.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString4.length(), 0);
                txtThird.setText(spanString4);
            }
        }
        else {
            txtFirst.setText(map.get("First"));
            txtSecond.setText(map.get("Second"));
            txtThird.setText(map.get("Third"));
        }
        return convertView;
    }
}

