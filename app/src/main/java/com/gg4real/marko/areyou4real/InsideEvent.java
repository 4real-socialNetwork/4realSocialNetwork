package com.gg4real.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.adapter.GlideApp;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.dialogs.DeleteEventWarning;
import com.gg4real.marko.areyou4real.dialogs.DeleteGroupWarning;
import com.gg4real.marko.areyou4real.dialogs.ShareEventDialog;
import com.gg4real.marko.areyou4real.dialogs.WarningDialog;
import com.gg4real.marko.areyou4real.fragments.HomeFragment;
import com.gg4real.marko.areyou4real.fragments.InsideEventMap;
import com.gg4real.marko.areyou4real.model.Event;
import com.gg4real.marko.areyou4real.model.EventRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nullable;

public class InsideEvent extends AppCompatActivity {
    private TextView tvEventName;
    private TextView tvEventActivity;
    private TextView tvEventTime;
    private TextView tvEventDate;
    private TextView tvEventDescription;
    private TextView tvEventPlayersNeeded;
    private Button tvEventPlayersEntered;
    private Button btnDoSomething;
    private Button btnSendEventRequest;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private String eventId;
    private String eventChatId;
    private CollectionReference eventsRef = db.collection("Events");
    private Intent intent;
    private Button fab;
    private String userId = FirebaseAuth.getInstance().getUid();
    private String btnText1 = "obriši event";
    private String btnText2 = "izađi iz eventa";
    private String btnText3 = "pridruži se eventu";
    private boolean isUserInEvent = false;
    private Button btnCompleteEvent;
    private String eventCreatorId = new String();
    private ImageView ivEventPlace;
    private double eventLat;
    private double eventLng;
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox4;
    private CheckBox mCheckBox5;
    private ArrayList<String> usersToBeInvited = new ArrayList<>();
    private ArrayList<String> duplicateCheck = new ArrayList<>();
    private ArrayList<String> namesOfUsersInvited = new ArrayList<>();
    private TinyDB tinyDB;
    private Context mContext;
    private long eventTimeInMili;
    private ArrayList<String> usersInEvent = new ArrayList<>();
    private ArrayList<String> positiveUsers = new ArrayList<>();
    private ArrayList<String> negativeUsers = new ArrayList<>();
    private TextView tvEventRange;
    private int range;
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private SimpleDateFormat sdfDate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
    private ImageView ivEventIcon;
    private TextView tvEventAdress;
    private TextView textView4;
    private Button btnChangeToPublic;
    private ArrayList<String> usersToBeRemoved = new ArrayList<>();
    private Event event;
    private TextView tvInvisible;
    private TextView tvAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_event);
        tvEventRange = findViewById(R.id.tvEventRange);
        loadData();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mContext = InsideEvent.this;

        tvInvisible = findViewById(R.id.tvInvisible);
        tvAdmin = findViewById(R.id.tvAdmin);
        tvEventAdress = findViewById(R.id.tvEventAdress);
        ivEventIcon = findViewById(R.id.ivEventPicture);
        fab = findViewById(R.id.btnChat);
        tvEventName = findViewById(R.id.tvEventName);
        tvEventActivity = findViewById(R.id.tvEventActivity);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        tvEventPlayersNeeded = findViewById(R.id.tvPlayersNeeded);
        tvEventPlayersEntered = findViewById(R.id.tvPlayersEntered);
        btnDoSomething = findViewById(R.id.btnDoSomething);
        btnCompleteEvent = findViewById(R.id.btnCompleteEvent);
        btnSendEventRequest = findViewById(R.id.btnSendEventRequest);
        ivEventPlace = findViewById(R.id.ivEventPlaceMap);
        textView4 = findViewById(R.id.textView4);
        btnChangeToPublic = findViewById(R.id.btnChangeToPublic);

        mCheckBox1 = findViewById(R.id.checkBox1);
        mCheckBox2 = findViewById(R.id.checkBox2);
        mCheckBox3 = findViewById(R.id.checkBox3);
        mCheckBox4 = findViewById(R.id.checkBox4);
        mCheckBox5 = findViewById(R.id.checkBox5);


        ivEventPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideEvent.this, InsideEventMap.class);
                intent.putExtra("event_lat", eventLat);
                intent.putExtra("event_lng", eventLng);
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InsideEvent.this, EventChatRoom.class);
                intent.putExtra("EVENTID", eventId);
                intent.putStringArrayListExtra("USERS_IN_CHAT", usersInEvent);
                startActivity(intent);
            }
        });

        btnSendEventRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareEventDialog dialog = new ShareEventDialog();
                dialog.show(getFragmentManager(), "ShareDialog");
            }
        });
        tvEventPlayersEntered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideEvent.this, UsersInEventActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideEvent.this, UsersInEventActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });


        btnChangeToPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarningDialog warningDialog = new WarningDialog();
                warningDialog.setCancelable(true);
                warningDialog.show(getFragmentManager(), "WarningDialog");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (eventCreatorId.equals(FirebaseAuth.getInstance().getUid())) {
            getMenuInflater().inflate(R.menu.event_menu, menu);

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.leaveGroup) {
            Intent intent = new Intent(InsideEvent.this, RemovePlayerActivity.class);
            intent.putExtra("EVENT_ID", eventId);
            startActivityForResult(intent, 4);
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

                    event = documentSnapshot.toObject(Event.class);
                    eventCreatorId = event.getIdOfTheUserWhoCreatedIt();
                    tvEventName.setText(event.getName());
                    tvEventActivity.setText(event.getActivity());
                    whatEvent(event.getActivity());
                    tvEventTime.setText(sdfTime.format(event.getEventStart()));
                    tvEventDate.setText(sdfDate.format(event.getEventStart()));
                    tvEventAdress.setText(event.getEventAdress());
                    tvEventDescription.setText(event.getEventDescription());
                    tvEventPlayersNeeded.setText("" + (event.getUsersNeeded()+1));
                    tvEventPlayersEntered.setText(""+(event.getUsersEntered()+1));
                    eventLat = event.getEventLat();
                    eventLng = event.getEventLng();
                    usersInEvent.clear();
                    usersInEvent = event.getListOfUsersParticipatingInEvent();


                    if (userId.equals(eventCreatorId)) {
                        if (event.isPrivate()) {
                            tvAdmin.setVisibility(View.VISIBLE);
                            btnChangeToPublic.setVisibility(View.VISIBLE);
                        }
                        tvAdmin.setVisibility(View.VISIBLE);
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

                } catch (Exception e1) {
                    Log.d("nULL", "onEvent: NULLPOINTER");
                }


            }


        });
    }


    private void loadData() {

        intent = getIntent();
        eventId = intent.getStringExtra("EVENT_ID");
        eventChatId = intent.getStringExtra("CHAT_ID");
        range = intent.getIntExtra("range", 0);
        tvEventRange.setText(range + " km");


        eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                try {
                    Event event = documentSnapshot.toObject(Event.class);
                    tvEventName.setText(event.getName());
                    tvEventActivity.setText(event.getActivity());
                    whatEvent(event.getActivity());
                    tvEventTime.setText(sdfTime.format(event.getEventStart()));
                    tvEventDescription.setText(event.getEventDescription());
                    tvEventPlayersNeeded.setText("" + (event.getUsersNeeded()+1));
                    tvEventPlayersEntered.setText(""+(event.getUsersEntered()+1));
                    eventLat = event.getEventLat();
                    eventLng = event.getEventLng();
                    eventTimeInMili = event.getEventStart();
                    usersInEvent.clear();
                    usersInEvent = event.getListOfUsersParticipatingInEvent();


                    if (event.getSkillNeeded() == 1) {
                        mCheckBox1.setChecked(true);
                        mCheckBox2.setChecked(false);
                        mCheckBox3.setChecked(false);
                        mCheckBox4.setChecked(false);
                        mCheckBox5.setChecked(false);


                    } else if (event.getSkillNeeded() == 2) {
                        mCheckBox1.setChecked(true);
                        mCheckBox2.setChecked(true);
                        mCheckBox3.setChecked(false);
                        mCheckBox4.setChecked(false);
                        mCheckBox5.setChecked(false);


                    } else if (event.getSkillNeeded() == 3) {
                        mCheckBox1.setChecked(true);
                        mCheckBox2.setChecked(true);
                        mCheckBox3.setChecked(true);
                        mCheckBox4.setChecked(false);
                        mCheckBox5.setChecked(false);

                    } else if (event.getSkillNeeded() == 4) {
                        mCheckBox1.setChecked(true);
                        mCheckBox2.setChecked(true);
                        mCheckBox3.setChecked(true);
                        mCheckBox4.setChecked(true);
                        mCheckBox5.setChecked(false);

                    } else if (event.getSkillNeeded() == 5) {
                        mCheckBox1.setChecked(true);
                        mCheckBox2.setChecked(true);
                        mCheckBox3.setChecked(true);
                        mCheckBox4.setChecked(true);
                        mCheckBox5.setChecked(true);
                    }

                    if (userId.equals(event.getIdOfTheUserWhoCreatedIt())) {
                        if (event.isPrivate()) {
                            tvAdmin.setVisibility(View.VISIBLE);
                            btnChangeToPublic.setVisibility(View.VISIBLE);
                        }
                        tvAdmin.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        btnDoSomething.setText(btnText1);
                        if (event.getUsersNeeded() - event.getUsersEntered() > 0) {
                            btnSendEventRequest.setVisibility(View.VISIBLE);
                            btnSendEventRequest.setClickable(true);
                        }

                        deleteEvent();


                    } else if (!(event.getListOfUsersParticipatingInEvent().contains(userId))) {
                        btnDoSomething.setText(btnText3);
                        if (event.getUsersEntered() >= event.getUsersNeeded()) {
                            btnDoSomething.setText("Event full");
                        } else {
                            joinEvent();

                        }
                    } else {
                        fab.setVisibility(View.VISIBLE);
                        btnDoSomething.setText(btnText2);
                        exitEvent();
                    }
                    checkTime(event);


                } catch (Exception e2) {
                    Log.d("exception", "onSuccess: " + e2.getLocalizedMessage());
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
                DeleteEventWarning warningDialog = new DeleteEventWarning();
                warningDialog.setCancelable(true);
                warningDialog.show(getFragmentManager(), "WarningDialog");

            }
        });
        loadData();
    }

    private void checkTime(final Event event) {

        if (eventCreatorId.equals(userId) && event.getEventStart() + 600000 < System.currentTimeMillis()) {
            if (usersInEvent.size() - 1 == 0) {
                btnDoSomething.setText("Obriši event");
                btnDoSomething.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventsRef.document(eventId).delete();
                        startActivity(new Intent(InsideEvent.this, MainActivity.class));
                    }
                });
            } else {
                btnSendEventRequest.setVisibility(View.INVISIBLE);
                btnDoSomething.setVisibility(View.INVISIBLE);
                btnDoSomething.setClickable(false);
                btnCompleteEvent.setVisibility(View.VISIBLE);
                btnCompleteEvent.setClickable(true);
                btnCompleteEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InsideEvent.this, CompleteEventActivity.class);
                        intent.putExtra("EVENT_ID", eventId);
                        startActivityForResult(intent, 3);

                    }
                });
            }

        } else if ((!eventCreatorId.equals(userId)) && event.getEventStart() + 18000000 < System.currentTimeMillis()) {
            tvInvisible.setVisibility(View.VISIBLE);
            btnDoSomething.setVisibility(View.INVISIBLE);
            btnDoSomething.setClickable(false);
        }
    }

    private void checkIfEventIsInEventRequests() {
        TinyDB tinyDB = new TinyDB(InsideEvent.this);
        final String userDocRef = tinyDB.getString("USERDOCREF");

        usersRef.document(tinyDB.getString("USERDOCREF")).collection("EventRequests")
                .whereEqualTo("eventId", eventId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    String requestDocRef = dc.getId();
                    usersRef.document(userDocRef).collection("EventRequests")
                            .document(requestDocRef).update("answered", true);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK && data != null) {
                usersToBeInvited = data.getStringArrayListExtra("users_in_group");
                namesOfUsersInvited = data.getStringArrayListExtra("group_names");
                Toast.makeText(InsideEvent.this, usersToBeInvited.get(0), Toast.LENGTH_SHORT).show();
                sendEventRequest();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK && data != null) {
                usersToBeInvited = data.getStringArrayListExtra("friends_selected");
                Toast.makeText(mContext, usersToBeInvited.get(0), Toast.LENGTH_SHORT).show();
                sendEventRequest();
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK && data != null) {

                positiveUsers = data.getStringArrayListExtra("positive_users");
                negativeUsers = data.getStringArrayListExtra("negative_users");
                finishEvent();
            }
        } else if (requestCode == 4) {
            if (resultCode == RESULT_OK && data != null) {
                usersToBeRemoved = data.getStringArrayListExtra("to_be_removed");
                for (String userId : usersToBeRemoved) {
                    removePlayer(userId);

                }

            }
        }


    }

    private void sendEventRequest() {
        tinyDB = new TinyDB(mContext);
        String userName = tinyDB.getString("USERNAME");
        String userDocRef = tinyDB.getString("USERDOCREF");
        for (String reciverId : usersToBeInvited) {
            if (!reciverId.equals(FirebaseAuth.getInstance().getUid())) {
                if (!duplicateCheck.contains(reciverId)) {
                    duplicateCheck.add(reciverId);
                    final EventRequest eventRequest = new EventRequest(eventId, "", tvEventActivity.getText().toString(), eventTimeInMili, userName, userDocRef, reciverId, false,System.currentTimeMillis()+"");
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
        Toast.makeText(mContext, "Uspjeh", Toast.LENGTH_SHORT).show();
    }

    private void finishEvent() {

        if (positiveUsers != null && positiveUsers.size() > 0) {
            for (String userId : positiveUsers) {
                usersRef.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        user.addPositiveReview();
                        double positive = user.getPositiveReview();
                        double entered = user.getNumberOfEventsParticipated();
                        double sum = (positive / entered) * 100;
                        user.setPercentage((int) sum);
                        usersRef.document(documentSnapshot.getId()).set(user);
                    }
                });
            }
        }
        if (negativeUsers != null && negativeUsers.size() > 0) {
            for (String userId : negativeUsers) {
                usersRef.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        user.addNegativeReview();
                        double positive = user.getPositiveReview();
                        double entered = user.getNumberOfEventsParticipated();
                        double sum = (positive / entered) * 100;
                        user.setPercentage((int) sum);
                        usersRef.document(documentSnapshot.getId()).set(user);
                    }
                });
            }

        }
        endEvent();

    }

    private void endEvent() {
        eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event event = documentSnapshot.toObject(Event.class);
                event.getListOfUsersParticipatingInEvent().clear();
                event.setCompleted(true);
                eventsRef.document(eventId).set(event);
                btnCompleteEvent.setVisibility(View.INVISIBLE);
                btnCompleteEvent.setClickable(false);
                Toast.makeText(mContext, "Event uspješno završen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void whatEvent(String activity) {
        switch (activity) {
            case "Nogomet":
                GlideApp.with(mContext).load(R.drawable.nogomet_in_event).circleCrop().into(ivEventIcon);
                break;
            case "Košarka":
                GlideApp.with(mContext).load(R.drawable.kosarka_inside_event).circleCrop().into(ivEventIcon);
                break;
            case "Šah":
                GlideApp.with(mContext).load(R.drawable.sah_inside_event).circleCrop().into(ivEventIcon);
                break;
            case "Društvene igre":
                GlideApp.with(mContext).load(R.drawable.drustvene_inside_event).circleCrop().into(ivEventIcon);
                break;
            case "Druženje":
                GlideApp.with(mContext).load(R.drawable.druzenja_inside_event).circleCrop().into(ivEventIcon);
                break;
            case "Ostalo":
                GlideApp.with(mContext).load(R.drawable.inside_event_ostalo).circleCrop().into(ivEventIcon);

        }

    }

    public String getEventId() {
        return eventId;
    }

    private void removePlayer(String userId) {
        event.removeUserFromEvent(userId);
        eventsRef.document(eventId).set(event);

    }
    public void removeEvent(){
        eventsRef.document(eventId).delete();
        Intent intent = new Intent(InsideEvent.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(InsideEvent.this, "Događaj izbrisan", Toast.LENGTH_SHORT).show();
    }
    public void makeBtnBeGone(){
        btnChangeToPublic.setVisibility(View.INVISIBLE);
    }


}









