package com.example.marko.areyou4real.LoginCreateUser;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.MyFirebaseMessagingService;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.dialogs.InterestDialog;
import com.example.marko.areyou4real.fragments.TimePickerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;

public class CreateUser extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "CreateUser";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = db.collection("Users");

    private Context mContext = CreateUser.this;
    private Button btnInteres;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText surname;
    private EditText description;
    private AppCompatSeekBar seekBar;
    private Button btnCreateAccount;
    private ProgressBar progressBar;
    private TextView showSeekBar;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private Button btnTimeFrom;
    private int startTimeHour;
    private int endTimeHour;
    private int startTimeMinutes;
    private int endTimeMinutes;
    private Button btnTimeTo;
    private TextView tvShowInterest;
    private TextView tvStartTimeDisplay;
    private TextView tvEndTimeDisplay;
    public TinyDB tinyDB ;
    String mToken = "";


    private int current_range = 5;
    //tag putem kojeg saznajemo koji timepicker se koristi :)
    private int witchTimePicker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setUpToolbar();
        btnInteres = findViewById(R.id.btnInteres);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        description = findViewById(R.id.etDescripiton);
        seekBar = findViewById(R.id.seekBar);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        progressBar = findViewById(R.id.progressBarUser);
        showSeekBar = findViewById(R.id.tvSeekBarShower);
        btnTimeFrom = findViewById(R.id.btnFromTime);
        btnTimeTo = findViewById(R.id.btnEndTime);
        tvShowInterest = findViewById(R.id.tvShowInterest);
        tvStartTimeDisplay = findViewById(R.id.tvStartTimeDisplay);
        tvEndTimeDisplay = findViewById(R.id.tvEndTimeDisplay);

        showSeekBar.setText(current_range + " km");
        seekBar.setProgress(current_range);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showSeekBar.setText(progress + " km");
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                witchTimePicker = 0;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker");
            }
        });

        btnTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                witchTimePicker = 1;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker 2");
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createUserAndAccount();
            }
        });
        btnInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    private void createUserAndAccount() {
        if (!validateForm()) {
            return;
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(CreateUser.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                 mToken = instanceIdResult.getToken();
            }
        });


        final String mail = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String ime = name.getText().toString().trim();
        final String prezime = surname.getText().toString().trim();
        final String opis = description.getText().toString().trim();
        final int udaljenost = seekBar.getProgress();


        if (mail.contentEquals("@") || pass.length() > 6) {
            mAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user = new User(FirebaseAuth.getInstance().getUid(),mToken, ime, prezime, mail, opis, selectedItems, udaljenost, startTimeHour, startTimeMinutes, endTimeHour, endTimeMinutes);
                            mUsersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    tinyDB = new TinyDB(CreateUser.this);
                                    tinyDB.putString("USERDOCREF",documentReference.getId());
                                    Toast.makeText(mContext, tinyDB.getString("USERDOCREF"), Toast.LENGTH_SHORT).show();
                                  //  Toast.makeText(CreateUser.this, "Račun kreiran", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateUser.this, "Već postoji korisnik s tim računom", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            Toast.makeText(this, "Email ili šifa nisu ispravni", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Obavezno ispuniti");
            result = false;
            btnCreateAccount.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            email.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            password.setError(null);
        }
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            name.setError(null);
        }
        if (TextUtils.isEmpty(surname.getText().toString())) {
            surname.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            surname.setError(null);
        }
        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            description.setError(null);
        }
        if (TextUtils.isEmpty(tvStartTimeDisplay.getText().toString())) {
            tvStartTimeDisplay.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            tvStartTimeDisplay.setError(null);
        }

        return result;
    }

    public void openDialog() {
        InterestDialog dialog = new InterestDialog();
        dialog.show(getFragmentManager(), "ExampleDialog");
    }

    public void setItems(ArrayList<String> items) {
        selectedItems.clear();
        String string = "";
        selectedItems.addAll(items);
        for (int i = 0; i < selectedItems.size(); i++) {
            string += "" + selectedItems.get(i) + ",";
        }
        tvShowInterest.setText(string);

    }

    private void setUpToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView timeStart = findViewById(R.id.tvStartTimeDisplay);
        TextView timeEnd = findViewById(R.id.tvEndTimeDisplay);
        if (witchTimePicker == 0) {
            startTimeHour = hourOfDay;
            startTimeMinutes = minute;
            timeStart.setText(startTimeHour + " : " + startTimeMinutes);
        } else if (witchTimePicker == 1) {
            endTimeHour = hourOfDay;
            endTimeMinutes = minute;
            timeEnd.setText(endTimeHour + " : " + endTimeMinutes);
        }


    }



}
