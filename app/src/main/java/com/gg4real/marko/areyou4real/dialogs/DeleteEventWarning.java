package com.gg4real.marko.areyou4real.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.gg4real.marko.areyou4real.InsideEvent;
import com.gg4real.marko.areyou4real.InsideGroup;

public class DeleteEventWarning extends DialogFragment {
    private InsideEvent activity = new InsideEvent();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setTitle("Brisanje događaja");
        builder.setMessage("Jeste li sigurni?");
        builder.setCancelable(false);
        activity = (InsideEvent) getActivity();
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.removeEvent();
                getActivity().finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
