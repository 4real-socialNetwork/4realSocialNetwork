package com.gg4real.marko.areyou4real;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.LoginCreateUser.LoginActivity;
import com.gg4real.marko.areyou4real.adapter.BadgeHelperClass;
import com.gg4real.marko.areyou4real.adapter.BottomNavigationViewHelper;
import com.gg4real.marko.areyou4real.adapter.GlideApp;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.dialogs.SkillDialogUserProfile;
import com.gg4real.marko.areyou4real.model.FriendRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import javax.annotation.Nullable;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private TextView name;
    private EditText userDescription;
    private ImageView profilePicture;
    private Uri mImageUri;
    private Button btnUserInterest;
    private Button btnSaveChanges;
    private ArrayList<String> updatedInterest = new ArrayList<>();
    private TextView tvInterests;
    private String interest = "Interesi su : ";
    private ProgressBar progressBar;
    private String value = "";
    private int interesiWasClicked = 0;
    private int current_range;
    private TextView showSeekBar;
    private AppCompatSeekBar seekBar;
    private static final int ACTIVITY_NUM = 3;
    private Button btnLogOut;
    private Button btnFriendsList;
    private TextView btnUploadPicture;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStorageRef;
    private String pictureUrl = "";
    private TinyDB tinyDB;
    private Context mContext;
    private String userProfilePictureUrl = new String();
    private int isPictureChanging = 0;
    private TextView tvPercent;
    private Button btnNogomet;
    private Button btnKosarka;
    private Button btnSah;
    private Button btnDrustveneIgre;
    private Button btnDruzenje;
    private Button btnOstalo;
    int isItEventNogomet = 4;
    int isItEventKosarka = 4;
    int isItEventSah = 4;
    int isItEventDruzenje = 4;
    int isItEventDrustvene = 4;
    int isItEventOstalo = 4;
    int interestNumber;
    private int nogometSkill;
    private int kosarkaSkill;
    private int sahSkill;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private TextView tvNogometSkill;
    private TextView tvKosarkaSkill;
    private TextView tvSahSkill;
    private ProgressBar progressBarPicture;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private BadgeHelperClass badgeHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setUpBottomNavigationView();
        mContext = UserProfile.this;
        tinyDB = new TinyDB(UserProfile.this);
        badgeHelper = new BadgeHelperClass(tinyDB,mContext,usersRef,bottomNavigationViewEx);



        name = findViewById(R.id.etUserName);
        userDescription = findViewById(R.id.etUserDescription);
        profilePicture = findViewById(R.id.ivProfilePicture);
        tvPercent = findViewById(R.id.tvPercent);
        GlideApp.with(UserProfile.this).load(R.drawable.avatar).circleCrop().into(profilePicture);
        btnSaveChanges = findViewById(R.id.btnSaveUserChanges);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);
        showSeekBar = findViewById(R.id.tvSeekBarShower);
        btnLogOut = findViewById(R.id.btnLogout);
        btnFriendsList = findViewById(R.id.btnFriendsList);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePictures");
        btnNogomet = findViewById(R.id.btnNogomet);
        btnKosarka = findViewById(R.id.btnKosarka);
        btnSah = findViewById(R.id.btnSah);
        btnDrustveneIgre = findViewById(R.id.btnDrustveneIgre);
        btnDruzenje = findViewById(R.id.btnDruzenje);
        tvNogometSkill = findViewById(R.id.tvNogometSkill);
        tvKosarkaSkill = findViewById(R.id.tvKosarkaSkill);
        tvSahSkill = findViewById(R.id.tvSahSkill);
        progressBarPicture = findViewById(R.id.progressBarPicture);
        btnOstalo = findViewById(R.id.btnOstalo);


        updateUI();

        setUpInterestButtons();


        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                updateUserProfile();

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfile.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnFriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, MyFriendsActivity.class);
                startActivity(intent);
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                isPictureChanging = 1;
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
        TinyDB tinyDB = new TinyDB(UserProfile.this);
        String thisUserDocRef = tinyDB.getString("USERDOCREF");
        usersRef.document(thisUserDocRef).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                userProfilePictureUrl = user.getProfilePictureUrl();
                name.setText(user.getName());
                tvPercent.setText(user.getPercentage() + " %");
                pictureUrl = userProfilePictureUrl;
                nogometSkill = user.getNogometSkill();
                kosarkaSkill = user.getKosarkaSkill();
                sahSkill = user.getSahSkill();
                tvNogometSkill.setText(nogometSkill + "");
                tvKosarkaSkill.setText(kosarkaSkill + "");
                tvSahSkill.setText(sahSkill + "");
                if (mContext != null) {
                    GlideApp.with(UserProfile.this).load(user.getProfilePictureUrl()).
                            placeholder(R.drawable.avatar).circleCrop()
                            .into(profilePicture);
                }

                for (String interest : user.getInterests()) {
                    switch (interest) {
                        case "Nogomet":
                            btnNogomet.setPressed(true);
                            btnNogomet.setBackgroundResource(R.drawable.interest_button_pressed);
                            isItEventNogomet += 1;
                            if (!selectedItems.contains("Nogomet")) {
                                selectedItems.add("Nogomet");
                            }
                            break;
                        case "Košarka":
                            btnKosarka.setPressed(true);
                            if (btnKosarka.isPressed()) {
                                btnKosarka.setBackgroundResource(R.drawable.interest_button_pressed);
                                isItEventKosarka += 1;
                                if (!selectedItems.contains("Košarka")) {
                                    selectedItems.add("Košarka");
                                }
                            }
                            break;
                        case "Šah":
                            btnSah.setPressed(true);
                            btnSah.setBackgroundResource(R.drawable.interest_button_pressed);
                            isItEventSah += 1;
                            if (!selectedItems.contains("Šah")) {
                                selectedItems.add("Šah");

                            }
                            break;
                        case "Druženje":
                            btnDruzenje.setPressed(true);
                            btnDruzenje.setBackgroundResource(R.drawable.interest_button_pressed);
                            isItEventDruzenje += 1;
                            if (!selectedItems.contains("Druženje")) {
                                selectedItems.add("Druženje");
                            }
                            break;

                        case "Društvene igre":
                            btnDrustveneIgre.setPressed(true);
                            if (btnDrustveneIgre.isPressed()) {
                                btnDrustveneIgre.setBackgroundResource(R.drawable.interest_button_pressed);
                                isItEventDrustvene += 1;
                                if (!selectedItems.contains("Društvene igre")) {
                                    selectedItems.add("Društvene igre");

                                }
                            }
                        case "Ostalo":
                            btnOstalo.setPressed(true);
                            if(btnOstalo.isPressed()){
                                btnOstalo.setBackgroundResource(R.drawable.interest_button_pressed);
                                isItEventOstalo +=1;
                                if(!selectedItems.contains("Ostalo")){
                                    selectedItems.add("Ostalo");
                                }
                            }
                    }
                }

            }

        });

    }


    private void updateUserProfile() {
        tinyDB = new TinyDB(UserProfile.this);
        String userDocRef = tinyDB.getString("USERDOCREF");

        usersRef.document(userDocRef).update("range", current_range, "name", name.getText().toString(), "description",
                userDescription.getText().toString(), "interests", selectedItems, "nogometSkill", nogometSkill, "kosarkaSkill", kosarkaSkill, "sahSkill", sahSkill,
                "profilePictureUrl",pictureUrl
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.INVISIBLE);
                isPictureChanging = 0;
                Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setBar() {
        showSeekBar.setText(current_range + " km");
        seekBar.setProgress(current_range);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showSeekBar.setText(progress + " km");
                seekBar.setProgress(progress);
                current_range = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUpBottomNavigationView() {
        bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(UserProfile.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setIcon(R.drawable.nav_profile_selected);
        menuItem.setChecked(true);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            progressBarPicture.setVisibility(View.VISIBLE);
            mImageUri = data.getData();
            GlideApp.with(UserProfile.this)
                    .load(mImageUri)
                    .circleCrop()
                    .into(profilePicture);
            if (mImageUri != null) {
                final StorageReference fileReferance = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                fileReferance.putFile(mImageUri).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UserProfile.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata();
                        taskSnapshot.getUploadSessionUri();

                        fileReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pictureUrl = uri.toString();
                                progressBar.setVisibility(View.INVISIBLE);
                                updateUserProfile();
                                isPictureChanging = 0;
                                Toast.makeText(mContext, "Slika profila postavljena", Toast.LENGTH_SHORT).show();
                                progressBarPicture.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                });
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    @Override
    protected void onStart() {
        super.onStart();
        TinyDB tinyDB = new TinyDB(UserProfile.this);
        String thisUserDocRef = tinyDB.getString("USERDOCREF");
        usersRef.document(thisUserDocRef).addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    name.setText(user.getName());
                    tvPercent.setText(user.getPercentage() + " %");
                    if (mContext != null) {
                        if (isPictureChanging == 0) {
                            try {
                                GlideApp.with(UserProfile.this).load(user.getProfilePictureUrl()).
                                        placeholder(R.drawable.avatar).circleCrop()
                                        .into(profilePicture);
                            } catch (IllegalArgumentException e1) {
                                Log.d("Illegal argument", "onEvent: " + e1.getMessage());
                            }

                        }
                    }


                    userDescription.setText(user.getDescription());
                    current_range = user.getRange();
                    setBar();
                }


            }
        });
        setQuery();

    }

    private void setUpInterestButtons() {
        btnNogomet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventNogomet % 2 == 0) {
                    setInterestNumber(1);
                    SkillDialogUserProfile skillDialog = new SkillDialogUserProfile();
                    skillDialog.show(getFragmentManager(), "NogometSkinDialog");
                    btnNogomet.setPressed(true);
                    if (btnNogomet.isPressed()) {
                        btnNogomet.setBackgroundResource(R.drawable.interest_button_pressed);
                        isItEventNogomet += 1;
                        if (!selectedItems.contains("Nogomet")) {
                            selectedItems.add("Nogomet");

                        }

                    }
                } else {
                    btnNogomet.setPressed(false);
                    if (!btnNogomet.isPressed()) {
                        nogometSkill = 0;
                        btnNogomet.setBackgroundResource(R.drawable.interest_button);
                        isItEventNogomet += 1;
                        selectedItems.remove("Nogomet");
                    }

                }

            }
        });

        btnKosarka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventKosarka % 2 == 0) {
                    btnKosarka.setPressed(true);
                    if (btnKosarka.isPressed()) {
                        setInterestNumber(2);
                        SkillDialogUserProfile skillDialog = new SkillDialogUserProfile();
                        skillDialog.show(getFragmentManager(), "KosarkaSkillDialog");
                        btnKosarka.setBackgroundResource(R.drawable.interest_button_pressed);
                        isItEventKosarka += 1;
                        if (!selectedItems.contains("Košarka")) {
                            selectedItems.add("Košarka");

                        }

                    }
                } else {
                    btnKosarka.setPressed(false);
                    if (!btnKosarka.isPressed()) {
                        kosarkaSkill = 0;
                        btnKosarka.setBackgroundResource(R.drawable.interest_button);
                        isItEventKosarka += 1;
                        selectedItems.remove("Košarka");
                    }

                }

            }
        });


        btnSah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventSah % 2 == 0) {
                    btnSah.setPressed(true);
                    if (btnSah.isPressed()) {
                        setInterestNumber(3);
                        SkillDialogUserProfile skillDialog = new SkillDialogUserProfile();
                        skillDialog.show(getFragmentManager(), "SahSkillDialog");
                        btnSah.setBackgroundResource(R.drawable.interest_button_pressed);
                        isItEventSah += 1;
                        if (!selectedItems.contains("Šah")) {
                            selectedItems.add("Šah");

                        }

                    }
                } else {
                    btnSah.setPressed(false);
                    if (!btnSah.isPressed()) {
                        sahSkill = 0;
                        btnSah.setBackgroundResource(R.drawable.interest_button);
                        isItEventSah += 1;
                        selectedItems.remove("Šah");
                    }

                }

            }
        });

        btnDruzenje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventDruzenje % 2 == 0) {
                    btnDruzenje.setPressed(true);
                    if (btnDruzenje.isPressed()) {
                        btnDruzenje.setBackgroundResource(R.drawable.interest_button_pressed);
                        isItEventDruzenje += 1;
                        if (!selectedItems.contains("Druženje")) {
                            selectedItems.add("Druženje");

                        }

                    }
                } else {
                    btnDruzenje.setPressed(false);
                    if (!btnDruzenje.isPressed()) {
                        btnDruzenje.setBackgroundResource(R.drawable.interest_button);
                        isItEventDruzenje += 1;
                        selectedItems.remove("Druženje");
                    }

                }

            }
        });

        btnDrustveneIgre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventDrustvene % 2 == 0) {
                    btnDrustveneIgre.setPressed(true);
                    if (btnDrustveneIgre.isPressed()) {
                        btnDrustveneIgre.setBackgroundResource(R.drawable.interest_button_pressed);
                        isItEventDrustvene += 1;
                        if (!selectedItems.contains("Društvene igre")) {
                            selectedItems.add("Društvene igre");

                        }

                    }
                } else {
                    btnDrustveneIgre.setPressed(false);
                    if (!btnDrustveneIgre.isPressed()) {
                        btnDrustveneIgre.setBackgroundResource(R.drawable.interest_button);
                        isItEventDrustvene += 1;
                        selectedItems.remove("Društvene igre");
                    }

                }

            }
        });
        btnOstalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventOstalo % 2 == 0) {
                    btnOstalo.setPressed(true);
                    if (btnOstalo.isPressed()) {
                        btnOstalo.setBackgroundResource(R.drawable.interest_button_pressed);
                        isItEventOstalo += 1;
                        if (!selectedItems.contains("Ostalo")) {
                            selectedItems.add("Ostalo");
                        }
                    }
                } else {
                    btnOstalo.setPressed(false);
                    if (!btnOstalo.isPressed()) {
                        btnOstalo.setBackgroundResource(R.drawable.interest_button);
                        isItEventOstalo += 1;
                        selectedItems.remove("Ostalo");
                    }

                }

            }
        });
    }

    public void setInterestNumber(int i) {

        interestNumber = i;
    }

    public int getIntNumber() {
        return interestNumber;
    }

    public void setNogmetSkill(int i) {
        nogometSkill = i;
        tvNogometSkill.setText(nogometSkill + "");
    }

    public void setKosarkaSkill(int i) {
        kosarkaSkill = i;
        tvKosarkaSkill.setText(kosarkaSkill + "");
    }

    public void setSahSkill(int i) {
        sahSkill = i;
        tvSahSkill.setText(sahSkill + "");
    }

    private Badge addBadgeAt(int number, BottomNavigationViewEx navigationViewEx) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(navigationViewEx.getBottomNavigationItemView(4))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                            Toast.makeText(UserProfile.this, "me", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void setQuery() {
        tinyDB = new TinyDB(UserProfile.this);
        Query query = usersRef.document(tinyDB.getString("USERDOCREF")).collection("FriendRequest").whereEqualTo("accepted", false)
                .orderBy("senderName")
                .limit(50);
        try{
            query.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for(DocumentSnapshot dc : queryDocumentSnapshots){
                        if(dc.toObject(FriendRequest.class)!=null){
                            addBadgeAt(1, bottomNavigationViewEx);

                        }
                    }
                }
            });
        }catch (NullPointerException e){
            Log.d("TAG", "setQuery: "+e.getMessage());
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeHelper.setQueryFriendRequest(bottomNavigationViewEx);
        badgeHelper.setQueryEventRequest(bottomNavigationViewEx);
    }
}
