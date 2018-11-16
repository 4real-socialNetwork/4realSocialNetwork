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
import com.example.marko.areyou4real.adapter.MyEventsAdapter;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class OtherNotificationsFragment extends Fragment {
    private RecyclerView mRecycleView;
    private EventRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private CollectionReference eventsRef = db.collection("Events");
    private CollectionReference userRef = db.collection("Users");
    private FloatingActionButton fab;
    private ArrayList<String> interests = new ArrayList<>();
    private ArrayList<Event> eventsList = new ArrayList<>();
    String userDocId = "";
    TinyDB tinyDB;
    private MyEventsAdapter myEventsAdapter;
    String userToken = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.other_request_notification_fragment, container, false);


        mContext = getContext();

      //  setUpAdapter();

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
       // mRecycleView.setLayoutAnimation(animation);




        return view;
    }


    private void setUpOtherNotificationsRecyclerView(View view) {
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
//        myEventsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
    //    myEventsAdapter.stopListening();
    }
}
