package com.example.coingecko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://coingeckowatchlists-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText rPassword = findViewById(R.id.rPassword);

        final Button signUpBtn = findViewById(R.id.btn_signup);
        final Button loginBtn = findViewById(R.id.goToLogin);
        Log.i("Signup", "create");

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_txt = email.getText().toString();
                final String password_txt = password.getText().toString();
                final String rPassword_txt =  rPassword.getText().toString();
                Log.i("SignUpActivity", "what is going on");

                if (email_txt.isEmpty() || password_txt.isEmpty() || rPassword_txt.isEmpty()) {
                    Log.i("SignUpActivity", "Empty");
                    Toast.makeText(SignUpActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else if (!password_txt.equals(rPassword_txt)) {
                    Log.i("SignUpActivity", "Huh");
                    Toast.makeText(SignUpActivity.this, "Ensure passwords match", Toast.LENGTH_SHORT).show();
                } else {
                    dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(email_txt)) {
                                Log.i("SignUpActivity", "Huhu");
                                Toast.makeText(SignUpActivity.this, "User with this email exists already", Toast.LENGTH_SHORT).show();
                            } else {
                                dbRef.child("users").child(email_txt).child("pw").setValue(password_txt);
                                dbRef.child("users").child(email_txt).child("coins").push().setValue("bitcoin");
                                Log.i("SignUpActivity", "Should be here");
                                Toast.makeText(SignUpActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SignUpActivity", "OnClick");
                finish();
            }
        });

    }
}