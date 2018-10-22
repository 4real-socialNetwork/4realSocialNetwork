package com.example.marko.areyou4real.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.marko.areyou4real.CreateUser;
import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;

import java.util.ArrayList;
import java.util.List;

public class InterestDialog extends DialogFragment {
    private ArrayList<String> mSelectedItems;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Odaberite Vaše interese");
        builder.setMultiChoiceItems(R.array.interests, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] items = getActivity().getResources().getStringArray(R.array.interests);
                if (isChecked) {
                    mSelectedItems.add(items[which]);
                } else if (mSelectedItems.contains(items[which])) {
                    mSelectedItems.remove(items[which]);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getInterestsInActivity(mSelectedItems);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Poništi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private void getInterestsInActivity(ArrayList<String> value) {
        CreateUser activity = (CreateUser) getActivity();
        activity.setItems(value);

    }
}
