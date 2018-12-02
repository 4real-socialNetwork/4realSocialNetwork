package com.gg4real.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gg4real.marko.areyou4real.adapter.RemovePlayerRecyclerAdapter;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RemovePlayerActivity extends AppCompatActivity {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private CollectionReference userRef = db.collection("Users");
    private CollectionReference eventsRef = db.collection("Events");
    TinyDB tinyDB;
    private RemovePlayerRecyclerAdapter myRequestAdapter;
    private Button btnFriendsSelected;
    private ArrayList<String> usersInEvent = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends_selector);

        mContext = RemovePlayerActivity.this;
        setUpMyFriendsRecyclerView();


        btnFriendsSelected = findViewById(R.id.btnFriendsSelected);
        btnFriendsSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InsideEvent.class);
                intent.putStringArrayListExtra("to_be_removed", myRequestAdapter.getToBeRemoved());
                setResult(RemovePlayerActivity.RESULT_OK, intent);
                finish();

            }


        });
    }

    private void setUpMyFriendsRecyclerView() {

        myRequestAdapter = new RemovePlayerRecyclerAdapter(mContext);


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
                    if (!userId.equals(FirebaseAuth.getInstance().getUid())) {
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
            }
        });


    }


}
