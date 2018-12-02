package com.gg4real.marko.areyou4real;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.adapter.UsersInEventRecyclerAdapter;
import com.gg4real.marko.areyou4real.model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UsersInEventActivity extends AppCompatActivity {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private CollectionReference userRef = db.collection("Users");
    private CollectionReference eventsRef = db.collection("Events");
    TinyDB tinyDB;
    private UsersInEventRecyclerAdapter myRequestAdapter;
    private ArrayList<String> usersInEvent;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_event);

        mContext = UsersInEventActivity.this;
        setUpMyFriendsRecyclerView();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return true;
    }

    private void setUpMyFriendsRecyclerView() {

        myRequestAdapter = new UsersInEventRecyclerAdapter(mContext);


        RecyclerView recyclerView = findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRequestAdapter);

        loadData();

    }

    private void loadData() {
        eventsRef.document(getIntent().getStringExtra("EVENT_ID")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usersInEvent = documentSnapshot.toObject(Event.class).getListOfUsersParticipatingInEvent();
                for (String userId : usersInEvent) {
                        userRef.whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                                    User user = dc.toObject(User.class);
                                    myRequestAdapter.addUser(user);
                                    myRequestAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                }
        });


    }


}
