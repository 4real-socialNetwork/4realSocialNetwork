package com.gg4real.marko.areyou4real.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gg4real.marko.areyou4real.R;
import com.gg4real.marko.areyou4real.adapter.MyFriendRequestRecyclerAdapter;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.model.FriendRequest;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class FriendRequestNotificationFragment extends Fragment {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = FirebaseAuth.getInstance().getUid();
    private CollectionReference eventsRef = db.collection("Events");
    private CollectionReference userRef = db.collection("Users");
    private ArrayList<FriendRequest> requestList = new ArrayList<>();
    String userDocId = "";
    TinyDB tinyDB;
    private MyFriendRequestRecyclerAdapter myRequestAdapter;
    String userToken = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_request_notification_fragment, container, false);


        mContext = getContext();
        setUpMyFriendRequestsRecyclerView(view);


        return view;
    }


    private void setUpMyFriendRequestsRecyclerView(View view) {
        tinyDB = new TinyDB(mContext);
        userDocId = tinyDB.getString("USERDOCREF");
        Query query = userRef.document(userDocId).collection("FriendRequest").whereEqualTo("accepted",false)
                .orderBy("senderName")
                .limit(50);

        FirestoreRecyclerOptions<FriendRequest> firestoreRecyclerOptions = new FirestoreRecyclerOptions
                .Builder<FriendRequest>()
                .setQuery(query, FriendRequest.class)
                .build();

        myRequestAdapter = new MyFriendRequestRecyclerAdapter(firestoreRecyclerOptions, mContext);

        RecyclerView recyclerView = view.findViewById(R.id.friendRequestRecyclerView);
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
