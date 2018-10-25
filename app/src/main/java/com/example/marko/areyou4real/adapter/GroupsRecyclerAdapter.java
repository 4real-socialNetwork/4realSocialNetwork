package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marko.areyou4real.InsideGroup;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.Group;

import java.util.ArrayList;

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsRecyclerAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<Group> grupsList;

    public GroupsRecyclerAdapter(Context mcontext) {
        this.mContext = mcontext;
        this.grupsList = new ArrayList<>();
    }


    @NonNull
    @Override
    public GroupsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_layout_item, parent, false);
        return new MyViewHolder(view);
    }
    public void addGroup(Group group){
        grupsList.add(group);
        notifyItemInserted(getItemCount() - 1);

    }

    @Override
    public void onBindViewHolder(@NonNull GroupsRecyclerAdapter.MyViewHolder holder, int position) {
        holder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InsideGroup.class);
                mContext.startActivity(intent);
            }
        });
        holder.groupName.setText(grupsList.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        return grupsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout groupItem;
        ImageView groupImage;
        TextView groupName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.groupImage = itemView.findViewById(R.id.ivGroupImage);
            this.groupItem = itemView.findViewById(R.id.groupsItemLayout);
            this.groupName = itemView.findViewById(R.id.tvGroupName);
        }
    }
    public void clearAll(){
        grupsList.clear();
    }
}