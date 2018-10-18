package com.example.marko.areyou4real;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateUser extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private Context mContext = CreateUser.this;

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText surname;
    private EditText description;
    private SeekBar range;
    private EditText time;
    private Button btnCreateAccount;
    private ProgressBar progressBar;
    private TextView rangeShower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        description = findViewById(R.id.etDescripiton);
        range = findViewById(R.id.rangeBar);
        time = findViewById(R.id.etTime);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        progressBar = findViewById(R.id.progressBarUser);
        rangeShower = findViewById(R.id.tvSeekBarShower);
        rangeShower.setText(("" + progressBar.getProgress() + " km"));


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                createUserAndAccount();
            }
        });

    }


    private void createUserAndAccount() {
        final String mail = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String ime = name.getText().toString().trim();
        final String prezime = surname.getText().toString().trim();
        final String opis = description.getText().toString().trim();
        final int udaljenost = range.getProgress();
        final int vrijeme = Integer.parseInt(time.getText().toString());

        if (mail.contentEquals("@") || pass.length() > 6) {
            auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            User user = new User(authResult.getUser().getUid(), ime, prezime, mail, opis, udaljenost, vrijeme, 24);
                            usersRef.add(user);


                            Toast.makeText(CreateUser.this, "Acc created", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateUser.this, "That account allready exists", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            Toast.makeText(this, "Password or email not valid", Toast.LENGTH_SHORT).show();
        }

    }


}
