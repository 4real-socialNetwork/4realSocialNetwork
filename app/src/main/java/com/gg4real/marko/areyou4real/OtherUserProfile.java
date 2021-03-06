
package com.gg4real.marko.areyou4real;

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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.adapter.GlideApp;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.model.FriendRequest;
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
    private TextView tvPercent;
    private CheckBox c1;
    private CheckBox c2;
    private CheckBox c3;
    private CheckBox c4;
    private CheckBox c5;
    private CheckBox c6;
    private CheckBox c7;
    private CheckBox c8;
    private CheckBox c9;
    private CheckBox c10;
    private CheckBox c11;
    private CheckBox c12;
    private CheckBox c13;
    private CheckBox c14;
    private CheckBox c15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        toolbar = findViewById(R.id.toolbar);
        mContext = OtherUserProfile.this;
        initiliaseCheckBox();
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
        tvPercent = findViewById(R.id.tvPercent);
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
                tvPercent.setText(otherUser.getPercentage() + " % dolazaka");
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
                    if (value.equals(otherUser.getInterests().get(otherUser.getInterests().size() - 1))) {
                        interests += value;
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
                , thisUserDocRef, currentUserId,System.currentTimeMillis()+""))
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
                        btnAddPlayer.setVisibility(View.VISIBLE);
                        btnAddPlayer.setText("Zahtjev poslan");
                        btnAddPlayer.setClickable(false);

                    } else if (fr.getSenderId().equals(FirebaseAuth.getInstance().getUid()) && (fr.isAccepted())) {
                        btnAddPlayer.setVisibility(View.VISIBLE);
                        btnAddPlayer.setText("Prijatelji ste");
                        btnAddPlayer.setClickable(false);
                    } else {
                        btnAddPlayer.setVisibility(View.VISIBLE);
                        btnAddPlayer.setText("Pošalji zahtjev");
                        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendPlayerRequest();
                            }
                        });
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
                        if(task!=null){
                            currentUserName = task.getResult().toObject(User.class).getName();

                        }
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
                            btnAddPlayer.setVisibility(View.VISIBLE);
                            btnAddPlayer.setText("Potvrdi prijatelja");
                            btnAddPlayer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    btnDeclinePlayer.setVisibility(View.INVISIBLE);
                                    btnAddPlayer.setVisibility(View.INVISIBLE);
                                    Toast.makeText(OtherUserProfile.this, "Zahtjev prihvaćen", Toast.LENGTH_SHORT).show();
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

        try {
            usersRef.document(otherUserDocRef).addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    try {
                        otherUser = documentSnapshot.toObject(User.class);
                        setSkillStar(otherUser);

                    } catch (NullPointerException e1) {
                        Log.d("log", "onEvent: ");
                    }
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


                    }

                }
            });

        } catch (Exception e) {
            Log.d("OnStart", "onStart: There was an error in the onStart");
        }


    }

    private void checkIfFriends() {
        usersRef.document(currentUserDocId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.toObject(User.class).getUserFriends().contains(otherUser.getUserId())) {
                    btnAddPlayer.setVisibility(View.VISIBLE);
                    btnAddPlayer.setText("Prijatelji ste");
                    btnAddPlayer.setClickable(false);
                } else {
                    checkIfFriendRequestRecived();
                }
            }
        });
    }

    private void initiliaseCheckBox() {
        c1 = findViewById(R.id.checkBox1);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);
        c5 = findViewById(R.id.checkBox5);
        c6 = findViewById(R.id.checkBox6);
        c7 = findViewById(R.id.checkBox7);
        c8 = findViewById(R.id.checkBox8);
        c9 = findViewById(R.id.checkBox9);
        c10 = findViewById(R.id.checkBox10);
        c11 = findViewById(R.id.checkBox11);
        c12 = findViewById(R.id.checkBox12);
        c13 = findViewById(R.id.checkBox13);
        c14 = findViewById(R.id.checkBox14);
        c15 = findViewById(R.id.checkBox15);
    }

    private void setSkillStar(User user) {
        switch (user.getNogometSkill()) {
            case 0:
                break;
            case 1:
                c1.setChecked(true);
                break;
            case 2:
                c1.setChecked(true);
                c2.setChecked(true);
                break;
            case 3:
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                break;
            case 4:
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(true);
                break;
            case 5:
                c1.setChecked(true);
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(true);
                c5.setChecked(true);
                break;
        }
        switch (user.getKosarkaSkill()) {
            case 0:
                break;
            case 1:
                c6.setChecked(true);
                break;
            case 2:
                c6.setChecked(true);
                c7.setChecked(true);
                break;
            case 3:
                c6.setChecked(true);
                c7.setChecked(true);
                c8.setChecked(true);
                break;
            case 4:
                c6.setChecked(true);
                c7.setChecked(true);
                c8.setChecked(true);
                c9.setChecked(true);
                break;
            case 5:
                c6.setChecked(true);
                c7.setChecked(true);
                c8.setChecked(true);
                c9.setChecked(true);
                c10.setChecked(true);
                break;
        }switch (user.getSahSkill()) {
            case 0:
                break;
            case 1:
                c11.setChecked(true);
                break;
            case 2:
                c11.setChecked(true);
                c12.setChecked(true);
                break;
            case 3:
                c11.setChecked(true);
                c12.setChecked(true);
                c13.setChecked(true);
                break;
            case 4:
                c11.setChecked(true);
                c12.setChecked(true);
                c13.setChecked(true);
                c14.setChecked(true);
                break;
            case 5:
                c11.setChecked(true);
                c12.setChecked(true);
                c13.setChecked(true);
                c14.setChecked(true);
                c15.setChecked(true);
                break;
        }

    }
}
