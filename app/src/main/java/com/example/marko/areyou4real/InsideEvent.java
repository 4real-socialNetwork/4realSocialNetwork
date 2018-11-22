package com.example.marko.areyou4real;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.EventRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    private Button btnSendEventRequest;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private String eventId;
    private String eventChatId;
    private CollectionReference eventsRef = db.collection("Events");
    private Intent intent;
    private FloatingActionButton fab;
    private String userId = FirebaseAuth.getInstance().getUid();
    private String btnText1 = "delete event";
    private String btnText2 = "exit event";
    private String btnText3 = "join event";
    private boolean isUserInEvent = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault());
    private Button btnCompleteEvent;
    private String eventCreatorId = new String();
    private ImageView ivEventPlace;
    private double eventLat;
    private double eventLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_event);
        loadData();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = findViewById(R.id.fab);
        tvEventName = findViewById(R.id.tvEventName);
        tvEventActivity = findViewById(R.id.tvEventActivity);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        tvEventPlace = findViewById(R.id.tvEventPlace);
        tvEventPlayersNeeded = findViewById(R.id.tvPlayersNeeded);
        tvEventPlayersEntered = findViewById(R.id.tvPlayersEntered);
        btnDoSomething = findViewById(R.id.btnDoSomething);
        btnCompleteEvent = findViewById(R.id.btnCompleteEvent);
        btnSendEventRequest = findViewById(R.id.btnSendEventRequest);
        ivEventPlace = findViewById(R.id.ivEventPlaceMap);

        ivEventPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideEvent.this,MapsActivity.class);
                Toast.makeText(InsideEvent.this, "soon", Toast.LENGTH_SHORT).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InsideEvent.this, EventChatRoom.class);
                intent.putExtra("EVENTID", eventId);
                startActivity(intent);
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
                    eventCreatorId = event.getIdOfTheUserWhoCreatedIt();
                    tvEventName.setText(event.getName());
                    tvEventActivity.setText(event.getActivity());
                    tvEventTime.setText(sdf.format(event.getEventStart()));
                    tvEventDescription.setText(event.getEventDescription());
                    tvEventPlace.setText(event.getEventAdress());
                    tvEventPlayersNeeded.setText("" + event.getUsersNeeded());
                    tvEventPlayersEntered.setText("" + event.getUsersEntered());
                    eventLat = event.getEventLat();
                    eventLng = event.getEventLng();

                    if (userId.equals(event.getIdOfTheUserWhoCreatedIt())) {
                        btnDoSomething.setText(btnText1);
                        fab.setVisibility(View.VISIBLE);
                        fab.setClickable(true);

                    } else if (!(event.getListOfUsersParticipatingInEvent().contains(userId))) {
                        btnDoSomething.setText(btnText3);
                        fab.setVisibility(View.INVISIBLE);
                        fab.setClickable(false);

                    } else {
                        btnDoSomething.setText(btnText2);
                        fab.setVisibility(View.VISIBLE);
                        fab.setClickable(true);
                    }
                    checkTime(event);
                } catch (NullPointerException exception) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(InsideEvent.this, "yes", Toast.LENGTH_SHORT).show();
                }


            }


        });
    }


    private void loadData() {

        intent = getIntent();
        eventId = intent.getStringExtra("EVENT_ID");
        eventChatId = intent.getStringExtra("CHAT_ID");

        eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    Event event = documentSnapshot.toObject(Event.class);
                    tvEventName.setText(event.getName());
                    tvEventActivity.setText(event.getActivity());
                    tvEventTime.setText(sdf.format(event.getEventStart()));
                    tvEventDescription.setText(event.getEventDescription());
                    tvEventPlace.setText(event.getEventAdress());
                    tvEventPlayersNeeded.setText("" + event.getUsersNeeded());
                    tvEventPlayersEntered.setText("" + event.getUsersEntered());
                    eventLat = event.getEventLat();
                    eventLng = event.getEventLng();

                    if (userId.equals(event.getIdOfTheUserWhoCreatedIt())) {
                        btnDoSomething.setText(btnText1);
                        btnSendEventRequest.setVisibility(View.VISIBLE);
                        deleteEvent();


                    } else if (!(event.getListOfUsersParticipatingInEvent().contains(userId))) {
                        btnDoSomething.setText(btnText3);
                        if (event.getUsersEntered() >= event.getUsersNeeded()) {
                            btnDoSomething.setText("Event full");
                        } else {
                            joinEvent();

                        }
                    } else {
                        btnDoSomething.setText(btnText2);
                        exitEvent();
                    }

                } catch (NullPointerException e) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(InsideEvent.this, "yes", Toast.LENGTH_SHORT).show();
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
                    eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Event event = documentSnapshot.toObject(Event.class);
                            if (event.getListOfUsersParticipatingInEvent().contains(userId)) {
                                Toast.makeText(InsideEvent.this, "Allready in event", Toast.LENGTH_SHORT).show();
                            } else {
                                event.addUsersToArray(userId);
                                eventsRef.document(eventId).set(event);
                                btnDoSomething.setText(btnText2);
                                Toast.makeText(InsideEvent.this, "event joined", Toast.LENGTH_SHORT).show();
                                fab.setVisibility(View.VISIBLE);
                                fab.setClickable(true);
                                checkIfEventIsInEventRequests();
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
                                        fab.setVisibility(View.INVISIBLE);
                                        fab.setClickable(false);
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
                Intent intent = new Intent(InsideEvent.this, MainActivity.class);
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

    private void checkTime(final Event event) {

        if (eventCreatorId.equals(userId)&&event.getEventStart()+18000000 <  Calendar.getInstance().getTimeInMillis()){
            btnCompleteEvent.setVisibility(View.VISIBLE);
            btnCompleteEvent.setClickable(true);
            btnCompleteEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventsRef.document(eventId).update("isCompleted",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InsideEvent.this, "Event završen", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    private void checkIfEventIsInEventRequests(){
        TinyDB tinyDB = new TinyDB(InsideEvent.this);
        final String userDocRef = tinyDB.getString("USERDOCREF");

        usersRef.document(tinyDB.getString("USERDOCREF")).collection("EventRequests")
                .whereEqualTo("eventId",eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots){
                    String requestDocRef = dc.getId();
                    usersRef.document(userDocRef).collection("EventRequests")
                            .document(requestDocRef).update("answered",true);

                }
            }
        });
    }
}








