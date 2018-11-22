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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.InsideEvent;
import com.example.marko.areyou4real.OtherUserProfile;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.FriendRequest;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyFriendRequestRecyclerAdapter extends FirestoreRecyclerAdapter<FriendRequest, MyFriendRequestRecyclerAdapter.MyEventsHolder> {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");


    public MyFriendRequestRecyclerAdapter(@NonNull FirestoreRecyclerOptions<FriendRequest> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyFriendRequestRecyclerAdapter.MyEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item, parent, false);
        return new MyEventsHolder(view);

    }

    @Override
    protected void onBindViewHolder(@NonNull final MyEventsHolder holder, int position, @NonNull final FriendRequest model) {
        final TinyDB tinyDB = new TinyDB(mContext);
        final String userDocRef = tinyDB.getString("USERDOCREF");
        holder.senderName.setText(model.getSenderName());
        holder.senderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherUserProfile.class);
                intent.putExtra("otherUserDocRef", model.getSenderDocRef());
                mContext.startActivity(intent);
            }
        });
        holder.btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> friendsList = new ArrayList<>();
                friendsList.add(model.getSenderId());
                holder.tvSituationDescription.setText("i Vi ste prijatelji");


                usersRef.document(userDocRef).update("userFriends", FieldValue.arrayUnion(model.getSenderId()));
               usersRef.document(model.getSenderDocRef()).update("userFriends", FieldValue.arrayUnion((FirebaseAuth.getInstance().getUid())));
                usersRef.document(userDocRef).collection("FriendRequest").document(model.getFriendRequestRef()).update("accepted", true);

                Toast.makeText(mContext, "Vi i " + model.getSenderName() + " ste prijatelji", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class MyEventsHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView senderName;
        RelativeLayout senderLayout;
        Button btnAllow;
        TextView tvSituationDescription;

        public MyEventsHolder(View itemView) {
            super(itemView);
            this.senderName = itemView.findViewById(R.id.senderName);
            this.senderLayout = itemView.findViewById(R.id.senderLayout);
            this.btnAllow = itemView.findViewById(R.id.btnAllow);
            this.tvSituationDescription = itemView.findViewById(R.id.tvSituationDescription);
        }
    }
}
