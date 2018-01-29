/*
 * Copyright Loris Pinna
 * lorispinna.com =)
 */

package loris.pinna.channelmessaging.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import loris.pinna.channelmessaging.db.UserDataSource;

public class FriendsAddedDialog extends DialogFragment {

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("" + getArguments().getString("name") + " a été ajouté en ami");
        // Create the AlertDialog object and return it
        return builder.create();
    }
}