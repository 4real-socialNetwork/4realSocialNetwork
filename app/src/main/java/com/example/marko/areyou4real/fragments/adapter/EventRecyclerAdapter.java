package com.example.marko.areyou4real.fragments.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.fragments.Event;
import com.example.marko.areyou4real.fragments.InsideEvent;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Event> eventList;


    public EventRecyclerAdapter(ArrayList<Event> eventList, Context mContext) {
        this.eventList = eventList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EventRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.home_event_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapter.MyViewHolder holder, int position) {
        String name = eventList.get(position).getName();
        String place = eventList.get(position).getEventDescription();
        final String eventId = eventList.get(position).getEventId();
        holder.sport.setText(name);
        holder.place.setText(place);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InsideEvent.class);
                intent.putExtra("EVENT_ID",eventId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView sport;
        TextView place;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.place = itemView.findViewById(R.id.tvEventName);
            this.relativeLayout = itemView.findViewById(R.id.home_event_item);
            this.sport = itemView.findViewById(R.id.tvPlace);

        }
    }


}
