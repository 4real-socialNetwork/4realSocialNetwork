package com.example.marko.areyou4real.LoginCreateUser;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
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

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.UserProfile;
import com.example.marko.areyou4real.adapter.GlideApp;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.dialogs.InterestDialog;
import com.example.marko.areyou4real.fragments.TimePickerFragment;
import com.example.marko.areyou4real.model.FriendRequest;
import com.example.marko.areyou4real.model.Group;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateUser extends AppCompatActivity {

    private static final String TAG = "CreateUser";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = db.collection("Users");
    private StorageReference mStorageRef;


    private Context mContext = CreateUser.this;
    private Button btnInteres;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText surname;
    private EditText description;
    private AppCompatSeekBar seekBar;
    private Button btnCreateAccount;
    private ProgressBar progressBar;
    private TextView showSeekBar;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private TextView tvShowInterest;
    public TinyDB tinyDB;
    public String mToken = "";
    private ArrayList<String> friendsList = new ArrayList<>();
    private Uri mPictureUri;
    private String profilePictureUrl = "";
    private ImageView profilePicture;
    private static final int PICK_IMAGE_REQUEST = 1;


    private int current_range = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setUpToolbar();
        btnInteres = findViewById(R.id.btnInteres);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        description = findViewById(R.id.etDescripiton);
        seekBar = findViewById(R.id.seekBar);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        progressBar = findViewById(R.id.progressBarUser);
        showSeekBar = findViewById(R.id.tvSeekBarShower);
        tvShowInterest = findViewById(R.id.tvShowInterest);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePictures");
        profilePicture = findViewById(R.id.ivProfilePicture);

        btnCreateAccount.setClickable(false);
        GlideApp.with(this).load(R.drawable.avatar).circleCrop().into(profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        showSeekBar.setText(current_range + " km");
        seekBar.setProgress(current_range);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showSeekBar.setText(progress + " km");
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                uploadFile();
            }
        });
        btnInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    private void createUserAndAccount() {
        if (!validateForm()) {
            return;
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(CreateUser.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                mToken = instanceIdResult.getToken();

            }
        });




        final String mail = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String ime = name.getText().toString().trim();
        final String prezime = surname.getText().toString().trim();
        final String opis = description.getText().toString().trim();
        final int udaljenost = seekBar.getProgress();


        if (mail.contentEquals("@") || pass.length() > 6) {
            mAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user = new User(FirebaseAuth.getInstance().getUid(), mToken, ime, prezime, mail, opis, selectedItems, udaljenost, friendsList, profilePictureUrl);
                            mUsersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(final DocumentReference documentReference) {
                                    friendsList.add(FirebaseAuth.getInstance().getUid());
                                    mUsersRef.document(documentReference.getId()).update("userDocRef",documentReference.getId());
                                    db.collection("Groups").add(new Group("Prijatelji", friendsList, "", mAuth.getUid(), true)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            db.collection("Groups").document(task.getResult().getId()).update("groupId", task.getResult().getId());

                                        }
                                    });
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateUser.this, "Već postoji korisnik s tim računom", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            Toast.makeText(this, "Email ili šifa nisu ispravni", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Obavezno ispuniti");
            result = false;
            btnCreateAccount.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            email.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            password.setError(null);
        }
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            name.setError(null);
        }
        if (TextUtils.isEmpty(surname.getText().toString())) {
            surname.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            surname.setError(null);
        }
        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            description.setError(null);
        }

        return result;
    }

    public void openDialog() {
        InterestDialog dialog = new InterestDialog();
        dialog.show(getFragmentManager(), "ExampleDialog");
    }

    public void setItems(ArrayList<String> items) {
        selectedItems.clear();
        String string = "";
        selectedItems.addAll(items);
        for (int i = 0; i < selectedItems.size(); i++) {
            string += "" + selectedItems.get(i) + ",";
        }
        tvShowInterest.setText(string);

    }

    private void setUpToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void uploadFile() {
        if(mPictureUri!=null){
            final StorageReference fileReferance = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mPictureUri));

            UploadTask uploadTask = fileReferance.putFile(mPictureUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(CreateUser.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata();
                    taskSnapshot.getUploadSessionUri();

                    fileReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePictureUrl = uri.toString();
                            Toast.makeText(mContext, profilePictureUrl, Toast.LENGTH_SHORT).show();
                            createUserAndAccount();
                        }
                    });
                }
            });
        }else{
            createUserAndAccount();
        }





    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
            mPictureUri = data.getData();
            GlideApp
                    .with(CreateUser.this)
                    .load(mPictureUri)
                    .circleCrop()
                    .into(profilePicture);

            uploadFile();
        }
    }


}
