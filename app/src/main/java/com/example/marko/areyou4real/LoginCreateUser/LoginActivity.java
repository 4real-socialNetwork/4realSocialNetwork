package com.example.marko.areyou4real.LoginCreateUser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignUp;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Context mContext = LoginActivity.this;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_PERMISSION_RESULT = 1234;
    private boolean mLocationPermissionGranted = false;
    private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                //prompt the dialog to update google play
                googleAPI.getErrorDialog(this,result,PLAY_SERVICES_RESOLUTION_REQUEST).show();

            }
        }
        else{
            //google play up to date
        }

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    checkIfSignedIn(user);
                }
            }
        };

        getLocationPermission();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void createUser() {
        Intent intent = new Intent(mContext, CreateUser.class);
        startActivity(intent);
    }

    private void login() {

        if (!validateForm()) {
            return;
        }

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "auth failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkIfSignedIn(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Obavezno popuniti");
            result = false;
        } else if (!isEmailValid(etEmail.getText().toString())){
            etEmail.setError("Email adresa nije valjana");
            result = false;
        }
        else {
            etEmail.setError(null);
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Obavezno popuniti");
            result = false;

        } else if(!isPasswordValid(etPassword.getText().toString())){
            etEmail.setError("Lozinka nije dovoljno duga");
            result = false;
        }
        else {
            etPassword.setError(null);
        }

        return result;
    }
    private boolean isEmailValid(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION,
                COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_RESULT);

            }
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_RESULT);
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

}
