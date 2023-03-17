package com.victor.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.whatsappclone.adapter.ChatAdapter;
import com.victor.whatsappclone.databinding.ActivityChatDetailBinding;
import com.victor.whatsappclone.model.MessageModel;

import java.util.ArrayList;
import java.util.Date;
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
        final String outgoingId = mAuth.getUid();
        String incomingId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        //Setting the userName and profilePic
        binding.chatDetailUsername.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar3).into(binding.chatDetailProfileImage);

        binding.chatDetailBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, incomingId);

        binding.chatDetailRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatDetailRecyclerView.setLayoutManager(layoutManager);

        final String outgoingRoom = outgoingId + incomingId;
        final String incomingRoom = incomingId + outgoingId;

        database.getReference().child("chats")
                        .child(outgoingRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messageModels.clear();
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            MessageModel model = snapshot1.getValue(MessageModel.class);
                                            model.setMessageId(snapshot1.getKey());
                                            messageModels.add(model);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

        binding.send.setOnClickListener(v -> {
            String message = binding.enterMessage.getText().toString();
            final MessageModel model = new MessageModel(outgoingId, message);
            model.setTimestamp(new Date().getTime());
            binding.enterMessage.setText("");

            database.getReference().child("chats")
                    .child(outgoingRoom)
                    .push()
                    .setValue(model).addOnSuccessListener(unused -> {

                    });
        });
    }
}