package com.example.marko.areyou4real.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.SearchUserActivity;
import com.example.marko.areyou4real.UserProfile;
import com.example.marko.areyou4real.NotificationsActivity;
import com.example.marko.areyou4real.PlacesActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    public static void setUpBottomNavigationView (BottomNavigationViewEx bottomNavigationView){
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.setTextVisibility(false);
    }
    public static void enableNavigation(final Context context, final BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                        break;
                    case R.id.nav_search_user:
                       Intent intent2 = new Intent(context,SearchUserActivity.class);
                       context.startActivity(intent2);
                        break;
                    case R.id.nav_places:
                        Intent intent3 = new Intent(context,PlacesActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.nav_user_profile:
                        Intent intent4 = new Intent(context,UserProfile.class);
                        context.startActivity(intent4);
                        break;
                    case R.id.nav_notification:
                        Intent intent5 = new Intent(context,NotificationsActivity.class);
                        context.startActivity(intent5);
                        break;
                }
                return true;
            }
        });

    }
}
