package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.InsideEvent;
import com.example.marko.areyou4real.OtherUserProfile;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.EventRequest;
import com.example.marko.areyou4real.model.FriendRequest;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyEventRequestRecyclerAdapter extends FirestoreRecyclerAdapter<EventRequest, MyEventRequestRecyclerAdapter.MyEventsHolder> {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private CollectionReference eventsRef = db.collection("Events");
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());



    public MyEventRequestRecyclerAdapter(@NonNull FirestoreRecyclerOptions<EventRequest> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyEventRequestRecyclerAdapter.MyEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_request_item, parent, false);
        return new MyEventsHolder(view);

    }


    @Override
    protected void onBindViewHolder(@NonNull MyEventsHolder holder, int position, @NonNull final EventRequest model) {
        final TinyDB tinyDB = new TinyDB(mContext);
        final String userDocRef = tinyDB.getString("USERDOCREF");
        holder.senderName.setText(model.getSenderName());
        holder.senderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InsideEvent.class);
                intent.putExtra("EVENT_ID",model.getEventId() );
                mContext.startActivity(intent);
            }
        });
        holder.btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usersRef.document(userDocRef).collection("EventRequests").document(model.getEventRequestDocRef()).update("answered",true);
                eventsRef.document(model.getEventId()).update("listOfUsersParticipatingInEvent",FieldValue.arrayUnion(FirebaseAuth.getInstance().getUid()));


                    Toast.makeText(mContext, "Pridružili ste se događaju : "+model.getEventActivity(), Toast.LENGTH_SHORT).show();

            }
        });
        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.document(userDocRef).collection("EventRequests").document(model.getEventId()).update("isAnswered",true);
            }
        });
        holder.tvEventName.setText(model.getEventActivity());
        holder.tvEventTime.setText(sdf.format(model.getEventTime()));
    }

    public class MyEventsHolder extends RecyclerView.ViewHolder {
        TextView senderName;
        RelativeLayout senderLayout;
        Button btnAllow;
        Button btnDecline;
        TextView tvEventName;
        TextView tvEventTime;

        public MyEventsHolder(View itemView) {
            super(itemView);
            this.senderName = itemView.findViewById(R.id.tvSenderName);
            this.senderLayout = itemView.findViewById(R.id.senderLayout);
            this.btnAllow = itemView.findViewById(R.id.btnAllow);
            this.btnDecline = itemView.findViewById(R.id.btnDecline);
            this.tvEventName = itemView.findViewById(R.id.tvEventName);
            this.tvEventTime = itemView.findViewById(R.id.tvTimeOfEvent);
        }
    }
}
