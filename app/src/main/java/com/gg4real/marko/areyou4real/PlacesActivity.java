package com.gg4real.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.adapter.BadgeHelperClass;
import com.gg4real.marko.areyou4real.adapter.BottomNavigationViewHelper;
import com.gg4real.marko.areyou4real.adapter.PlacesReyclerAdapter;
import com.gg4real.marko.areyou4real.adapter.SearchUserRecyclerViewAdapter;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.model.Arena;
import com.gg4real.marko.areyou4real.model.FriendRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class PlacesActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference arenasRef = db.collection("Arenas");
    private static final int ACTIVITY_NUM = 2;
    private RecyclerView mRecyclerView;
    private PlacesReyclerAdapter mAdapter;
    private LinearLayoutManager manager;
    private Context mContext = PlacesActivity.this;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private TinyDB tinyDB;
    private CollectionReference userRef = db.collection("Users");
    private ArrayList<Arena> arenaList = new ArrayList<>();
    private BadgeHelperClass badgeHelper;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_places);
        tinyDB = new TinyDB(PlacesActivity.this);

        badgeHelper = new BadgeHelperClass(tinyDB,PlacesActivity.this,userRef,bottomNavigationViewEx);
        setUpBottomNavigationView();
        getArenas();

    }

    private void setUpBottomNavigationView() {
        bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(PlacesActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setIcon(R.drawable.nav_places_selected);
        menuItem.setChecked(true);
    }

    private void setUpAdapter() {
             mAdapter = new PlacesReyclerAdapter(mContext, arenaList);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
        }


    private void getArenas() {
        arenasRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    Arena arena = dc.toObject(Arena.class);
                    arenaList.add(arena);
                    setUpAdapter();


                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                            Toast.makeText(PlacesActivity.this, "me", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setQuery() {
        tinyDB = new TinyDB(PlacesActivity.this);
        Query query = userRef.document(tinyDB.getString("USERDOCREF")).collection("FriendRequest").whereEqualTo("accepted", false)
                .orderBy("senderName")
                .limit(50);
        query.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    if (dc.toObject(FriendRequest.class) != null) {
                        addBadgeAt(1, bottomNavigationViewEx);

                    }
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        setQuery();
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeHelper.setQueryFriendRequest(bottomNavigationViewEx);
        badgeHelper.setQueryEventRequest(bottomNavigationViewEx);
    }
}
