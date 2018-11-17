
package com.example.marko.areyou4real;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.GlideApp;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.FriendRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    private Button btnAddPlayer;
    private DocumentReference thisUserDocRef;
    private User otherUser;
    private String currentUserName ="";
    private TinyDB tinyDB ;
    String currentUserDocId = "";
    private DocumentReference currentUserDocRef ;


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
        btnAddPlayer = findViewById(R.id.btnAddPlayer);
        ivUserProfilePicture = findViewById(R.id.ivProfilePicture);
        GlideApp.with(OtherUserProfile.this).load(R.drawable.avatar).circleCrop().into(ivUserProfilePicture);
        loadData();
        getCurrentUserName();


        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddPlayer.setText("zahtjev poslan");
                sendPlayerRequest();

            }
        });


    }

    private void loadData() {
        intent = getIntent();
        String otherUserId = intent.getStringExtra("userId");

        usersRef.whereEqualTo("userId", otherUserId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    otherUser = dc.toObject(User.class);
                     thisUserDocRef = dc.getReference();
                    tvUserName.setText(otherUser.getName());
                    tvUserDescription.setText(otherUser.getDescription());
                    for (String value : otherUser.getInterests()) {
                        if (value == otherUser.getInterests().get(otherUser.getInterests().size() - 1)) {
                            interests += value+".";
                        } else {
                            interests += value + ", ";

                        }
                    }
                    tvUserInterests.setText(interests);

                }
                checkIfFriendRequestIsSent();
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
    private void sendPlayerRequest(){
        String userFriendGroupId = new TinyDB(OtherUserProfile.this).getString("FRIENDSGROUPID");
        thisUserDocRef.collection("FriendRequest").add(new FriendRequest(FirebaseAuth.getInstance().getUid(),false,currentUserName,currentUserDocRef,"",userFriendGroupId))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(OtherUserProfile.this, "Zahtjev poslan", Toast.LENGTH_SHORT).show();
                        documentReference.update("friendRequestRef",documentReference.getId());
                    }
                });
    }

    private void checkIfFriendRequestIsSent(){
        thisUserDocRef.collection("FriendRequest").whereEqualTo("senderId",FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots){
                    FriendRequest fr = dc.toObject(FriendRequest.class);

                    if (fr.getSenderId().equals(FirebaseAuth.getInstance().getUid())&&(fr.isAccepted())==false){
                        btnAddPlayer.setText("Zahtjev poslan");
                        btnAddPlayer.setClickable(false);
                        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btnAddPlayer.setText("Dodaj prijatelja");
                            }
                        });
                    }else if (fr.getSenderId().equals(FirebaseAuth.getInstance().getUid())&&(fr.isAccepted())!=false){
                        btnAddPlayer.setText("Prijatelji ste");
                        btnAddPlayer.setClickable(false);
                    }else{
                        btnAddPlayer.setText("Dodajte prijatelja");
                        btnAddPlayer.setClickable(true);
                    }

                }
            }
        });
    }
    private void getCurrentUserName(){
        TinyDB tinyDB = new TinyDB(OtherUserProfile.this);
        currentUserDocId = tinyDB.getString("USERDOCREF");
        FirebaseFirestore.getInstance().collection("Users").document(currentUserDocId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         currentUserName = task.getResult().toObject(User.class).getName();
                        currentUserDocRef = task.getResult().getReference();
                    }
                });
    }
}
