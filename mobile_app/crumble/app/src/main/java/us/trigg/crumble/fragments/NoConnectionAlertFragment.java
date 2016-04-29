package us.trigg.crumble.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import us.trigg.crumble.MainActivity;
import us.trigg.crumble.R;
import us.trigg.crumble.interfaces.MyFragmentDialogInterface;

/**
 * Created by trigglatour on 4/21/16.
 */
public class NoConnectionAlertFragment extends DialogFragment {
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_POS_BUTTON_TEXT = "positive_button_text";
    public static final String KEY_NEG_BUTTON_TEXT = "negative_button_text";

    private String message;
    private String negativeButtonText;
    private String positiveButtonText;
    private MyFragmentDialogInterface callback;

    public NoConnectionAlertFragment() {
        message = getString(R.string.no_connection);
        positiveButtonText = getString(R.string.cancel);
        negativeButtonText = getString(R.string.cancel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (MyFragmentDialogInterface) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement NoConnectionAlertFragment");
        }
    }

    public static NoConnectionAlertFragment newInstance() {
        NoConnectionAlertFragment frag = new NoConnectionAlertFragment();
        return frag;
    }

    @Override
    public void setArguments(Bundle arguments) {
        String string = arguments.getString(KEY_MESSAGE);
        if (string != null) {
            message = string;
        }
        string = arguments.getString(KEY_NEG_BUTTON_TEXT);
        if (string != null) {
            negativeButtonText = string;
        }
        string = arguments.getString(KEY_POS_BUTTON_TEXT);
        if (string != null) {
            positiveButtonText = string;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MyFragmentDialogInterface)getContext()).doPositiveClick();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
