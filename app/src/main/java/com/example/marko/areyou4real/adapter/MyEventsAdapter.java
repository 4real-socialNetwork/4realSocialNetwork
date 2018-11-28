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

    private void photoCheck(@NonNull MyEventsAdapter.MyEventsHolder holder, int position, @NonNull Event model) {
        String activity = model.getActivity();
        int random = model.getPictureNumber();
        switch (activity) {
            case "Nogomet":
                switch (random) {
                    case 1:
                        GlideApp.with(mContext).load(R.drawable.nogomet_first).circleCrop().into(holder.eventImage);
                        break;
                    case 2:
                        GlideApp.with(mContext).load(R.drawable.nogomet_two).circleCrop().into(holder.eventImage);
                        break;
                    case 3:
                        GlideApp.with(mContext).load(R.drawable.nogomet_three).circleCrop().into(holder.eventImage);
                        break;
                    case 4:
                        GlideApp.with(mContext).load(R.drawable.nogomet_four).circleCrop().into(holder.eventImage);
                        break;
                    case 5:
                        GlideApp.with(mContext).load(R.drawable.nogomet_five).circleCrop().into(holder.eventImage);
                        break;
                }
                break;
            case "Košarka":
                switch (random) {
                    case 1:
                        GlideApp.with(mContext).load(R.drawable.kosarka_one).circleCrop().into(holder.eventImage);
                        break;
                    case 2:
                        GlideApp.with(mContext).load(R.drawable.kosarka_two).circleCrop().into(holder.eventImage);
                        break;
                    case 3:
                        GlideApp.with(mContext).load(R.drawable.kosarka_three).circleCrop().into(holder.eventImage);
                        break;
                    case 4:
                        GlideApp.with(mContext).load(R.drawable.kosarka_four).circleCrop().into(holder.eventImage);
                        break;
                    case 5:
                        GlideApp.with(mContext).load(R.drawable.kosarka_five).circleCrop().into(holder.eventImage);
                        break;
                }
                break;

            case "Šah":
                switch (random) {
                    case 1:
                        GlideApp.with(mContext).load(R.drawable.sah_one).circleCrop().into(holder.eventImage);
                        break;
                    case 2:
                        GlideApp.with(mContext).load(R.drawable.sah_two).circleCrop().into(holder.eventImage);
                        break;
                    case 3:
                        GlideApp.with(mContext).load(R.drawable.sah_three).circleCrop().into(holder.eventImage);
                        break;
                    case 4:
                        GlideApp.with(mContext).load(R.drawable.sah_four).circleCrop().into(holder.eventImage);
                        break;
                    case 5:
                        GlideApp.with(mContext).load(R.drawable.sah_five).circleCrop().into(holder.eventImage);
                        break;
                }
                break;
            case "Društvene igre":
                switch (random) {
                    case 1:
                        GlideApp.with(mContext).load(R.drawable.drustvene_igre_one).circleCrop().into(holder.eventImage);
                        break;
                    case 2:
                        GlideApp.with(mContext).load(R.drawable.drustvene_igre_two).circleCrop().into(holder.eventImage);
                        break;
                    case 3:
                        GlideApp.with(mContext).load(R.drawable.drustvene_igre_three).circleCrop().into(holder.eventImage);
                        break;
                    case 4:
                        GlideApp.with(mContext).load(R.drawable.drustvene_igre_four).circleCrop().into(holder.eventImage);
                        break;
                    case 5:
                        GlideApp.with(mContext).load(R.drawable.drustvene_igre_five).circleCrop().into(holder.eventImage);
                        break;
                }
                break;
            case "Druženje":
                switch (random) {
                    case 1:
                        GlideApp.with(mContext).load(R.drawable.druzenje_one).circleCrop().into(holder.eventImage);
                        break;
                    case 2:
                        GlideApp.with(mContext).load(R.drawable.druzenje_two).circleCrop().into(holder.eventImage);
                        break;
                    case 3:
                        GlideApp.with(mContext).load(R.drawable.druzenje_three).circleCrop().into(holder.eventImage);
                        break;
                    case 4:
                        GlideApp.with(mContext).load(R.drawable.druzenje_four).circleCrop().into(holder.eventImage);
                        break;
                    case 5:
                        GlideApp.with(mContext).load(R.drawable.druzenje_five).circleCrop().into(holder.eventImage);
                        break;
                }
                break;
        }
    }


    @Override
    protected void onBindViewHolder(@NonNull MyEventsAdapter.MyEventsHolder holder, int position, @NonNull Event model) {
        final String eventId = model.getEventId();
        holder.eventName.setText(model.getName());
        photoCheck(holder,position,model);
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
            this.eventName = itemView.findViewById(R.id.tvEventName);
            this.myEventsLayout = itemView.findViewById(R.id.myEventsLayout);
            this.eventImage = itemView.findViewById(R.id.ivEventPicture);
        }
    }
}
