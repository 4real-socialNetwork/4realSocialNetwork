package com.example.marko.areyou4real.LoginCreateUser;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.areyou4real.MainActivity;
import com.example.marko.areyou4real.R;
import com.example.marko.areyou4real.User;
import com.example.marko.areyou4real.dialogs.InterestDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreateUser extends AppCompatActivity {

    private static final String TAG = "CreateUser";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsersRef = db.collection("Users");

    private Context mContext = CreateUser.this;
    private Button btnInteres;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText surname;
    private EditText description;
    private AppCompatSeekBar seekBar;
    private EditText time;
    private Button btnCreateAccount;
    private ProgressBar progressBar;
    private TextView showSeekBar;
    private ArrayList<String> selectedItems = new ArrayList<>();

    private int current_range = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        setUpToolbar();
        btnInteres = findViewById(R.id.btnInteres);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        description = findViewById(R.id.etDescripiton);
        seekBar = findViewById(R.id.seekBar);
        time = findViewById(R.id.etTime);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        progressBar = findViewById(R.id.progressBarUser);
        showSeekBar = findViewById(R.id.tvSeekBarShower);

        showSeekBar.setText(current_range + " km");
        seekBar.setProgress(current_range);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showSeekBar.setText(progress + " km");
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createUserAndAccount();
            }
        });
        btnInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    private void createUserAndAccount() {

        if (!validateForm()) {
            return;
        }

        final String mail = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String ime = name.getText().toString().trim();
        final String prezime = surname.getText().toString().trim();
        final String opis = description.getText().toString().trim();
        final int udaljenost = seekBar.getProgress();
        final int vrijeme = Integer.parseInt(time.getText().toString());


        if (mail.contentEquals("@") || pass.length() > 6) {
            mAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            User user = new User(FirebaseAuth.getInstance().getUid(), ime, prezime, mail, opis, selectedItems, udaljenost, 14, vrijeme);
                            mUsersRef.add(user);

                            Toast.makeText(CreateUser.this, "Račun kreiran", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateUser.this, "Već postoji korisnik s tim računom", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            Toast.makeText(this, "Email ili šifa nisu ispravni", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Obavezno ispuniti");
            result = false;
            btnCreateAccount.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            email.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            password.setError(null);
        }
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            name.setError(null);
        }
        if (TextUtils.isEmpty(surname.getText().toString())) {
            surname.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            surname.setError(null);
        }
        if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            description.setError(null);
        }
        if (TextUtils.isEmpty(time.getText().toString())) {
            time.setError("Obavezno ispuniti");
            result = false;
            progressBar.setVisibility(View.INVISIBLE);


        } else {
            time.setError(null);
        }

        return result;
    }

    public void openDialog() {
        InterestDialog dialog = new InterestDialog();
        dialog.show(getFragmentManager(), "ExampleDialog");
    }

    public void setItems(ArrayList<String> items) {
        selectedItems.addAll(items);
        Toast.makeText(mContext, selectedItems.size() + " ", Toast.LENGTH_SHORT).show();

    }
    private void setUpToolbar (){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
