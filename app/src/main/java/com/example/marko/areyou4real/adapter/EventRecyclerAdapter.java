package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.InsideEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Event> eventList;
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm",Locale.getDefault());


    public EventRecyclerAdapter(Context mContext,ArrayList<Event>eventList) {
        this.eventList = eventList;
        this.mContext = mContext;
    }

    public void addItem(Event event) {
        this.eventList.add(event);
        notifyItemInserted(getItemCount() - 1);
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
        String name = eventList.get(position).getActivity();
        String place = "Zagreb";
        String usersNeeded = eventList.get(position).getUsersNeeded()+"";
        String timeOfEvent = sdf.format(eventList.get(position).getEventStart());
        final String eventId = eventList.get(position).getEventId();
        holder.sport.setText(name);
        holder.place.setText(place);
        holder.usersNeeded.setText(usersNeeded);
        holder.eventTime.setText(timeOfEvent);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InsideEvent.class);
                intent.putExtra("EVENT_ID", eventId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout relativeLayout;
        TextView sport;
        TextView place;
        TextView usersNeeded;
        TextView eventTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.place = itemView.findViewById(R.id.tvEventName);
            this.relativeLayout = itemView.findViewById(R.id.home_event_item);
            this.sport = itemView.findViewById(R.id.tvPlace);
            this.usersNeeded = itemView.findViewById(R.id.tvPlayersNeeded);
            this.eventTime = itemView.findViewById(R.id.tvEventStart);

        }
    }
    public void clearAll(){
        eventList.clear();
    }


}
