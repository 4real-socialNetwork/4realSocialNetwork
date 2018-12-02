package com.gg4real.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.InsideGroup;
import com.gg4real.marko.areyou4real.R;
import com.gg4real.marko.areyou4real.model.Group;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyGroupsFirebaseRecylcerAdapter extends FirestoreRecyclerAdapter<Group, MyGroupsFirebaseRecylcerAdapter.MyGroupsHolder> {
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private CollectionReference groupsRef = db.collection("Groups");
    private ArrayList<String> usersInGroup = new ArrayList<>();
    private ArrayList<String> groupNames = new ArrayList<>();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyGroupsFirebaseRecylcerAdapter(@NonNull FirestoreRecyclerOptions<Group> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }


    public ArrayList<String> getUsersInGroup() {
        return usersInGroup;

    }

    @Override
    protected void onBindViewHolder(@NonNull final MyGroupsFirebaseRecylcerAdapter.MyGroupsHolder holder, int position, @NonNull final Group model) {
    holder.mGroupName.setText(model.getGroupName());

    holder.mCheckBox.setChecked(false);
    holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                for(String userId : model.getListOfUsersInGroup()){
                    usersInGroup.add(userId);

                }
                groupNames.add(model.getGroupName());
            }else {
                for(String userId : model.getListOfUsersInGroup()){
                    try{
                        usersInGroup.remove(userId);
                    }catch (NullPointerException e){
                        Toast.makeText(mContext, "empty", Toast.LENGTH_SHORT).show();
                    }
                }
                groupNames.remove(model.getGroupName());
            }
            
        }
    });
    holder.mLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,InsideGroup.class);
            intent.putExtra("GROUP_ID",model.getGroupId());
            mContext.startActivity(intent);
        }
    });
    }

    @NonNull
    @Override
    public MyGroupsFirebaseRecylcerAdapter.MyGroupsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,parent,false);

        return new MyGroupsHolder(view);
    }

    public class MyGroupsHolder extends RecyclerView.ViewHolder{
        TextView mGroupName;
        CheckBox mCheckBox;
        RelativeLayout mLayout;
        public MyGroupsHolder(View itemView) {
            super(itemView);
            this.mGroupName = itemView.findViewById(R.id.tvGroupName);
            this.mCheckBox = itemView.findViewById(R.id.groupCheckBox);
            this.mLayout = itemView.findViewById(R.id.groupItemLayout);
        }
    }
    public ArrayList<String> getGroupNames(){
        return groupNames;
    }
}
