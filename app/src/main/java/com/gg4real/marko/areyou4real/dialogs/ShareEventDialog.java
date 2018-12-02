package com.gg4real.marko.areyou4real.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.gg4real.marko.areyou4real.MyGroupsSelector;
import com.gg4real.marko.areyou4real.MyFriendsSelectorActivity;

public class ShareEventDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pozovite nekoga");
        builder.setMessage("Koga želite pozvati u događaj");
        builder.setCancelable(true);

        builder.setPositiveButton("Grupu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity().getApplicationContext(),MyGroupsSelector.class);
                getActivity().startActivityForResult(intent,1);
            }
        });
        builder.setNegativeButton("Pojedinca", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent((getActivity().getApplicationContext()),MyFriendsSelectorActivity.class);
                getActivity().startActivityForResult(intent,2);
            }
        });
        return builder.create();
    }
}
