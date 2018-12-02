package com.gg4real.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.OtherUserProfile;
import com.gg4real.marko.areyou4real.R;
import com.gg4real.marko.areyou4real.User;

import java.util.ArrayList;

public class UsersInEventRecyclerAdapter extends RecyclerView.Adapter<UsersInEventRecyclerAdapter.ViewHolder> {
    Context mContext;
    ArrayList<User> userList ;
    ArrayList<String> positiveReview = new ArrayList<>();
    ArrayList<String> negativeReview= new ArrayList<>();

    public UsersInEventRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UsersInEventRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_in_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersInEventRecyclerAdapter.ViewHolder holder, final int position) {
        GlideApp.with(mContext)
                .load(userList.get(position).getProfilePictureUrl())
                .placeholder(R.drawable.avatar)
                .circleCrop()
                .into(holder.ivUserProfile);

        holder.tvUserName.setText(userList.get(position).getName());
        holder.searchListItemConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherUserProfile.class);
                intent.putExtra("otherUserDocRef", userList.get(position).getUserDocRef());
                mContext.startActivity(intent);
            }
        });
        holder.tvPercent.setText(userList.get(position).getPercentage()+" %");


    }

    public void addUser(User user) {
        if(userList.contains(user)){
           // Toast.makeText(mContext, "allreadyinthere", Toast.LENGTH_SHORT).show();
        }else {
            this.userList.add(user);
            notifyItemInserted(getItemCount() - 1);
        }
        

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvPercent;
        ConstraintLayout searchListItemConstraint;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvUserName = itemView.findViewById(R.id.tvUserName);
            this.tvPercent = itemView.findViewById(R.id.tvPercent);
            this.searchListItemConstraint = itemView.findViewById(R.id.searchListItemConstraint);
            this.ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
        }
    }




}
