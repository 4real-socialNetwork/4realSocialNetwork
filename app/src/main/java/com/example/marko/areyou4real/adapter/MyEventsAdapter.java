package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marko.areyou4real.InsideEvent;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.Event;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyEventsAdapter extends FirestoreRecyclerAdapter<Event, MyEventsAdapter.MyEventsHolder> {
        private Context mContext;

    public MyEventsAdapter(@NonNull FirestoreRecyclerOptions<Event> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyEventsAdapter.MyEventsHolder holder, int position, @NonNull Event model) {
        final String eventId = model.getEventId();
        holder.eventName.setText(model.getName());
        holder.myEventsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,InsideEvent.class);
                intent.putExtra("EVENT_ID",eventId);
                mContext.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public MyEventsAdapter.MyEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_events_item, parent, false);
        return new MyEventsHolder(view);

    }

    public class MyEventsHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName;
        LinearLayout myEventsLayout;

        public MyEventsHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.tvEventName);
            myEventsLayout = itemView.findViewById(R.id.myEventsLayout);
        }
    }
}
