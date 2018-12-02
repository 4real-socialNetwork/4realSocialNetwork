package com.gg4real.marko.areyou4real;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.adapter.TextMessageAdapter;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.gg4real.marko.areyou4real.model.TextMessage;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;

public class EventChatRoom extends AppCompatActivity {
    private EditText mMessageText;
    private TextMessage textMessage;
    public TinyDB tinyDB;
    private String mUserName;
    private String mUserId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Users");
    private DocumentReference docRef;
    private User currentUser;
    private TextMessageAdapter adapter;
    private Button btnSendMessage;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_chat_room);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mMessageText = findViewById(R.id.tiEditMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        setUpRecyclerView();
        getUserInfo();
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private void getUserInfo() {
        tinyDB = new TinyDB(this);
        docRef = colRef.document(tinyDB.getString("USERDOCREF"));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);
                Toast.makeText(EventChatRoom.this, "user ready", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EventChatRoom.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setUpRecyclerView() {
        String eventId = getIntent().getStringExtra("EVENTID");

        Query query = FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .collection("chatRoom")
                .orderBy("timestamp")
                .limit(50);

        FirestoreRecyclerOptions<TextMessage> options = new FirestoreRecyclerOptions.Builder<TextMessage>()
                .setQuery(query, TextMessage.class)
                .build();

        adapter = new TextMessageAdapter(options);


        layoutManager = new LinearLayoutManager(this);


        recyclerView = findViewById(R.id.messageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //when a new message is sent, focus on that:
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount());
            }
        });

        getUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    public void sendMessage() {
        String eventId = getIntent().getStringExtra("EVENTID");
        ArrayList<String> usersInChat = getIntent().getStringArrayListExtra("USERS_IN_CHAT");
        textMessage = new TextMessage(currentUser.getName(), mMessageText.getText().toString(), currentUser.getUserId(), eventId, "", System.currentTimeMillis(),usersInChat);
        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .collection("chatRoom").add(textMessage);

        mMessageText.setText("");


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}