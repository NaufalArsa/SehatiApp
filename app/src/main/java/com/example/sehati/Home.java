package com.example.sehati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sehati.databinding.ActivityHomeBinding;
import com.example.sehati.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private TextView tv_name;

    ActivityHomeBinding binding;
    Button moveBuyMedicine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get user's name
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");

        // Get the current user
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // No user is signed in, redirect to Login
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
        }
        // Find the TextView in the layout
        tv_name = findViewById(R.id.nameUser);

        // Set the user's display name from the database
        if (currentUser != null) {
            String userId = currentUser.getUid();
            ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            String displayName = user.name;
                            tv_name.setText(displayName);
                        }
                    } else {
                        Toast.makeText(Home.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        moveBuyMedicine = findViewById(R.id.bt_movetobuyMedicine);
        moveBuyMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika button di-klik, jalankan explicit intent ke Activity tujuan
                Intent intent = new Intent(Home.this, BuyMedicine.class);
                startActivity(intent);
            }
        });


        binding.relaxMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent relax = new Intent(Home.this, MusicTherapyActivity.class);
                startActivity(relax);
            }
        });



    }
}