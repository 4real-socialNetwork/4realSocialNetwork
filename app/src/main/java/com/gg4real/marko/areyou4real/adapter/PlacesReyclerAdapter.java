package com.gg4real.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gg4real.marko.areyou4real.model.Arena;
import com.gg4real.marko.areyou4real.R;

import java.util.ArrayList;

public class PlacesReyclerAdapter extends RecyclerView.Adapter<PlacesReyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Arena> mItems;

    public PlacesReyclerAdapter(Context mContext, ArrayList<Arena> mItems){
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.arena_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        Arena arena = mItems.get(i);
        Glide.with(mContext)
                .asBitmap()
                .load(mItems.get(i).getImageResourceId())
                .into(holder.itemImage);

        holder.name.setText(arena.getArenaName());
        holder.description.setText(arena.getArenaDescription());
        holder.phone.setText(arena.getArenaPhoneNumber());
        holder.price.setText(arena.getArenaPrice());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private TextView name, description, phone, price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.image_card);
            name = itemView.findViewById(R.id.txt_name);
            description = itemView.findViewById(R.id.txt_description);
            phone = itemView.findViewById(R.id.txt_phone);
            price = itemView.findViewById(R.id.txt_price);
        }
    }

}
