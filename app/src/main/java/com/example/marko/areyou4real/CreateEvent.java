package com.example.marko.areyou4real;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marko.areyou4real.fragments.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEvent extends AppCompatActivity {
    private EditText name;
    private EditText activity;
    private EditText lat;
    private EditText longitude;
    private EditText time;
    private EditText playersNeeded;
    private EditText eventDescription;
    private Button btnCreateEvent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("Events");
    private DocumentReference docRef;
    private String docId;
    private String userId = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        name = findViewById(R.id.etName);
        activity = findViewById(R.id.etActivity);
        lat = findViewById(R.id.etLat);
        longitude = findViewById(R.id.etLong);
        time = findViewById(R.id.etTime);
        playersNeeded = findViewById(R.id.etPlayersNeeded);
        eventDescription = findViewById(R.id.etEventDescription);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalender.set(Calendar.MINUTE, minute);

                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });
    }

    public void createEvent() {
        String ime = name.getText().toString();
        String aktivnost = activity.getText().toString();
        int kord1 = Integer.parseInt(lat.getText().toString());
        int kord2 = Integer.parseInt(longitude.getText().toString());
        double vrijeme = Double.parseDouble(time.getText().toString());
        int ljudiPotrebno = Integer.parseInt(playersNeeded.getText().toString());
        String opis = eventDescription.getText().toString();

        Event event = new Event(userId, ime, aktivnost, vrijeme, kord2, kord1, ljudiPotrebno, opis);
        event.addCreatorUserToArray(userId);

        eventsRef.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CreateEvent.this, "Event created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

}
