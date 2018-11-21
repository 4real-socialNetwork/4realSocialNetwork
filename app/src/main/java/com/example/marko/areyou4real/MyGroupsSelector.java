package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.MyEventsAdapter;
import com.example.marko.areyou4real.adapter.MyFriendRequestRecyclerAdapter;
import com.example.marko.areyou4real.adapter.MyGroupsFirebaseRecylcerAdapter;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.FriendRequest;
import com.example.marko.areyou4real.model.Group;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyGroupsSelector extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyGroupsFirebaseRecylcerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("Groups");
    private Toolbar toolbar;
    private Button btnGrupSelected;
    private TinyDB tinyDB;
    private Context mContext;
    private String userId = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups_selector);
        mContext = MyGroupsSelector.this;
        setUpAdapter();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnGrupSelected = findViewById(R.id.btnGroupSelected);
        btnGrupSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("users_in_group",mAdapter.getUsersInGroup());
                intent.putStringArrayListExtra("group_names",mAdapter.getGroupNames());
                setResult(MyGroupsSelector.RESULT_OK,intent);
                finish();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    private void setUpAdapter(){
        Query query = groupsRef.whereArrayContains("listOfUsersInGroup", userId)
                .orderBy("groupName", Query.Direction.DESCENDING)
                .limit(50);

        FirestoreRecyclerOptions<Group> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Group>()
                .setQuery(query, Group.class)
                .build();

        mAdapter = new MyGroupsFirebaseRecylcerAdapter(firestoreRecyclerOptions,mContext);

        recyclerView = findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
