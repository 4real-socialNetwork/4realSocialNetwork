package com.example.marko.areyou4real;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

public class CreateGroup extends AppCompatActivity {

    private EditText etSearchUsers;
    private EditText etGroupName;
    private RecyclerView searchRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        etGroupName = findViewById(R.id.etGroupName);
        etSearchUsers = findViewById(R.id.etAddUsers);
        searchRecycler = findViewById(R.id.searchRecyclerView);


    }
}
