package com.gg4real.marko.areyou4real.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.MainActivity;
import com.gg4real.marko.areyou4real.model.EventRequest;
import com.gg4real.marko.areyou4real.model.FriendRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import javax.annotation.Nullable;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class BadgeHelperClass {

    TinyDB tinyDB;
    Context mContext;
    CollectionReference userRef;
    BottomNavigationViewEx bottomNavigationViewEx;


    public BadgeHelperClass(TinyDB tinyDB, Context mContext, CollectionReference userRef, BottomNavigationViewEx bottomNavigationViewEx) {
        this.tinyDB = tinyDB;
        this.mContext = mContext;
        this.userRef = userRef;
        this.bottomNavigationViewEx = bottomNavigationViewEx;
    }

    public void setQueryFriendRequest(final BottomNavigationViewEx bottomNavigationViewEx) {
        tinyDB = new TinyDB(mContext);
        Query query = userRef.document(tinyDB.getString("USERDOCREF")).collection("FriendRequest").whereEqualTo("accepted", false)
                .orderBy("requestTimeInMili")
                .limit(50);
        query.addSnapshotListener((Activity) mContext,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    for(DocumentSnapshot dc : queryDocumentSnapshots){
                        if(dc.toObject(FriendRequest.class)!=null){
                            addBadgeAt(1, bottomNavigationViewEx);

                        }
                    }
                }

            }
        });


    }

    public void setQueryEventRequest(final BottomNavigationViewEx bottomNavigationViewEx) {
        tinyDB = new TinyDB(mContext);
        Query query = userRef.document(tinyDB.getString("USERDOCREF")).collection("EventRequests").whereEqualTo("answered", false)
                .orderBy("requestTimeInMili")
                .limit(50);
        query.addSnapshotListener((Activity) mContext,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    for(DocumentSnapshot dc : queryDocumentSnapshots){
                        if(dc.toObject(EventRequest.class)!=null){
                            addBadgeAt(1, bottomNavigationViewEx);

                        }
                    }
                }

            }
        });


    }


    private Badge addBadgeAt(int number, BottomNavigationViewEx navigationViewEx) {
        // add badge
        return new QBadgeView(mContext)
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
}
