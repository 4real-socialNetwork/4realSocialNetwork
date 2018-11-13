package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.InsideGroup;
import com.example.marko.areyou4real.OtherUserProfile;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.UserProfile;

import java.util.ArrayList;

public class SearchUserRecyclerViewAdapter extends RecyclerView.Adapter<SearchUserRecyclerViewAdapter.ViewHolder> implements Filterable {
    Context mContext;
    ArrayList<User> userList;
    ArrayList<User> userListFull;

    public SearchUserRecyclerViewAdapter(Context mContext,ArrayList<User> userList) {
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
    public void onBindViewHolder(@NonNull final SearchUserRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.userName.setText(userList.get(position).getName());
        holder.searchUserLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,OtherUserProfile.class);
                intent.putExtra("userId",userList.get(position).getUserId());
                mContext.startActivity(intent);
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

    @Override
    public Filter getFilter() {
        return filterUser;
    }

    private Filter filterUser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> filteredList = new ArrayList<>();
            if (constraint==null || constraint.length()==0){
                filteredList.addAll(userListFull);
            }else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (User user : userListFull){
                    if (user.getName().toLowerCase().contains(filteredPattern)){
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((ArrayList<User>)results.values);
            notifyDataSetChanged();
        }
    };

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
