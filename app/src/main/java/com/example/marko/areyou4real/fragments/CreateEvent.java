package com.example.marko.areyou4real.fragments;


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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener , TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "CreateEvent";

    private Context mContext = CreateEvent.this;

    private EditText name;
    private Spinner activity;
    private EditText lat;
    private EditText longitude;
    private EditText playersNeeded;
    private EditText eventDescription;
    private Button btnCreateEvent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("Events");
    private DocumentReference docRef;
    private String docId;
    private String userId = FirebaseAuth.getInstance().getUid();
    private String selectedInteres;
    private int startTimeHour;
    private int startTimeMinute;
    private Button btnSetTime;
    private TextView tvEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        System.out.println("onCreate Create Event.");
        setToolbar();
        tvEventTime = findViewById(R.id.tvTimeStart);
        name = findViewById(R.id.etName);
        activity = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity.setAdapter(spinnerAdapter);
        activity.setOnItemSelectedListener(this);
        btnSetTime = findViewById(R.id.btnTime);
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker");
            }
        });

        lat = findViewById(R.id.etLat);
        longitude = findViewById(R.id.etLong);
        playersNeeded = findViewById(R.id.etPlayersNeeded);
        eventDescription = findViewById(R.id.etEventDescription);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String ime = name.getText().toString();
        String aktivnost = selectedInteres;
        int kord1 = Integer.parseInt(lat.getText().toString());
        int kord2 = Integer.parseInt(longitude.getText().toString());
        int ljudiPotrebno = Integer.parseInt(playersNeeded.getText().toString());
        String opis = eventDescription.getText().toString();

        Event event = new Event(userId, ime, aktivnost, startTimeHour,startTimeMinute, kord2, kord1, ljudiPotrebno, opis);
        event.addCreatorUserToArray(userId);

        eventsRef.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CreateEvent.this, "Event created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
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

                }
            }
        });
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
    tvEventTime.setText(startTimeHour+" : "+startTimeMinute);
    }
}