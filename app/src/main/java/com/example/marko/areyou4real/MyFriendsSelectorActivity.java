package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.MyFriendsSelectorFirebaseAdapter;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MyFriendsSelectorActivity extends AppCompatActivity {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private CollectionReference userRef = db.collection("Users");
    String userDocId = "";
    TinyDB tinyDB;
    private MyFriendsSelectorFirebaseAdapter myRequestAdapter;
    private Button btnFriendsSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends_selector);

        mContext = MyFriendsSelectorActivity.this;
        setUpMyFriendsRecyclerView();
        btnFriendsSelected = findViewById(R.id.btnFriendsSelected);
        btnFriendsSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ArrayList<String> friendsSelected = new ArrayList<>();
                 friendsSelected = myRequestAdapter.getmSelectedFriends();
                 if(friendsSelected != null){
                     Intent intent = new Intent(mContext,InsideEvent.class);
                     intent.putStringArrayListExtra("friends_selected",friendsSelected);
                     setResult(MyFriendsSelectorActivity.RESULT_OK,intent);
                     finish();
                 }else{
                     Toast.makeText(mContext, "Niste izabrali igrača", Toast.LENGTH_SHORT).show();
                     finish();
                 }


            }
        });
    }

    private void setUpMyFriendsRecyclerView() {
        tinyDB = new TinyDB(mContext);
        userDocId = tinyDB.getString("USERDOCREF");
        Query query = userRef.whereArrayContains("userFriends",userId)
                .orderBy("name",Query.Direction.DESCENDING)
                .limit(50);

        FirestoreRecyclerOptions<User> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<User>()
                .setQuery(query, User.class)
                .build();

        myRequestAdapter = new MyFriendsSelectorFirebaseAdapter (firestoreRecyclerOptions, mContext);

        RecyclerView recyclerView = findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRequestAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        myRequestAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myRequestAdapter.stopListening();
    }
    
}
