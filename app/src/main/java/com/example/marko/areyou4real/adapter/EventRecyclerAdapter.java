package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.model.Event;
import com.example.marko.areyou4real.InsideEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Event> eventList;
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault());
    private double lat;
    private double lng;
    private double eventLat;
    private double eventLng;
    private double range;

    private void calculateRange() {
        TinyDB tinyDB = new TinyDB(mContext);
        String userDocRef = tinyDB.getString("USERDOCREF");
        FirebaseFirestore.getInstance().collection("Users").document(userDocRef).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                lat = user.getUserLat();
                lng = user.getUserLong();

                range = distance(lat, lng, eventLat, eventLng, 'K');

            }
        });
    }
    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }



    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public EventRecyclerAdapter(Context mContext, ArrayList<Event> eventList) {
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

        final String eventId = eventList.get(position).getEventId();
        eventLat = eventList.get(position).getEventLat();
        eventLng = eventList.get(position).getEventLng();
        calculateRange();
        holder.sport.setText(eventList.get(position).getActivity());
        holder.place.setText(((int)range+1)+" km");
        holder.usersNeeded.setText(eventList.get(position).getUsersNeeded() + "");
        holder.eventTime.setText(sdf.format(eventList.get(position).getEventStart()));

        GlideApp.with(mContext).load(R.drawable.football_image).circleCrop().into(holder.eventPicture);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InsideEvent.class);
                intent.putExtra("EVENT_ID", eventId);
                intent.putExtra("range",(int)range);
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
        ImageView eventPicture;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.place = itemView.findViewById(R.id.tvPlace);
            this.relativeLayout = itemView.findViewById(R.id.home_event_item);
            this.sport = itemView.findViewById(R.id.tvEventName);
            this.usersNeeded = itemView.findViewById(R.id.tvPlayersNeeded);
            this.eventTime = itemView.findViewById(R.id.tvEventStart);
            this.eventPicture = itemView.findViewById(R.id.imageView);

        }




        }


    public void clearAll() {
        eventList.clear();
    }


}
