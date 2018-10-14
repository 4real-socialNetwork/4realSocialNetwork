package com.example.marko.areyou4real;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private EditText name;
    private EditText userDescription;
    private ImageView profilePicture;
    private EditText userInterest;
    private Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.etUserName);
        userDescription = findViewById(R.id.etUserDescription);
        profilePicture = findViewById(R.id.ivProfilePicture);
        userInterest = findViewById(R.id.etUserInterest);
        btnSaveChanges = findViewById(R.id.btnSaveUserChanges);
        updateUI();

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void updateUI() {
        usersRef.whereEqualTo("userId",userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    User user = dc.toObject(User.class);
                    name.setText(user.getName());
                    userInterest.setText(user.getInterest());
                    userDescription.setText(user.getDescription());
                }
            }
        });

    }

    public void saveChanges() {
        usersRef.whereEqualTo("userId",userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots){
                    String document = dc.getId();
                    dc.getDocumentReference(document).update("description",userDescription.getText().toString(),"name",
                            name.getText().toString(),"interest",userInterest.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserProfile.this, "Changes saved", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}

