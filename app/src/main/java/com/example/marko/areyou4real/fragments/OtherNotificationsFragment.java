package com.example.marko.areyou4real.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.adapter.EventRecyclerAdapter;
import com.example.marko.areyou4real.adapter.MyEventRequestRecyclerAdapter;
import com.example.marko.areyou4real.adapter.MyEventsAdapter;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.EventRequest;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class OtherNotificationsFragment extends Fragment {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("Events");
    private CollectionReference userRef = db.collection("Users");
    private FloatingActionButton fab;
    TinyDB tinyDB;
    private MyEventRequestRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.other_request_notification_fragment, container, false);


        mContext = getContext();
        setUpOtherNotificationsRecyclerView(view);


        return view;
    }


    private void setUpOtherNotificationsRecyclerView(View view) {
        tinyDB = new TinyDB(mContext);
        String userDocRef = tinyDB.getString("USERDOCREF");
        Query query = userRef.document(userDocRef).collection("EventRequests")
                .whereEqualTo("answered", false)
                .orderBy("eventTime", Query.Direction.DESCENDING)
                .limit(50);

        FirestoreRecyclerOptions<EventRequest> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<EventRequest>()
                .setQuery(query, EventRequest.class)
                .build();

        mAdapter = new MyEventRequestRecyclerAdapter(firestoreRecyclerOptions, mContext);

        RecyclerView recyclerView = view.findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
