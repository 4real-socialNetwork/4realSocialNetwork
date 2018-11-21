package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marko.areyou4real.adapter.BottomNavigationViewHelper;
import com.example.marko.areyou4real.adapter.SearchUserRecyclerViewAdapter;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class MyFriendsActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditText mUserSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private Context mContext;
    private SearchUserRecyclerViewAdapter mAdapter;
    private ArrayList<User> userList = new ArrayList();
    private ArrayList<String> friendsId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        setUpAdapter();
        getUsersId();
        mUserSearch = findViewById(R.id.etSearchUser);
        mUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


    }


    public void setUpAdapter() {
        mContext = MyFriendsActivity.this;
        mAdapter = new SearchUserRecyclerViewAdapter(mContext, userList);
        RecyclerView recyclerView = findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void getUsersId() {
        TinyDB tinyDB = new TinyDB(MyFriendsActivity.this);
        String userDocRef = tinyDB.getString("USERDOCREF");
        usersRef.document(userDocRef).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        for (String friend : user.getUserFriends()){
                            friendsId.add(friend);
                        }
                        displayUsers();
                    }
                });
    }

    private void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }
        mAdapter.filterList(filteredList);
    }
    private void displayUsers(){
        for(int i=0;i<friendsId.size();i++){
            usersRef.whereEqualTo("userId",friendsId.get(i)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot dc : queryDocumentSnapshots){
                        User user = dc.toObject(User.class);
                        userList.add(user);
                    }
                }
            });
        }
    }
}
