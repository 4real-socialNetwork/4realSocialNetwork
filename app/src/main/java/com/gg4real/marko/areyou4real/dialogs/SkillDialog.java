package com.gg4real.marko.areyou4real.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.LoginCreateUser.CreateUser;
import com.gg4real.marko.areyou4real.R;
import com.gg4real.marko.areyou4real.UserProfile;

public class SkillDialog extends DialogFragment {

    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    CheckBox mCheckBox4;
    CheckBox mCheckBox5;
    CreateUser activity;
    UserProfile activity1;


    private int skill = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ocijenite svoj skill");
        builder.setCancelable(false);

        LayoutInflater li = getActivity().getLayoutInflater();
        View v = li.inflate(R.layout.skill_dialog, null);
        builder.setView(v);


        mCheckBox1 = v.findViewById(R.id.checkBox1);
        mCheckBox2 = v.findViewById(R.id.checkBox2);
        mCheckBox3 = v.findViewById(R.id.checkBox3);
        mCheckBox4 = v.findViewById(R.id.checkBox4);
        mCheckBox5 = v.findViewById(R.id.checkBox5);
        try{
            activity = (CreateUser) getActivity();

        }catch (Exception e){
            activity1 = (UserProfile) getActivity();
        }

        setUpCheckBoxes();



        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (skill > 0) {
                    switch (activity.getIntNumber()) {

                        case 1:
                            activity.setNogmetSkill(skill);
                            break;
                        case 2:
                            activity.setKosarkaSkill(skill);
                            break;
                        case 3:
                            activity.setSahSkill(skill);
                            break;

                    }
                } else {
                    Toast.makeText(activity, "Izaberite razinu", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }


    private void setUpCheckBoxes() {


        mCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(false);
                    mCheckBox3.setChecked(false);
                    skill = 1;

                } else {
                    mCheckBox1.setChecked(false);
                    mCheckBox2.setChecked(false);
                    mCheckBox3.setChecked(false);

                }
            }
        });
        mCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(false);
                    skill = 2;

                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(false);
                    mCheckBox3.setChecked(false);
                }

            }
        });
        mCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(true);
                    skill = 3;
                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(false);
                }


            }
        });
        mCheckBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(true);
                    mCheckBox4.setChecked(true);
                    skill = 4;
                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(true);
                    mCheckBox4.setChecked(false);
                }


            }
        });
        mCheckBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(true);
                    mCheckBox4.setChecked(true);
                    mCheckBox5.setChecked(true);
                    skill = 5;
                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(true);
                    mCheckBox4.setChecked(true);
                    mCheckBox5.setChecked(false);
                }


            }
        });

    }


}

