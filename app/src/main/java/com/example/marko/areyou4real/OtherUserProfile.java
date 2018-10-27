
package com.example.marko.areyou4real;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class OtherUserProfile extends AppCompatActivity {
    private TextView tvUserName;
    private TextView tvUserDescription;
    private TextView tvUserInterests;
    private ImageView ivUserProfilePicture;
    private Toolbar toolbar;
    private Intent intent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private String interests = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDescription = findViewById(R.id.tvUserDescription);
        tvUserInterests = findViewById(R.id.tvInterests);
        loadData();


    }

    private void loadData() {
        intent = getIntent();
        String otherUserId = intent.getStringExtra("userId");

        usersRef.whereEqualTo("userId", otherUserId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    User user = dc.toObject(User.class);
                    tvUserName.setText(user.getName());
                    tvUserDescription.setText(user.getDescription());
                    for (String value : user.getInterests()) {
                        if (value == user.getInterests().get(user.getInterests().size() - 1)) {
                            interests += value+".";
                        } else {
                            interests += value + ", ";

                        }
                    }
                    tvUserInterests.setText(interests);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtherUserProfile.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
