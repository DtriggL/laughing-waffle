package us.trigg.crumble.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import us.trigg.crumble.R;
import us.trigg.crumble.WebCom;
import us.trigg.crumble.interfaces.WebComHandler;

import static us.trigg.crumble.WebConstants.OnlineUserTableContact.COLUMN_USER_ID;
import static us.trigg.crumble.WebConstants.PAYLOAD_TAG;
import static us.trigg.crumble.WebConstants.STATUS_FOUND;
import static us.trigg.crumble.WebConstants.STATUS_TAG;

/**
 * Created by ManojSeeram on 4/11/16.
 */
public class LoginFragment extends Fragment implements WebComHandler {
    //-----------------------------------------------------------------------------------
    // Constants
    //-----------------------------------------------------------------------------------
    public static final String TAG = "Login Fragment";
    //-----------------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------------
    // Parent Activity
    Context parent;

    // UI Elements
    private EditText usernameField;
    private EditText passwordField;
    private Button login;
    private Button logout;

    private WebCom webCom;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DialogFragment alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        // Get the UI elements
        usernameField = (EditText) rootView.findViewById(R.id.username);
        passwordField = (EditText) rootView.findViewById(R.id.password);
        login = (Button) rootView.findViewById(R.id.login_button);
        logout = (Button) rootView.findViewById(R.id.logout_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked(v);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClicked(v);
            }
        });

        webCom = new WebCom(LoginFragment.this, this.getContext());
        sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.SharedPreferencesKey), Context.MODE_PRIVATE);

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = context;
    }

    public void onLoginClicked(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        webCom.userLogin(username, password);
    }

    public void onLogoutClicked(View v) {
        // Remove item from shared preferences
        editor = sharedPreferences.edit();
        editor.remove(getString(R.string.stored_user_id));
        editor.commit();
        Toast.makeText(getContext(), getString(R.string.logout_successful), Toast.LENGTH_LONG).show();
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
        try {
            String status = json.getString(STATUS_TAG);
            if (status.compareTo(STATUS_FOUND) == 0) {
                // We has a successful login, store the user_id
                // Get the user_id from the JSON object
                JSONObject user = json.getJSONObject(PAYLOAD_TAG);
                String user_id = user.getString(COLUMN_USER_ID);
                editor = sharedPreferences.edit();
                editor.putInt(getString(R.string.stored_user_id), Integer.parseInt(user_id));
                editor.commit();
                // Show login successful
                Bundle bundle = new Bundle();
                bundle.putString(NoConnectionAlertFragment.KEY_MESSAGE, getString(R.string.login_successful));
                bundle.putString(NoConnectionAlertFragment.KEY_POS_BUTTON_TEXT, getString(R.string.ok));
                Toast.makeText(getContext(), "User Logged In", Toast.LENGTH_LONG).show();

            } else {
                // Unsuccessful login, alert the user
                // Show login successful
                Bundle bundle = new Bundle();
                bundle.putString(NoConnectionAlertFragment.KEY_MESSAGE, getString(R.string.login_unsuccessful));
                bundle.putString(NoConnectionAlertFragment.KEY_POS_BUTTON_TEXT, getString(R.string.ok));
                Toast.makeText(getContext(), "User Not Logged In", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.v(TAG, "Unable to parse login response from server.");
        }
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
       return getFragmentManager();
    }
}
