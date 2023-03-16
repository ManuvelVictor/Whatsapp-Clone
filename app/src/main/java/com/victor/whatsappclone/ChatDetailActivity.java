package com.victor.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.victor.whatsappclone.databinding.ActivityChatDetailBinding;

import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Getting the user data's from the intent
        final String senderId = mAuth.getUid();
        String receivedId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        //Setting the userName and profilePic
        binding.chatDetailUsername.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar3).into(binding.chatDetailProfileImage);

        binding.chatDetailBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}