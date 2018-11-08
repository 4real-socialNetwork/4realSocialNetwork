package com.example.marko.areyou4real;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.marko.areyou4real.LoginCreateUser.LoginActivity;
import com.example.marko.areyou4real.adapter.TinyDB;
import com.example.marko.areyou4real.fragments.CreateEvent;
import com.example.marko.areyou4real.fragments.GroupsFragment;
import com.example.marko.areyou4real.fragments.Home;
import com.example.marko.areyou4real.adapter.SectionPagerAdapter;
import com.example.marko.areyou4real.model.Event;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private SectionPagerAdapter mPageAdapter;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_PERMISSION_RESULT = 1234;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient client;
    private double userLat;
    private double userLng;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colRef = db.collection("Users");
    private String userDocumentId = "";
    private String serverKey ="";
    private TinyDB tinyDB ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        getLocationPermission();
        getUserDocumentId();
        getServerKey();


        mPageAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task != null) {
                    if (task.getResult() == null) {
                        getLastKnownLocation();
                    } else {
                        userLat = task.getResult().getLatitude();
                        userLng = task.getResult().getLongitude();
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profileMenu) {
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "going to UserProfile", Toast.LENGTH_SHORT).show();

            return false;
        } else if (item.getItemId() == R.id.myEvents) {
            Intent intent = new Intent(MainActivity.this, CreateEvent.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuSignOut) {
            signOut();
        }


        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), "Događaji");
        adapter.addFragment(new GroupsFragment(), "Vaše grupe");
        viewPager.setAdapter(adapter);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, "odjavljivanje", Toast.LENGTH_SHORT).show();
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION,
                COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                getLastKnownLocation();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_RESULT);
            }
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_RESULT);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_REQUEST_PERMISSION_RESULT: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void getUserDocumentId() {
        colRef.whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots) {
                    userDocumentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                    updateUserLocation();

                }
            }
        });

    }

    private void updateUserLocation() {
        colRef.document(userDocumentId).update("userLat", userLat, "userLong", userLng).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: task complete");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getLastKnownLocation();
    }

    private void getAllUserTokens(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventsRef = db.collection("Events");
        Query query = eventsRef.whereArrayContains("listOfUsersParticipatingInEvent",FirebaseAuth.getInstance().getUid());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots){
                    Event event = dc.toObject(Event.class);

                }
            }
        });
    }
    private void getServerKey(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference server = db.collection("Server");
        server.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dc : queryDocumentSnapshots){
                     serverKey = dc.toString();
                     tinyDB =  new TinyDB(MainActivity.this);
                     tinyDB.putString("SERVERKEY",serverKey);
                }
            }
        });
    }
}