package com.example.marko.areyou4real.fragments.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;

import java.util.ArrayList;

public class SearchUserRecyclerViewAdapter extends RecyclerView.Adapter<SearchUserRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    ArrayList<User> userList;

    public SearchUserRecyclerViewAdapter(Context mContext,ArrayList<User>userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public SearchUserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.userName.setText(userList.get(position).getName());
        holder.searchUserLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "not yet motherfucker", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addUser(User user) {
        this.userList.add(user);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        LinearLayout searchUserLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.tvUserName);
            this.searchUserLinearLayout = itemView.findViewById(R.id.searchListItemRLinearLayout);
        }
    }

    public void filterList(ArrayList<User> filteredList){
        userList = filteredList;
        notifyDataSetChanged();
    }
}
