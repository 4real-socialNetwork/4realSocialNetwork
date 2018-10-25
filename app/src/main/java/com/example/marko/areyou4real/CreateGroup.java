package com.example.marko.areyou4real;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.UsersAdapter;
import com.example.marko.areyou4real.model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
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

    private List<User> mNewGroup = new ArrayList<>();

    private Context mContext = CreateGroup.this;

    private FirebaseFirestore mInstance = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = mInstance.collection("Users");
    private CollectionReference mGroupsRef = mInstance.collection("Groups");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setUpToolbar();

        mChipsInput = findViewById(R.id.chips_input);
        mAddToGroup = findViewById(R.id.btn_create_group);
        mGroupName = findViewById(R.id.group_name);

        loadUsers();
        addToGroup();
    }

    private void loadUsers() {
        mUsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (DocumentSnapshot dc : task.getResult()){
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

        final Group group = new Group();

        mAddToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add to group triggered");
                List<Chip> contactsSelected = (List<Chip>) mChipsInput.getSelectedChipList();
                for (Chip chip : contactsSelected){
                    mNewGroup.add(mItemsUsers.get(chip.getLabel()));

                }
                for (User user : mNewGroup){
                    group.addUserId(user.getUserId());
                }
                group.setGroupName(mGroupName.getText().toString().trim());
                mGroupsRef.add(group);
                finish();

            }
        });
    }
    private void addRemoveChips(){
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

    private void getUsersChipList(){
        for (User user : mItemsUsers.values()){
            Chip userChip = new Chip(user.name, user.email);

            mUsers.add(user);
            mItems.add(userChip);
        }
        mChipsInput.setFilterableList(mItems);
    }

    private void dialogUsers() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
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

    private void setUpToolbar (){
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
}
