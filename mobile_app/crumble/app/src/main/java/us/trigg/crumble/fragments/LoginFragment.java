package us.trigg.crumble.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONObject;

import us.trigg.crumble.R;
import us.trigg.crumble.interfaces.WebComHandler;

/**
 * Created by ManojSeeram on 4/11/16.
 */
public class LoginFragment extends Fragment implements WebComHandler {
    //-----------------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------------
    // UI Elements
    private EditText usernameField;
    private EditText passwordField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        // Get the UI elements
        usernameField = (EditText) rootView.findViewById(R.id.username);
        passwordField = (EditText) rootView.findViewById(R.id.password);

        return rootView;

    }

    protected void onLoginClicked(View v) {
        String username = usernameField.getText().toString();
        String password = usernameField.getText().toString();
    }

    protected void onSignupClicked(View v) {
        // Start the signup fragment
    }

    //-----------------------------------------------------------------------------------
    // WebCom event handlers
    //-----------------------------------------------------------------------------------
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
}
