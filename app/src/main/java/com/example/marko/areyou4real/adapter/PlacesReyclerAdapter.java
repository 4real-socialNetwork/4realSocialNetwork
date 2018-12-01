package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.Arena;
import com.example.marko.areyou4real.R;

import java.util.ArrayList;

public class PlacesReyclerAdapter extends RecyclerView.Adapter<PlacesReyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Arena> placesList;


    public PlacesReyclerAdapter(Context mContext) {
        this.placesList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void addItem(Arena arena) {
        this.placesList.add(arena);
        notifyItemInserted(getItemCount() - 1);
    }

    @NonNull
    @Override
    public PlacesReyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.places_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesReyclerAdapter.MyViewHolder holder, int position) {
        holder.mPlaceName.setText(placesList.get(position).getArenaName());
        holder.mPlacePrice.setText(placesList.get(position).getArenaPrice());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "not yet", Toast.LENGTH_SHORT).show();
            }
        });
        GlideApp.with(mContext).load(R.drawable.logo).circleCrop().into(holder.placePicture);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mPlaceName;
        TextView mPlacePrice;
        LinearLayout mLayout;
        ImageView placePicture;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mPlaceName = itemView.findViewById(R.id.tvPlace_name);
            this.mPlacePrice = itemView.findViewById(R.id.tvPlace_price);
            this.mLayout = itemView.findViewById(R.id.mPlaceLayout);
            this.placePicture = itemView.findViewById(R.id.placePicture);

        }
    }
    public void clearAll(){
        placesList.clear();
    }


}
