package com.gg4real.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.adapter.BadgeHelperClass;
import com.gg4real.marko.areyou4real.adapter.BottomNavigationViewHelper;
import com.gg4real.marko.areyou4real.adapter.SearchUserRecyclerViewAdapter;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.model.FriendRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import javax.annotation.Nullable;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class SearchUserActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditText mUserSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private Context mContext;
    private SearchUserRecyclerViewAdapter mAdapter;
    private ArrayList<User> userList = new ArrayList();
    private TinyDB tinyDB;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private BadgeHelperClass badgeHelper;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        getUsers();
        mContext = SearchUserActivity.this;
        view = findViewById(R.id.relativeLay);
        Snackbar snackbar = Snackbar.make(view, "Savjet : dodajte prijatelje", 1500);
        snackbar.show();
        setUpBottomNavigationView();
        mUserSearch = findViewById(R.id.etSearchUser);
        tinyDB = new TinyDB(SearchUserActivity.this);
        badgeHelper = new BadgeHelperClass(tinyDB, mContext, usersRef, bottomNavigationViewEx);

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

    @Override
    protected void onStart() {
        super.onStart();
        setQuery();
    }

    private void setUpBottomNavigationView() {
        bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchUserActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setIcon(R.drawable.nav_search_selected);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setUpAdapter() {
        mContext = SearchUserActivity.this;
        mAdapter = new SearchUserRecyclerViewAdapter(mContext, userList);
        RecyclerView recyclerView = findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void getUsers() {
        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    User user = dc.toObject(User.class);
                    if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                        userList.add(user);
                    }
                }
                setUpAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
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
        try {
            mAdapter.filterList(filteredList);
        } catch (NullPointerException e) {
        }
    }

    public void setQuery() {
        tinyDB = new TinyDB(SearchUserActivity.this);
        Query query = usersRef.document(tinyDB.getString("USERDOCREF")).collection("FriendRequest").whereEqualTo("accepted", false)
                .orderBy("senderName")
                .limit(50);
        query.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    if (dc.toObject(FriendRequest.class) != null) {
                        addBadgeAt(1, bottomNavigationViewEx);

                    }
                }
            }
        });


    }

    private Badge addBadgeAt(int number, BottomNavigationViewEx navigationViewEx) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(navigationViewEx.getBottomNavigationItemView(4))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeHelper.setQueryFriendRequest(bottomNavigationViewEx);
        badgeHelper.setQueryEventRequest(bottomNavigationViewEx);
    }
}


