package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.InsideEvent;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.model.Place;

import java.util.ArrayList;

public class PlacesReyclerAdapter extends RecyclerView.Adapter<PlacesReyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Place> placesList;


    public PlacesReyclerAdapter(Context mContext) {
        this.placesList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void addItem(Place place) {
        this.placesList.add(place);
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
        String name = placesList.get(position).getmPlaceName();
        String location = placesList.get(position).getmPlaceAdress()+"";
        String price = placesList.get(position).getmPlacePrice()+"";
        holder.mPlaceName.setText(name);
        holder.mPlacePrice.setText(location);
        holder.mPlaceLocation.setText(price);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "not yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mPlaceName;
        TextView mPlacePrice;
        TextView mPlaceLocation;
        LinearLayout mLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mPlaceName = itemView.findViewById(R.id.tvPlace_name);
            this.mPlacePrice = itemView.findViewById(R.id.tvPlace_price);
            this.mPlaceLocation = itemView.findViewById(R.id.tvPlace_location);
            this.mLayout = itemView.findViewById(R.id.mPlaceLayout);

        }
    }
    public void clearAll(){
        placesList.clear();
    }


}
