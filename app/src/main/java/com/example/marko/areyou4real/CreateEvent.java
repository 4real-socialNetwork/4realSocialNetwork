package com.example.marko.areyou4real;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.fragments.DatePickerFragment;
import com.example.marko.areyou4real.fragments.TimePickerFragment;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.EventRequest;
import com.example.marko.areyou4real.model.TextMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener
        , DatePickerDialog.OnDateSetListener {

    private static final String TAG = "CreateEvent";

    private Context mContext = CreateEvent.this;

    private EditText name;
    private Spinner activity;
    private Spinner eventTypeSpinner;
    private EditText playersNeeded;
    private EditText eventDescription;
    private Button btnCreateEvent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("Events");
    private CollectionReference usersRef = db.collection("Users");
    private DocumentReference docRef;
    private String docId;
    private String userId = FirebaseAuth.getInstance().getUid();
    private String selectedInteres;
    private int startTimeHour;
    private int startTimeMinute;
    private int yearOfEvent;
    private int monthOfEvent;
    private int dayOfMonthOfEvent;
    private Button btnSetTime;
    private TextView tvEventTime;
    private Button btnMap;
    private TextView tvEventLocationAddress;
    private TextView tvEventForUsers;
    private double eventLat;
    private double eventLng;
    private String eventAddress = "";
    //user location;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public String mTextMessageId;
    private Button btnEventDate;
    private TextView tvEventDate;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
    private boolean isPrivate = false;
    private ArrayList<String> groupNames = new ArrayList<>();
    private ArrayList<String> usersInGroup = new ArrayList<>();
    private TinyDB tinyDB;
    private ProgressBar progressBar;
    private ArrayList<String> duplicateCheck = new ArrayList<>();
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox4;
    private CheckBox mCheckBox5;
    private int skillRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        System.out.println("onCreate Create Event.");

        tvEventLocationAddress = findViewById(R.id.tvPlaceAdress);

        setToolbar();
        if (isServicesOkay() == true) {
            init();
        }
        tvEventTime = findViewById(R.id.tvTimeStart);
        name = findViewById(R.id.etName);
        activity = findViewById(R.id.spinner);
        eventTypeSpinner = findViewById(R.id.eventTypeSpinner);

        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity.setAdapter(spinnerAdapter);
        activity.setOnItemSelectedListener(this);

        setEventTypeSpinner();

        mCheckBox1 = findViewById(R.id.checkBox1);
        mCheckBox2 = findViewById(R.id.checkBox2);
        mCheckBox3 = findViewById(R.id.checkBox3);

        setUpCheckBoxes();

        tvEventForUsers = findViewById(R.id.tvEventForUsers);


        btnSetTime = findViewById(R.id.btnTime);
        btnEventDate = findViewById(R.id.btnDate);
        tvEventDate = findViewById(R.id.tvDateOfEvent);

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker");
            }
        });

        btnEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        playersNeeded = findViewById(R.id.etPlayersNeeded);
        eventDescription = findViewById(R.id.etEventDescription);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createEvent();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void createEvent() {

        if (!isPrivate) {
            String eventName = name.getText().toString();
            String activity = selectedInteres;
            int peopleNeeded = Integer.parseInt(playersNeeded.getText().toString());
            String description = eventDescription.getText().toString();
            if (eventName.length() > 0 && activity.length() > 0 && peopleNeeded > 0 && description.length() > 0 && calendar.getTimeInMillis() != 0) {
                Event event = new Event(userId, eventName, activity, skillRequired, calendar.getTimeInMillis(), eventLat, eventLng, peopleNeeded, description, eventAddress, false, isPrivate);
                event.addCreatorUserToArray(userId);

                eventsRef.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CreateEvent.this, "Javno", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateEvent.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            docRef = task.getResult();
                            docId = docRef.getId();
                            docRef.update("eventId", docId);
                            //createChat();


                        }
                    }
                });
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        } else {
            String eventName = name.getText().toString();
            String activity = selectedInteres;
            int peopleNeeded = Integer.parseInt(playersNeeded.getText().toString());
            String description = eventDescription.getText().toString();
            if (eventName.length() > 0 && activity.length() > 0 && peopleNeeded > 0 && description.length() > 0 && calendar.getTimeInMillis() != 0) {
                Event event = new Event(userId, eventName, activity, 0, calendar.getTimeInMillis(), eventLat, eventLng, peopleNeeded, description, eventAddress, false, isPrivate);
                event.addCreatorUserToArray(userId);

                eventsRef.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CreateEvent.this, "Private event created", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateEvent.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            docRef = task.getResult();
                            docId = docRef.getId();
                            docRef.update("eventId", docId);
                            //createChat();
                            sendEventRequest();


                        }
                    }
                });
            }

        }

    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedInteres = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        startTimeHour = hourOfDay;
        startTimeMinute = minute;
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        tvEventTime.setText(timeFormat.format(calendar.getTime()));
    }


    public boolean isServicesOkay() {
        int avalabile = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext);

        if (avalabile == ConnectionResult.SUCCESS) {
            //Everything is fine and the user can make map requests;
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(avalabile)) {
            //an error occured but we can resolve it;
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CreateEvent.this, avalabile, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "we cant fix it", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void init() {
        btnMap = findViewById(R.id.btnMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                eventAddress = data.getStringExtra("ADDRESS");
                tvEventLocationAddress.setText(eventAddress);
                eventLat = data.getDoubleExtra("LAT", 0);
                eventLng = data.getDoubleExtra("LNG", 0);
            }
        } else if (requestCode == 2 && data != null) {
            if (resultCode == RESULT_OK) {
                isPrivate = true;
                try {
                    usersInGroup = data.getStringArrayListExtra("users_in_group");
                    groupNames = data.getStringArrayListExtra("group_names");
                    tvEventForUsers.setText(groupNames.toString());
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tvEventDate.setText(dateFormat.format(calendar.getTime()));


    }

    private void setEventTypeSpinner() {
        ArrayAdapter<CharSequence> eventSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.eventType, android.R.layout.simple_spinner_item);
        eventSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(eventSpinnerAdapter);
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        isPrivate = false;
                        tvEventForUsers.setText(parent.getItemAtPosition(position).toString());
                        break;
                    }
                    case 1:
                        isPrivate = true;
                        Intent intent = new Intent(CreateEvent.this, MyGroupsSelector.class);
                        startActivityForResult(intent, 2);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void sendEventRequest() {
        tinyDB = new TinyDB(mContext);
        String userName = tinyDB.getString("USERNAME");
        String userDocRef = tinyDB.getString("USERDOCREF");
        if (usersInGroup.size() > 0) {
            for (String reciverId : usersInGroup) {
                if (!reciverId.equals(FirebaseAuth.getInstance().getUid())) {
                    if (!duplicateCheck.contains(reciverId)) {
                        duplicateCheck.add(reciverId);
                        final EventRequest eventRequest = new EventRequest(docId, "", selectedInteres, calendar.getTimeInMillis(), userName, userDocRef, reciverId, false);
                        usersRef.whereEqualTo("userId", reciverId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                                    final String reciverDocRef = dc.getReference().getId();
                                    usersRef.document(reciverDocRef).collection("EventRequests").add(eventRequest).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            documentReference.update("reciverDocRef", reciverDocRef, "eventRequestDocRef", documentReference.getId());
                                        }
                                    });

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }


            }
        }

        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(mContext, "Uspjeh", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
    }

    private void setUpCheckBoxes() {
        mCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(false);
                    mCheckBox3.setChecked(false);
                    skillRequired = 1;

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
                    skillRequired = 2;

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
                    skillRequired = 3;
                } else {
                    mCheckBox1.setChecked(true);
                    mCheckBox2.setChecked(true);
                    mCheckBox3.setChecked(false);
                }


            }
        });

    }


}
