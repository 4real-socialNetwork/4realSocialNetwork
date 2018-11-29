package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.SearchUserRecyclerViewAdapter;
import com.example.marko.areyou4real.model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InsideGroup extends AppCompatActivity {
    private Context mContext = InsideGroup.this;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    private CollectionReference groupsRef = db.collection("Groups");
    private SearchUserRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Intent intent;
    private String groupId = "";
    private ArrayList<String> usersInArray = new ArrayList<>();
    private String groupName = "";
    private FloatingActionButton fab;
    private ArrayList<User> userList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_group);
        intent = getIntent();
        groupName = intent.getStringExtra("GROUP_NAME");
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsideGroup.this, GroupChatRoom.class);
                intent.putExtra("GROUPID", groupId);
                intent.putStringArrayListExtra("USERS_IN_GROUP",usersInArray);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group name");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(groupName);


        loadData();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void setmAdapter() {
        mRecyclerView = findViewById(R.id.container);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new SearchUserRecyclerViewAdapter(mContext,userList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        intent = getIntent();
        groupId = intent.getStringExtra("GROUP_ID");

        groupsRef.document(groupId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Group group = documentSnapshot.toObject(Group.class);
                usersInArray.addAll(group.getListOfUsersInGroup());
                setmAdapter();
                for (String value : usersInArray) {
                    userRef.whereEqualTo("userId", value)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot dc : task.getResult()) {
                                    User user = dc.toObject(User.class);
                                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                                        userList.add(user);
                                        mAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
