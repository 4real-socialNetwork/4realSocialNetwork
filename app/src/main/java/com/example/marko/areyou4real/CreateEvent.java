package com.example.marko.areyou4real;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private ArrayList<String> peopleInEvent = new ArrayList<>();

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

    }

    public void createEvent() {
        String ime = name.getText().toString();
        String aktivnost = activity.getText().toString();
        int kord1 = Integer.parseInt(lat.getText().toString());
        int kord2 = Integer.parseInt(longitude.getText().toString());
        int vrijeme = Integer.parseInt(time.getText().toString());
        int ljudiPotrebno = Integer.parseInt(playersNeeded.getText().toString());
        String opis = eventDescription.getText().toString();

        peopleInEvent.add(FirebaseAuth.getInstance().getUid());
        Event event = new Event(userId, ime, aktivnost, kord1, kord2, vrijeme, ljudiPotrebno, opis);
        event.setUsersEntered(1);
        event.setListOfUsersParticipatingInEvent(peopleInEvent);
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
