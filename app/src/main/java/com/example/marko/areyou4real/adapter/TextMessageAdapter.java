package com.example.marko.areyou4real.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.model.TextMessage;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TextMessageAdapter extends FirestoreRecyclerAdapter<TextMessage, TextMessageAdapter.TextMessageHolder> {

    private SimpleDateFormat sdf;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TextMessageAdapter(@NonNull FirestoreRecyclerOptions<TextMessage> options) {
        super(options);
        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }


    @Override
    protected void onBindViewHolder(@NonNull TextMessageHolder holder, int position, @NonNull TextMessage model) {
        try{
            holder.mSenderName.setText(model.getName());
            holder.mSenderMessage.setText(model.getMessage());
            holder.mSenderTime.setText(sdf.format(model.getTimestamp()));
        }catch (NullPointerException e){
        }

    }

    @NonNull
    @Override
    public TextMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_message_item,parent,false);

        return new TextMessageHolder(view);
    }

    class TextMessageHolder extends RecyclerView.ViewHolder {
        TextView mSenderName;
        TextView mSenderMessage;
        TextView mSenderTime;

        public TextMessageHolder(View itemView) {
            super(itemView);
            mSenderName = itemView.findViewById(R.id.tvSenderName);
            mSenderMessage = itemView.findViewById(R.id.tvTextMessage);
            mSenderTime = itemView.findViewById(R.id.tvTimeOfMessage);

        }
    }
}
