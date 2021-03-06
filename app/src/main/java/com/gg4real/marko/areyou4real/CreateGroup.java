package com.gg4real.marko.areyou4real;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.adapter.GlideApp;
import com.gg4real.marko.areyou4real.adapter.UsersAdapter;
import com.gg4real.marko.areyou4real.model.Group;
import com.gg4real.marko.areyou4real.model.TextMessage;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateGroup extends AppCompatActivity {

    private static final String TAG = "CreateGroup";

    private ChipsInput mChipsInput;
    private Button mAddToGroup;
    private EditText mGroupName;

    private List<Chip> mItems = new ArrayList<>();
    private List<ChipInterface> mItemsAdded = new ArrayList<>();

    private Map<String, User> mItemsUsers = new HashMap<>();
    private List<User> mUsers = new ArrayList<>();
    private StorageReference mStorageRef;

    private List<User> mNewGroup = new ArrayList<>();


    private Context mContext = CreateGroup.this;

    private FirebaseFirestore mInstance = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = mInstance.collection("Users");
    private CollectionReference mGroupsRef = mInstance.collection("Groups");
    private DocumentReference docRef;

    private String groupId = "";

    private String mTextMessageId = "";
    private Uri mPictureUri;
    private String profilePictureUrl = new String();
    private ImageView profilePicture;
    private static final int PICK_IMAGE_REQUEST = 1;
    Group group = new Group();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setUpToolbar();
        profilePicture = findViewById(R.id.ivGroupPicture);

        mStorageRef = FirebaseStorage.getInstance().getReference("GroupPictures");
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        GlideApp.with(mContext).load(R.drawable.druzenje_three).circleCrop().into(profilePicture);

        mChipsInput = findViewById(R.id.chips_input);
        mAddToGroup = findViewById(R.id.btn_create_group);
        mGroupName = findViewById(R.id.group_name);

        loadUsers();
        addToGroup();
    }

    private void loadUsers() {
        mUsersRef.whereArrayContains("userFriends", FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot dc : task.getResult()) {
                        User user = dc.toObject(User.class);
                        mItemsUsers.put(user.getName(), user);
                    }
                    getUsersChipList();

                    findViewById(R.id.users).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogUsers();
                        }
                    });

                    addRemoveChips();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Could not fetch users from firestore.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToGroup() {

        final DocumentReference newGroup = mGroupsRef.document("1");

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mAddToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    return;
                }
                mAddToGroup.setClickable(false);
                System.out.println("Add to group triggered");
                List<Chip> contactsSelected = (List<Chip>) mChipsInput.getSelectedChipList();
                for (Chip chip : contactsSelected) {
                    if (!mNewGroup.contains(mItemsUsers.get(chip.getLabel()))) {
                        mNewGroup.add(mItemsUsers.get(chip.getLabel()));

                    }

                }
                for (User user : mNewGroup) {
                    group.addUserId(user.getUserId());
                }
                group.addUserId(FirebaseAuth.getInstance().getUid());
                group.setGroupName(mGroupName.getText().toString().trim());
                group.setGroupPictureUrl(profilePictureUrl);
                if (group.getListOfUsersInGroup().size() > 1 ) {
                    mAddToGroup.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    mGroupsRef.add(group).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                docRef = task.getResult();
                                groupId = docRef.getId();
                                docRef.update("groupId", groupId, "groupPictureUrl", profilePictureUrl, "groupAdmin", FirebaseAuth.getInstance().getUid());
                                createChat();

                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Grupa mora imati više od jednoga člana", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    mAddToGroup.setClickable(true);
                }


            }
        });
    }

    private void addRemoveChips() {
        mChipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int i) {
                mItemsAdded.add(chip);
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int i) {
                mItemsAdded.remove(chip);
            }

            @Override
            public void onTextChanged(CharSequence charSequence) {

            }
        });
    }

    private void getUsersChipList() {
        for (User user : mItemsUsers.values()) {
            Chip userChip = new Chip(user.name, user.email);

            mUsers.add(user);
            mItems.add(userChip);
        }
        mChipsInput.setFilterableList(mItems);
    }

    private void dialogUsers() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setTitle("Lista prijatelja");
        dialog.setContentView(R.layout.dialog_users);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        UsersAdapter _adapter = new UsersAdapter(mContext, mUsers);
        recyclerView.setAdapter(_adapter);
        _adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User obj, int position) {
                mChipsInput.addChip(obj.name, obj.email);
                dialog.hide();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

    private void createChat() {
        mGroupsRef.document(groupId).collection("chatRoom").add(new TextMessage("", "Nova grupa stvorena", "", "", groupId, Calendar.getInstance().getTimeInMillis(), null)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mTextMessageId = documentReference.getId();
                mGroupsRef.document(groupId).collection("chatRoom").document(mTextMessageId)
                        .update("eventChatId", mTextMessageId);
                Intent intent = new Intent(mContext, InsideGroup.class);
                intent.putExtra("GROUP_ID", groupId);
                startActivity(intent);
                finish();

            }
        });
    }

    private void uploadFile() {
        if (mPictureUri != null) {
            final StorageReference fileReferance = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mPictureUri));

            UploadTask uploadTask = fileReferance.putFile(mPictureUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(CreateGroup.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
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
                            progressBar.setVisibility(View.INVISIBLE);
                            mAddToGroup.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            progressBar.setVisibility(View.VISIBLE);
            mPictureUri = data.getData();
            GlideApp
                    .with(CreateGroup.this)
                    .load(mPictureUri)
                    .circleCrop()
                    .into(profilePicture);

            uploadFile();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mGroupName.getText().toString())) {
            mGroupName.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            mGroupName.setError(null);
        }

        return result;
    }


}

