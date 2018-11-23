package com.example.marko.areyou4real;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.marko.areyou4real.LoginCreateUser.LoginActivity;
import com.example.marko.areyou4real.adapter.BottomNavigationViewHelper;
import com.example.marko.areyou4real.adapter.GlideApp;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.dialogs.InterestDialog;
import com.example.marko.areyou4real.fragments.TimePickerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private EditText name;
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
    private Button btnUploadPicture;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStorageRef;
    private String pictureUrl = "";
    private TinyDB tinyDB;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setUpBottomNavigationView();
        mContext = UserProfile.this;


        tvInterests = findViewById(R.id.interestTextView);
        name = findViewById(R.id.etUserName);
        userDescription = findViewById(R.id.etUserDescription);
        profilePicture = findViewById(R.id.ivProfilePicture);
        GlideApp.with(UserProfile.this).load(R.drawable.avatar).circleCrop().into(profilePicture);
        btnUserInterest = findViewById(R.id.btnUserInterest);
        btnSaveChanges = findViewById(R.id.btnSaveUserChanges);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);
        showSeekBar = findViewById(R.id.tvSeekBarShower);
        btnLogOut = findViewById(R.id.btnLogout);
        btnFriendsList = findViewById(R.id.btnFriendsList);
        btnUploadPicture = findViewById(R.id.btnUploadPicture);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePictures");


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
            }
        });

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                progressBar.setVisibility(View.VISIBLE);
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
                name.setText(user.getName());
                if(mContext!=null){
                    GlideApp.with(UserProfile.this).load(user.getProfilePictureUrl()).
                            placeholder(R.drawable.avatar).circleCrop()
                            .into(profilePicture);
                }

                for (int i = 0; i < user.getInterests().size(); i++) {
                    if (user.getInterests().get(i) != null) {
                        interest += user.getInterests().get(i) + ",";

                    }
                }
                tvInterests.setText(interest.toLowerCase());
                //setBar();
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


    private void updateUserProfile() {
    tinyDB = new TinyDB(UserProfile.this);
    String userDocRef = tinyDB.getString("USERDOCREF");

        if (updatedInterest.size() == 0) {
            usersRef.document(userDocRef).update("range", current_range, "name", name.getText().toString(), "description",
                    userDescription.getText().toString()
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            usersRef.document(userDocRef).update("range", current_range, "name", name.getText().toString(), "description",
                    userDescription.getText().toString(), "interests", updatedInterest
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserProfile.this, "Promjene spremljene", Toast.LENGTH_SHORT).show();
                }
            });
        }


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
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(UserProfile.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
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
            mImageUri = data.getData();
            GlideApp.with(UserProfile.this)
                    .load(mImageUri)
                    .circleCrop()
                    .into(profilePicture);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {

        if(mImageUri!=null){
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
                            Toast.makeText(UserProfile.this, pictureUrl, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        TinyDB tinyDB = new TinyDB(UserProfile.this);
        String thisUserDocRef = tinyDB.getString("USERDOCREF");
        usersRef.document(thisUserDocRef).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot!=null){
                    User user = documentSnapshot.toObject(User.class);
                    name.setText(user.getName());
                    if(mContext!=null){
                        GlideApp.with(UserProfile.this).load(user.getProfilePictureUrl()).
                                placeholder(R.drawable.avatar).circleCrop()
                                .into(profilePicture);
                    }

                    // for (int i = 0; i < user.getInterests().size(); i++) {
                    //     if (user.getInterests().get(i) != null) {
                    //       interest += user.getInterests().get(i) + ",";

                    //    }
                    // }
                    userDescription.setText(user.getDescription());
                    current_range = user.getRange();
                    setBar();
                }



            }
        });

    }
}

