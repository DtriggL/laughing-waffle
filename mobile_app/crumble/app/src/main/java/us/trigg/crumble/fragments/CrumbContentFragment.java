package us.trigg.crumble.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import us.trigg.crumble.Crumb;
import us.trigg.crumble.R;

/**
 * Created by ManojSeeram on 4/11/16.
 */
public class CrumbContentFragment extends Fragment {

    Crumb toCrumb;
    Button doneButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crumb_content, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.pin_title);
        TextView content = (TextView) rootView.findViewById(R.id.pin_content);

        title.setText(toCrumb.getTitle());
        content.setText(toCrumb.getMessage());

        Log.d("CCF", "made it. why didn't fragment activity start?");

        return rootView;
    }

    public void settoCrumb(Crumb crumb) {
        toCrumb = crumb;
    }

    //closes info window and goes back to map
    public void endFragment() {
        getActivity().onBackPressed();
    }
}
