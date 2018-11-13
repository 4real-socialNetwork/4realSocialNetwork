package com.example.marko.areyou4real.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.marko.areyou4real.CreateEvent;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.adapter.EventRecyclerAdapter;
import com.example.marko.areyou4real.adapter.MyEventsAdapter;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.service.MyFirebaseMessagingService;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class HomeFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecycleView;
    private EventRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private CollectionReference eventsRef = db.collection("Events");
    private CollectionReference userRef = db.collection("Users");
    private SwipeRefreshLayout swipe;
    private FloatingActionButton fab;
    private ArrayList<String> interests = new ArrayList<>();
    private ArrayList<Event> eventsList = new ArrayList<>();
    private double userLat;
    private double userLng;
    private double userRange;
    String userDocId = "";
    TinyDB tinyDB;
    private MyEventsAdapter myEventsAdapter;
    private MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
    String userToken = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_layout, container, false);
        mContext = getContext();
        swipe = view.findViewById(R.id.swipee);
        fab = view.findViewById(R.id.fab);

        setUpMyEventsRecyclerView(view);
        setInterests(view);
        setUpAdapter(view);



        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        mRecycleView.setLayoutAnimation(animation);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateEvent.class);
                startActivity(intent);
            }
        });


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                        setInterests(view);
                        runLayoutAnimation(mRecycleView);

                    }
                }, 1000);
            }
        });


        return view;
    }


    public void loadEvents(final View view) {
        for (String item : interests) {
            eventsRef.whereEqualTo("activity", item)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot dc : task.getResult()) {
                        Event event = dc.toObject(Event.class);
                        if (distance(userLat, userLng, event.getEventLat(), event.getEventLng(), 'K') <= userRange) {
                            eventsList.add(event);
                        }
                        setUpAdapter(view);

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


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    private void setInterests(final View view) {
        interests.clear();
        userRef.whereEqualTo("userId", userId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    userDocId = dc.getId();
                    User user = dc.toObject(User.class);
                    userLat = user.getUserLat();
                    userLng = user.getUserLong();
                    userRange = user.getRange();
                    userToken = user.getUserToken();
                    ArrayList<String> list = new ArrayList<>(user.getInterests());
                    interests.addAll(list);
                    tinyDB = new TinyDB(getContext());
                    tinyDB.putString("USERDOCREF", userDocId);
                    tinyDB.putString("USERTOKEN", userToken);
                }
                loadEvents(view);
                //updateUserToken();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void setUpMyEventsRecyclerView(View view) {
        Query query = eventsRef.whereArrayContains("listOfUsersParticipatingInEvent", userId)
                .orderBy("name", Query.Direction.DESCENDING)
                .limit(50);

        FirestoreRecyclerOptions<Event> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<Event>()
                .setQuery(query, Event.class)
                .build();

        myEventsAdapter = new MyEventsAdapter(firestoreRecyclerOptions, mContext);

        RecyclerView recyclerView = view.findViewById(R.id.myEventsRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myEventsAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        myEventsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myEventsAdapter.stopListening();
    }

    public void setUpAdapter(View view){
        mRecycleView = view.findViewById(R.id.homeRecyclerView);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new EventRecyclerAdapter(mContext,eventsList);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(mLayoutManager);


        mAdapter.notifyDataSetChanged();

    }



}
