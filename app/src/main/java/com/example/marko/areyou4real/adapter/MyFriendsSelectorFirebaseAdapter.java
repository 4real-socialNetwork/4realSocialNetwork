package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marko.areyou4real.InsideEvent;
import com.example.marko.areyou4real.OtherUserProfile;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.EventRequest;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class MyFriendsSelectorFirebaseAdapter extends FirestoreRecyclerAdapter<User, MyFriendsSelectorFirebaseAdapter.MyEventsHolder> {
        private Context mContext;
        private ArrayList<String> mSelectedFriends = new ArrayList<>();

    public MyFriendsSelectorFirebaseAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public MyFriendsSelectorFirebaseAdapter.MyEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_friends_selector_item, parent, false);

        return new MyEventsHolder(view);

    }



    @Override
    protected void onBindViewHolder(@NonNull MyEventsHolder holder, int position, @NonNull final User model) {
        GlideApp.with(mContext).load(model.getProfilePictureUrl()).placeholder(R.drawable.avatar).circleCrop().into(holder.mUserPicture);
        holder.mUserName.setText(model.getName());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSelectedFriends.add(model.getUserId());
                }else{
                    mSelectedFriends.remove(model.getUserId());
                }
            }
        });
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,OtherUserProfile.class);
                intent.putExtra("otherUserDocRef",model.getUserDocRef());
                mContext.startActivity(intent);
            }
        });

    }

    public class MyEventsHolder extends RecyclerView.ViewHolder {
        ImageView mUserPicture;
        TextView mUserName;
        RelativeLayout mItemLayout;
        CheckBox mCheckBox;

        public MyEventsHolder(View itemView) {
            super(itemView);
            this.mUserName = itemView.findViewById(R.id.tvUserName);
            this.mItemLayout = itemView.findViewById(R.id.searchListItemLinearLayout);
            this.mUserPicture = itemView.findViewById(R.id.ivUserProfile);
            this.mCheckBox = itemView.findViewById(R.id.mCheckbox);
        }
    }
    public ArrayList<String> getmSelectedFriends(){
        return mSelectedFriends;
    }
}
