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

import loris.pinna.channelmessaging.R;
import loris.pinna.channelmessaging.classes.Message;
import loris.pinna.channelmessaging.db.UserDataSource;

public class FriendsDialog extends DialogFragment {

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Ajouter en ami " + getArguments().getString("name") + "?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserDataSource dataSource = new UserDataSource(getContext());
                        dataSource.open();
                        dataSource.createUser(getArguments().getInt("id"), getArguments().getString("name"), getArguments().getString("url"));

                        FriendsAddedDialog d = new FriendsAddedDialog();
                        d.setArguments(getArguments());
                        d.show(getFragmentManager(), "friend-added");

                        dataSource.close();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}