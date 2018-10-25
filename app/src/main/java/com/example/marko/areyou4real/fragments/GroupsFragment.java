package com.example.marko.areyou4real.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.marko.areyou4real.CreateGroup;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.adapter.GroupsRecyclerAdapter;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class GroupsFragment extends android.support.v4.app.Fragment {

    private SwipeRefreshLayout swipe;
    private RecyclerView mRecyclerView;
    private GroupsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("Groups");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String userId = auth.getUid();
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groups_layout, container, false);
        swipe = view.findViewById(R.id.groupSwipe);
        mContext = getContext();
        setmAdapter(view);
        getGroups();
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroup.class);
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
                        getGroups();
                        int min = 15;
                        int max = 25;
                        Random random = new Random();
                        int i = random.nextInt(max - min + 1) + min;
                    }
                }, 1000);
            }
        });
        return view;
    }

    public void getGroups() {
        mAdapter.clearAll();
        groupsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    Group group = dc.toObject(Group.class);
                    mAdapter.addGroup(group);
                    mAdapter.notifyDataSetChanged();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setmAdapter(View view) {
        mRecyclerView = view.findViewById(R.id.container);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GroupsRecyclerAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

}
