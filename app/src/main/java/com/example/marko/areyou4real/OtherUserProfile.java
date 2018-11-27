
package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.GlideApp;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.EventRequest;
import com.example.marko.areyou4real.model.FriendRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

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
    private Button btnDeclinePlayer;
    private String thisUserDocRef;
    private User otherUser;
    private String currentUserName = "";
    private TinyDB tinyDB;
    String currentUserDocId = "";
    private String otherUserDocRef = "";
    private String friendRequestDocRef = "";
    private Context mContext;
    private String currentUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        toolbar = findViewById(R.id.toolbar);
        mContext = OtherUserProfile.this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserDescription = findViewById(R.id.tvUserDescription);
        tvUserInterests = findViewById(R.id.tvInterests);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);
        btnDeclinePlayer = findViewById(R.id.btnDeclinePlayer);
        ivUserProfilePicture = findViewById(R.id.ivProfilePicture);
        GlideApp.with(OtherUserProfile.this).load(R.drawable.avatar).circleCrop().into(ivUserProfilePicture);
        loadData();
        getCurrentUserName();


        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddPlayer.setText("zahtjev poslan");
                btnAddPlayer.setClickable(false);
                sendPlayerRequest();

            }
        });


    }

    private void loadData() {
        intent = getIntent();
        otherUserDocRef = intent.getStringExtra("otherUserDocRef");

        usersRef.document(otherUserDocRef).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                otherUser = documentSnapshot.toObject(User.class);
                thisUserDocRef = documentSnapshot.getId();
                currentUserId = documentSnapshot.toObject(User.class).getUserId();
                if (otherUser.getProfilePictureUrl() != null) {
                    GlideApp.with(OtherUserProfile.this).load(otherUser.getProfilePictureUrl())
                            .circleCrop()
                            .placeholder(R.drawable.avatar)
                            .into(ivUserProfilePicture);
                } else {
                    GlideApp.with(OtherUserProfile.this).load(R.drawable.avatar).circleCrop().into(ivUserProfilePicture);

                }


                tvUserName.setText(otherUser.getName());
                tvUserDescription.setText(otherUser.getDescription());
                for (String value : otherUser.getInterests()) {
                    if (value == otherUser.getInterests().get(otherUser.getInterests().size() - 1)) {
                        interests += value + ".";
                    } else {
                        interests += value + ", ";

                    }
                }
                tvUserInterests.setText(interests);
                checkIfFriends();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtherUserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void sendPlayerRequest() {
        usersRef.document(thisUserDocRef).collection("FriendRequest").add(new FriendRequest(FirebaseAuth.getInstance().getUid(), false, currentUserName, currentUserDocId, ""
                , thisUserDocRef,currentUserId))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(OtherUserProfile.this, "Zahtjev poslan", Toast.LENGTH_SHORT).show();
                        documentReference.update("friendRequestRef", documentReference.getId());
                    }
                });
    }

    private void checkIfFriendRequestIsSent() {
        usersRef.document(thisUserDocRef).collection("FriendRequest").whereEqualTo("senderId", FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    FriendRequest fr = dc.toObject(FriendRequest.class);

                    if ((fr.getSenderId().equals(FirebaseAuth.getInstance().getUid())) && !(fr.isAccepted())) {
                        btnAddPlayer.setText("Zahtjev poslan");
                        btnAddPlayer.setClickable(false);

                    } else if (fr.getSenderId().equals(FirebaseAuth.getInstance().getUid()) && (fr.isAccepted())) {
                        btnAddPlayer.setText("Prijatelji ste");
                        btnAddPlayer.setClickable(false);
                    }
                    }

                }

        });

    }

    private void getCurrentUserName() {
        tinyDB = new TinyDB(OtherUserProfile.this);
        currentUserDocId = tinyDB.getString("USERDOCREF");
        FirebaseFirestore.getInstance().collection("Users").document(currentUserDocId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        currentUserName = task.getResult().toObject(User.class).getName();
                    }
                });
    }

    private void checkIfFriendRequestRecived() {
        currentUserDocId = tinyDB.getString("USERDOCREF");

        usersRef.document(currentUserDocId).collection("FriendRequest")
                .whereEqualTo("senderDocRef", otherUserDocRef).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    if (dc.toObject(FriendRequest.class) != null) {
                        FriendRequest friendRequest = dc.toObject(FriendRequest.class);
                        friendRequestDocRef = friendRequest.getFriendRequestRef();
                        if (!friendRequest.isAccepted()) {
                            btnDeclinePlayer.setVisibility(View.VISIBLE);
                            btnDeclinePlayer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    usersRef.document(currentUserDocId).collection("FriendRequest")
                                            .document(friendRequestDocRef).delete();
                                    btnDeclinePlayer.setVisibility(View.INVISIBLE);
                                }
                            });
                            btnAddPlayer.setText("Potvrdi prijatelja");
                            btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btnDeclinePlayer.setVisibility(View.INVISIBLE);
                                    btnAddPlayer.setVisibility(View.INVISIBLE);
                                    Toast.makeText(OtherUserProfile.this, "yes", Toast.LENGTH_SHORT).show();
                                    usersRef.document(currentUserDocId).collection("FriendRequest")
                                            .document(friendRequestDocRef).update("accepted", true);

                                    usersRef.document(currentUserDocId).update("userFriends", FieldValue.arrayUnion(otherUser.getUserId()));
                                    usersRef.document(otherUserDocRef).update("userFriends", FieldValue.arrayUnion(FirebaseAuth.getInstance().getUid()));


                                }
                            });
                        } else {
                            checkIfFriendRequestIsSent();

                        }

                    }

                }

            }
        });
        checkIfFriendRequestIsSent();

    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        otherUserDocRef = intent.getStringExtra("otherUserDocRef");
        getCurrentUserName();

        try{
            usersRef.document(otherUserDocRef).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    otherUser = documentSnapshot.toObject(User.class);
                    thisUserDocRef = documentSnapshot.getId();
                    if (mContext != null) {
                        try {
                            if (otherUser.getProfilePictureUrl() != null) {
                                GlideApp.with(OtherUserProfile.this).load(otherUser.getProfilePictureUrl())
                                        .circleCrop()
                                        .placeholder(R.drawable.avatar)
                                        .into(ivUserProfilePicture);
                            } else {
                                GlideApp.with(OtherUserProfile.this).load(R.drawable.avatar).circleCrop().into(ivUserProfilePicture);

                            }
                        } catch (IllegalArgumentException exception) {
                            Log.d("IllegalArgument", "onEvent: " + exception.getMessage());

                        }


                        tvUserName.setText(otherUser.getName());
                        tvUserDescription.setText(otherUser.getDescription());
                        for (String value : otherUser.getInterests()) {
                            if (value == otherUser.getInterests().get(otherUser.getInterests().size() - 1)) {
                                interests += value + ".";
                            } else {
                                interests += value + ", ";

                            }
                        }
                        // tvUserInterests.setText(interests);

                    }

                }
            });

        }catch (Exception e){
            Log.d("OnStart", "onStart: There was an error in the onStart");
        }



    }

    private void checkIfFriends() {
        usersRef.document(currentUserDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.toObject(User.class).getUserFriends().contains(otherUser.getUserId())) {
                    btnAddPlayer.setText("Prijatelji ste");
                    btnAddPlayer.setClickable(false);
                } else {
                    checkIfFriendRequestRecived();
                }
            }
        });
    }
}
