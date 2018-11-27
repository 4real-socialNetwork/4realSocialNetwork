package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import android.widget.Toast;

import com.example.marko.areyou4real.OtherUserProfile;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;

import java.util.ArrayList;

public class CompleteEventRecyclerAdapter extends RecyclerView.Adapter<CompleteEventRecyclerAdapter.ViewHolder> {
    Context mContext;
    ArrayList<User> userList ;
    ArrayList<String> positiveReview = new ArrayList<>();
    ArrayList<String> negativeReview= new ArrayList<>();

    public CompleteEventRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CompleteEventRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompleteEventRecyclerAdapter.ViewHolder holder, final int position) {
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
        holder.checkBoxPositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    positiveReview.add(userList.get(position).getUserDocRef());
                    holder.checkBoxNegative.setChecked(false);
                }else{
                    positiveReview.remove(userList.get(position).getUserDocRef());
                    holder.checkBoxNegative.setChecked(true);
                }
            }
        });

        holder.checkBoxNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    negativeReview.add(userList.get(position).getUserDocRef());
                    holder.checkBoxPositive.setChecked(false);
                }else{
                    negativeReview.remove(userList.get(position).getUserDocRef());
                    holder.checkBoxPositive.setChecked(true);
                }
            }
        });


    }

    public void addUser(User user) {
        if(userList.contains(user)){
            Toast.makeText(mContext, "allreadyinthere", Toast.LENGTH_SHORT).show();
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
        ConstraintLayout searchListItemConstraint;
        CheckBox checkBoxPositive;
        CheckBox checkBoxNegative;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvUserName = itemView.findViewById(R.id.tvUserName);
            this.searchListItemConstraint = itemView.findViewById(R.id.searchListItemConstraint);
            this.ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            this.checkBoxPositive = itemView.findViewById(R.id.checkBoxPositive);
            this.checkBoxNegative = itemView.findViewById(R.id.checkBoxNegative);
        }
    }

    public ArrayList<String> getPositiveReview() {
        return positiveReview;
    }

    public ArrayList<String> getNegativeReview() {
        return negativeReview;
    }


}
