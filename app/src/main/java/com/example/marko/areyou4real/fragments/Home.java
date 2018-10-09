package com.example.marko.areyou4real.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.fragments.adapter.EventRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.cache.RemovalNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class Home extends android.support.v4.app.Fragment {
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    //private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("Events");
    private SwipeRefreshLayout swipe ;
    private ArrayList<Event> listOfEvents = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        //String id = auth.getUid();
        mContext = getContext();
        swipe = view.findViewById(R.id.swipee);
       // Toast.makeText(mContext, auth.getUid(), Toast.LENGTH_SHORT).show();


        mRecycleView = view.findViewById(R.id.homeRecyclerView);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new EventRecyclerAdapter(getContext(), listOfEvents);
        mRecycleView.setAdapter(mAdapter);

        loadEvents();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);

                        int min = 15;
                        int max = 25;
                        Random random = new Random();
                        int i = random.nextInt(max-min+1)+min;
                        me(getView());
                    }
                },1000);
            }
        });




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        me(getView());

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void loadEvents() {
        eventsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Event event = document.toObject(Event.class);
                            listOfEvents.add(event);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void me (View view) {
        mRecycleView = view.findViewById(R.id.homeRecyclerView);
        mRecycleView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new EventRecyclerAdapter(getContext(), listOfEvents);
        mRecycleView.setAdapter(mAdapter);

    }
    public void setUserId(){

    }

}
