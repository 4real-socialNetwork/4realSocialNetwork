package com.example.marko.areyou4real;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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
    private FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText surname;
    private EditText description;
    private SeekBar range;
    private EditText time;
    private Button btnCreateAccount;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        description = findViewById(R.id.etDescripiton);
        range = findViewById(R.id.rangeBar);
        time = findViewById(R.id.etTime);
        btnCreateAccount = findViewById(R.id.btnCreateAcc);
        userId = user.getUid();
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                createAcc();
            }
        });

    }

    private void createAcc() {
        String mail = email.getText().toString().trim();
        String ime = name.getText().toString().trim();
        String prezime = surname.getText().toString().trim();
        String opis = description.getText().toString().trim();
        int udaljenost = range.getProgress();
        int vrijeme = Integer.parseInt(time.getText().toString());


        User user = new User(userId, ime, prezime, mail, opis, udaljenost, vrijeme, 24);
        usersRef.add(user);
    }

    private void createUser() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (mail.contentEquals("@") || pass.length() > 6) {
            auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(CreateUser.this, "Acc created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateUser.this, "That account allready exists", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Password or email not valid", Toast.LENGTH_SHORT).show();
        }

    }


}
