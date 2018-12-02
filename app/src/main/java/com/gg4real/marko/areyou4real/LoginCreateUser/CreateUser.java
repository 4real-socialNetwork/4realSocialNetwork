package com.gg4real.marko.areyou4real.LoginCreateUser;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.MainActivity;
import com.gg4real.marko.areyou4real.R;
import com.gg4real.marko.areyou4real.User;
import com.gg4real.marko.areyou4real.adapter.GlideApp;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.dialogs.SkillDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class CreateUser extends AppCompatActivity {

    private static final String TAG = "CreateUser";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = db.collection("Users");
    private StorageReference mStorageRef;


    private Context mContext = CreateUser.this;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText passwordCheck;
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


    private int current_range = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setUpToolbar();
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        name = findViewById(R.id.etName);
        passwordCheck = findViewById(R.id.etPasswordCheck);
        description = findViewById(R.id.etDescripiton);
        seekBar = findViewById(R.id.seekBar);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        progressBar = findViewById(R.id.progressBarUser);
        showSeekBar = findViewById(R.id.tvSeekBarShower);
        tvShowInterest = findViewById(R.id.tvShowInterest);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePictures");
        profilePicture = findViewById(R.id.ivProfilePicture);
        btnNogomet = findViewById(R.id.btnNogomet);
        btnKosarka = findViewById(R.id.btnKosarka);
        btnSah = findViewById(R.id.btnSah);
        btnDrustveneIgre = findViewById(R.id.btnDrustveneIgre);
        btnDruzenje = findViewById(R.id.btnDruzenje);
        btnOstalo = findViewById(R.id.btnOstalo);
        tinyDB = new TinyDB(CreateUser.this);


        btnCreateAccount.setClickable(false);
        GlideApp.with(this).load(R.drawable.avatar).circleCrop().into(profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        setUpInterestButtons();

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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final String mail = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String ime = name.getText().toString().trim();
        final String opis = description.getText().toString().trim();
        final int udaljenost = seekBar.getProgress();


        if (mail.contentEquals("@") || pass.length() > 6) {
            mAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            User user = new User(FirebaseAuth.getInstance().getUid(), mToken, ime, mail, opis, selectedItems, udaljenost, friendsList, profilePictureUrl,
                                    nogometSkill, kosarkaSkill, sahSkill, 0, 0, 100);
                            mUsersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(final DocumentReference documentReference) {
                                    friendsList.add(FirebaseAuth.getInstance().getUid());
                                    tinyDB.putString("USERDOCREF", documentReference.getId());

                                    mUsersRef.document(documentReference.getId()).update("userDocRef", documentReference.getId());

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
                    Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(email.getText().toString().trim())) {
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


        } else if ((password.getText().toString().trim().length()) < 7) {
            password.setError("Lozinka mora sadržavati najmanje 7 znakova");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);

        } else {
            password.setError(null);
        }

        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else

        {
            name.setError(null);
        }
        if (TextUtils.isEmpty(passwordCheck.getText().toString()))

        {
            passwordCheck.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else if ((password.getText().toString().trim().equals(passwordCheck.getText().toString().trim()))) {
            passwordCheck.setError("Lozinke nisu jednake");

        } else

        {
            passwordCheck.setError(null);
        }
        if (TextUtils.isEmpty(description.getText().

                toString()))

        {
            description.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else

        {
            description.setError(null);
        }

        return result;
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
        if (mPictureUri != null) {
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
                            Toast.makeText(mContext, "Još par trenutaka", Toast.LENGTH_SHORT).show();
                            createUserAndAccount();
                        }
                    });
                }
            });
        } else {
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

    private void setUpInterestButtons() {
        btnNogomet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItEventNogomet % 2 == 0) {
                    setInterestNumber(1);
                    SkillDialog skillDialog = new SkillDialog();
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
                        SkillDialog skillDialog = new SkillDialog();
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
                        SkillDialog skillDialog = new SkillDialog();
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

    }

    public void setKosarkaSkill(int i) {
        kosarkaSkill = i;
    }

    public void setSahSkill(int i) {
        sahSkill = i;
    }


}




