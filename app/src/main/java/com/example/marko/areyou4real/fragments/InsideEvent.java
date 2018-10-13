package com.example.marko.areyou4real.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class InsideEvent extends AppCompatActivity {
    private TextView tvEventName;
    private TextView tvEventActivity;
    private TextView tvEventTime;
    private TextView tvEventDescription;
    private TextView tvEventPlace;
    private TextView tvEventPlayersNeeded;
    private TextView tvEventPlayersEntered;
    private Button btnDoSomething;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String eventId;
    private CollectionReference eventsRef = db.collection("Events");
    private Intent intent;
    private FloatingActionButton fab;
    private String userId = FirebaseAuth.getInstance().getUid();
    private String btnText1 = "delete event";
    private String btnText2 = "exit event";
    private String btnText3 = "join event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_event);
        loadData();

        fab = findViewById(R.id.fab);
        tvEventName = findViewById(R.id.tvEventName);
        tvEventActivity = findViewById(R.id.tvEventActivity);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        tvEventPlace = findViewById(R.id.tvEventPlace);
        tvEventPlayersNeeded = findViewById(R.id.tvPlayersNeeded);
        tvEventPlayersEntered = findViewById(R.id.tvPlayersEntered);
        btnDoSomething = findViewById(R.id.btnDoSomething);

    }

    @Override
    protected void onStart() {
        super.onStart();


        eventsRef.document(eventId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                try {
                    Event event = documentSnapshot.toObject(Event.class);
                    tvEventName.setText(event.getName());
                    tvEventActivity.setText(event.getActivity());
                    tvEventTime.setText("" + event.getTime());
                    tvEventDescription.setText(event.getEventDescription());
                    tvEventPlace.setText("Neko mjesto");
                    tvEventPlayersNeeded.setText("" + event.getUsersNeeded());
                    tvEventPlayersEntered.setText("" + event.getUsersEntered());

                    if (userId.equals(event.getIdOfTheUserWhoCreatedIt())) {
                        btnDoSomething.setText(btnText1);

                    } else if (!(event.getListOfUsersParticipatingInEvent().contains(userId))) {
                        btnDoSomething.setText(btnText3);

                    } else {
                        btnDoSomething.setText(btnText2);
                    }
                } catch (NullPointerException exception) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }


            }


        });
    }


    private void loadData() {

        intent = getIntent();
        eventId = intent.getStringExtra("EVENT_ID");

        eventsRef.whereEqualTo("eventId", eventId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot dc : task.getResult()) {
                        Event event = dc.toObject(Event.class);

                        tvEventName.setText(event.getName());
                        tvEventActivity.setText(event.getActivity());
                        tvEventTime.setText("" + event.getTime());
                        tvEventDescription.setText(event.getEventDescription());
                        tvEventPlace.setText("Neko mjesto");
                        tvEventPlayersNeeded.setText("" + event.getUsersNeeded());
                        tvEventPlayersEntered.setText("" + event.getUsersEntered());

                        if (userId.equals(event.getIdOfTheUserWhoCreatedIt())) {
                            btnDoSomething.setText(btnText1);
                            deleteEvent();

                        } else if (!(event.getListOfUsersParticipatingInEvent().contains(userId))) {
                            btnDoSomething.setText(btnText3);
                            joinEvent();
                        } else {
                            btnDoSomething.setText(btnText2);
                            exitEvent();
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsideEvent.this, e.toString(), Toast.LENGTH_SHORT).show();
            }


        });


    }


    public void joinEvent() {
        if (btnDoSomething.getText().toString().equals(btnText3)) {
            btnDoSomething.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventsRef.whereEqualTo("eventId", eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot dc : queryDocumentSnapshots) {
                                Event event = dc.toObject(Event.class);
                                if (event.getListOfUsersParticipatingInEvent().contains(userId)) {
                                    Toast.makeText(InsideEvent.this, "Allready in event", Toast.LENGTH_SHORT).show();
                                } else {
                                    event.addUsersToArray(userId);
                                    eventsRef.document(eventId).set(event);
                                    btnDoSomething.setText(btnText2);
                                    Toast.makeText(InsideEvent.this, "event joined", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });


                }
            });
        }
        loadData();

    }

    public void exitEvent() {
        if (btnDoSomething.getText().toString().equals(btnText2)) {
            btnDoSomething.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventsRef.document(eventId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Event event = task.getResult().toObject(Event.class);
                            if (event.getUsersEntered() <= 0) {
                                Toast.makeText(InsideEvent.this, "Error you cant exit at the moment, try again in a min", Toast.LENGTH_SHORT).show();
                            } else {
                                event.removeUserFromEvent(userId);
                                eventsRef.document(eventId).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(InsideEvent.this, "event exited", Toast.LENGTH_SHORT).show();
                                        btnDoSomething.setText(btnText3);
                                    }
                                });
                            }

                        }
                    });


                }
            });
        }
        loadData();
    }

    public void deleteEvent() {
        btnDoSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(InsideEvent.this, "event deleted", Toast.LENGTH_SHORT).show();
                eventsRef.document(eventId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });
        loadData();
    }
}








