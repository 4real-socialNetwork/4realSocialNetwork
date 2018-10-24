package com.example.marko.areyou4real;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.UsersAdapter;
import com.example.marko.areyou4real.model.UserChip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateGroup extends AppCompatActivity {

    private ChipsInput mChipsInput;
    private List<UserChip> mItems = new ArrayList<>();
    private List<ChipInterface> mItemsAdded = new ArrayList<>();
    private List<User> mItemsUsers = new ArrayList<>();


    private Context mContext = CreateGroup.this;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setUpToolbar();

        mChipsInput = findViewById(R.id.chips_input);

        initComponent();
    }
    private void initComponent(){
        // get Users for the database
        loadUsers();

        //chips listener

    }
    private void getUsersChipList(){
        Integer id = 0;
        for (User user : mItemsUsers){
            UserChip userChip = new UserChip(id.toString(), user.name, user.email);

            mItems.add(userChip);
            id++;
        }
        System.out.println("mItems filled with userChip objects");
        mChipsInput.setFilterableList(mItems);
    }

    public void loadUsers() {
        mUsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    System.out.println("onComplete: Task is succesfull.");
                    for (DocumentSnapshot dc : task.getResult()){
                        User user = dc.toObject(User.class);
                        mItemsUsers.add(user);
                    }
                    System.out.println("users loaded to the list: " + mItemsUsers.toString());

                    ((ImageButton) findViewById(R.id.users)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("clicked all the users, dialog appears");
                            dialogUsers();
                        }
                    });
                    // adding users to the chip list
                    getUsersChipList();


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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Could not fetch users from firestore", Toast.LENGTH_SHORT).show();
            }
        });
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

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UsersAdapter _adapter = new UsersAdapter(mContext, mItemsUsers);
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
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
