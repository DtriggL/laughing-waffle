package us.trigg.crumble.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import us.trigg.crumble.R;
import us.trigg.crumble.interfaces.MyFragmentDialogInterface;

/**
 * Created by trigglatour on 4/21/16.
 */
public class NoConnectionAlertFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_connection)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((MyFragmentDialogInterface)getActivity()).doPositiveClick();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
