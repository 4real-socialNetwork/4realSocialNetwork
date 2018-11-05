package com.example.marko.areyou4real.fragments;


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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.MapsActivity;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class CreateEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "CreateEvent";

    private Context mContext = CreateEvent.this;

    private EditText name;
    private Spinner activity;
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
    private Button btnMap;
    private TextView tvEventLocationAddress;
    private double eventLat;
    private double eventLng;
    private String eventAddress = "";
    //user location;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ArrayList<String> usersInEventChat = new ArrayList<>();
    public String mTextMessageId;



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
        String eventName = name.getText().toString();
        String activity = selectedInteres;
        int peopleNeeded = Integer.parseInt(playersNeeded.getText().toString());
        String description = eventDescription.getText().toString();

        Event event = new Event(userId, eventName, activity, startTimeHour, startTimeMinute, eventLat, eventLng, peopleNeeded, description,eventAddress);
        event.addCreatorUserToArray(userId);

        eventsRef.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CreateEvent.this, "Event created", Toast.LENGTH_SHORT).show();

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
                    createChat();


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
        tvEventTime.setText(startTimeHour + " : " + startTimeMinute);
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
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if(resultCode==RESULT_OK){
                eventAddress = data.getStringExtra("ADDRESS");
                tvEventLocationAddress.setText(eventAddress);
                eventLat = data.getDoubleExtra("LAT",0);
                eventLng = data.getDoubleExtra("LNG",0);
            }
        }
    }

    public void createChat(){
        usersInEventChat.add(FirebaseAuth.getInstance().getUid());
        eventsRef.document(docId).collection("chatRoom").add(new TextMessage("","","")).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mTextMessageId = documentReference.getId();
                eventsRef.document(docId).collection("chatRoom").document(mTextMessageId)
                        .update("eventChatId",mTextMessageId);
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);

            }
        });
    }

}
