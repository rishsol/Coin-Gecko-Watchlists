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

public class LoginActivity extends AppCompatActivity {

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://coingeckowatchlists-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.emailLogin);
        final EditText password = findViewById(R.id.passwordLogin);
        final Button login = findViewById(R.id.btn_login);
        final Button signUp = findViewById(R.id.goToSignUp);

        Log.i("LoginActivity", "hello");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailTxt = email.getText().toString();
                final String pwTxt = password.getText().toString();

                if (emailTxt.isEmpty() || pwTxt.isEmpty()) {
                    Log.i("LoginActivity", "Login Stuff");
                    Toast.makeText(LoginActivity.this, "Enter email + pw", Toast.LENGTH_SHORT).show();
                } else {
                    dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(emailTxt)) {
                                final String getPw = snapshot.child(emailTxt).child("pw").getValue(String.class);

                                if (getPw.equals(pwTxt)) {
                                    Log.i("LoginActivity", "correct login");
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("email", emailTxt);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Log.i("LoginActivity", "wrong pw");
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.i("LoginActivity", "can't find user");
                                Toast.makeText(LoginActivity.this, "Can't find user", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //Log.i("LoginActivity", "Login success");
                    Toast.makeText(LoginActivity.this, "Cool", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


    }
}