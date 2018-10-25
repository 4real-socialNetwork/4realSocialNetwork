package com.example.marko.areyou4real;

import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marko.areyou4real.dialogs.InterestDialog;
import com.example.marko.areyou4real.fragments.TimePickerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private EditText name;
    private EditText userDescription;
    private ImageView profilePicture;
    private Button btnUserInterest;
    private Button btnSaveChanges;
    private ArrayList<String> updatedInterest = new ArrayList<>();
    private TextView tvInterests;
    private String interest = "Interesi su : ";
    private TextView tvTimeStart;
    private TextView tvTimeEnd;
    private int witchTimePicker = 0;
    private Button btnTimeStart;
    private Button btnTimeEnd;
    private ProgressBar progressBar;
    private String value = "";
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int interesiWasClicked = 0;
    private int pocetakEventaWasClickerd = 0;
    private int krajEventaWasClicked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);
        tvTimeStart = findViewById(R.id.tvTimeStart);
        tvInterests = findViewById(R.id.interestTextView);
        name = findViewById(R.id.etUserName);
        userDescription = findViewById(R.id.etUserDescription);
        profilePicture = findViewById(R.id.ivProfilePicture);
        btnUserInterest = findViewById(R.id.btnUserInterest);
        btnSaveChanges = findViewById(R.id.btnSaveUserChanges);
        btnTimeStart = findViewById(R.id.btnFromTime);
        btnTimeEnd = findViewById(R.id.btnEndTime);
        progressBar = findViewById(R.id.progressBar);
        updateUI();

        btnUserInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interesiWasClicked = 1;
                openDialog();
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDocumentRefId();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        btnTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pocetakEventaWasClickerd = 1;
                witchTimePicker = 0;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker");
            }
        });
        btnTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                krajEventaWasClicked = 1;
                witchTimePicker = 1;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker 2");
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


    public void updateUI() {
        usersRef.whereEqualTo("userId", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    User user = dc.toObject(User.class);
                    name.setText(user.getName());
                    userDescription.setText(user.getDescription());
                    tvTimeStart.setText("Eventi od: " + user.getTimeStartHour() + " : " + user.getTimestartMinute());
                    tvTimeEnd.setText("Eventi do: " + user.getTimeStopHour() + " : " + user.getTimeStopMinute());
                    for (int i = 0; i < user.getInterests().size(); i++) {
                        if (user.getInterests().get(i) != null) {
                            interest += user.getInterests().get(i) + ",";

                        }
                    }
                    tvInterests.setText(interest.toLowerCase());
                }
            }
        });

    }

    public void getDocumentRefId() {
        usersRef.whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot dc : queryDocumentSnapshots) {
                            value = dc.getId();

                        }
                        updateUserProfile();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openDialog() {
        InterestDialog dialog = new InterestDialog();
        dialog.show(getFragmentManager(), "ExampleDialog");
    }

    public void setItems(ArrayList<String> items) {
        updatedInterest.clear();
        String string = "";
        updatedInterest.addAll(items);
        for (int i = 0; i < updatedInterest.size(); i++) {
            string += "" + updatedInterest.get(i) + ",";
        }
        tvInterests.setText(string);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView timeStart = findViewById(R.id.tvTimeStart);
        TextView timeEnd = findViewById(R.id.tvTimeEnd);
        if (witchTimePicker == 0) {
            startHour = hourOfDay;
            startMinute = minute;
            timeStart.setText(hourOfDay + " : " + minute);
        } else if (witchTimePicker == 1) {
            endHour = hourOfDay;
            endMinute = minute;
            timeEnd.setText(hourOfDay + " : " + minute);
        }

    }

    private void updateUserProfile() {

        if (interesiWasClicked == 1 && pocetakEventaWasClickerd == 1 && krajEventaWasClicked == 1) {
            usersRef.document(value).update("name", name.getText().toString(), "description",
                    userDescription.getText().toString(), "interests", updatedInterest
                    , "timeStartHour", startHour, "timeStartMinute", startMinute
                    , "timeStopHour", endHour, "timeStopMinute", endMinute).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (interesiWasClicked == 0 && pocetakEventaWasClickerd == 1 && krajEventaWasClicked == 1) {
            usersRef.document(value).update("name", name.getText().toString(), "description"
                    , userDescription.getText().toString(), "timeStartHour", startHour, "timeStartMinute", startMinute
                    , "timeStopHour", endHour, "timeStopMinute", endMinute).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (interesiWasClicked == 0 && pocetakEventaWasClickerd == 0 && krajEventaWasClicked == 1) {
            usersRef.document(value).update("name", name.getText().toString(), "description", userDescription.getText().toString(),
                    "timeStopHour", endHour, "timeStopMinute", endMinute).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (interesiWasClicked == 0 && pocetakEventaWasClickerd == 0 && krajEventaWasClicked == 0) {
            usersRef.document(value).update("name", name.getText().toString(), "description", userDescription.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else if (interesiWasClicked == 1 && pocetakEventaWasClickerd == 0 && krajEventaWasClicked == 0) {
            usersRef.document(value).update("name", name.getText().toString(), "description", userDescription.getText().toString(),
                    "interests", updatedInterest).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (interesiWasClicked == 0 && pocetakEventaWasClickerd == 1 && krajEventaWasClicked == 0) {
            usersRef.document(value).update("name", name.getText().toString(), "description", userDescription.getText().toString()
                    , "timeStartHour", startHour, "timeStartMinute", startMinute).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (interesiWasClicked == 1 && pocetakEventaWasClickerd == 1 && krajEventaWasClicked == 0) {
            usersRef.document(value).update("name", name.getText().toString(), "description",
                    userDescription.getText().toString(), "interests", updatedInterest
                    , "timeStartHour", startHour, "timeStartMinute", startMinute
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}

