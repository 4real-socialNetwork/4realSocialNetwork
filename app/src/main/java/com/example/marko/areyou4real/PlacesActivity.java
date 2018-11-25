package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marko.areyou4real.adapter.BottomNavigationViewHelper;
import com.example.marko.areyou4real.adapter.PlacesReyclerAdapter;
import com.example.marko.areyou4real.model.Place;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class PlacesActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 2;
    private RecyclerView recyclerView;
    private PlacesReyclerAdapter adapter;
    private LinearLayoutManager manager;
    private Context mContext = PlacesActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_places);
        setUpBottomNavigationView();
        setUpAdapter();
    }

    private void setUpBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(PlacesActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setIcon(R.drawable.nav_places_selected);
        menuItem.setChecked(true);
    }

    private void setUpAdapter() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(mContext);
        adapter = new PlacesReyclerAdapter(mContext);
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        adapter.addItem(new Place("SD Martinovka", "Stjepana Radića 23", null, 3881654, 150.00, "Marinovka je kul"));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
