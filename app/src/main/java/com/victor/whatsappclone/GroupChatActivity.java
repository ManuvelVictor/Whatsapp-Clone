package com.victor.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.whatsappclone.adapter.ChatAdapter;
import com.victor.whatsappclone.databinding.ActivityGroupChatBinding;
import com.victor.whatsappclone.model.MessageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();


        binding.chatDetailBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
            startActivity(intent);
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String outgoingId = FirebaseAuth.getInstance().getUid();
        binding.chatDetailUsername.setText("Group Chat");

        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatDetailRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatDetailRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModels.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                                    messageModels.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.send.setOnClickListener(v -> {
            final String message = binding.enterMessage.getText().toString();
            final MessageModel model = new MessageModel(outgoingId, message);
            model.setTimestamp(new Date().getTime());

            binding.enterMessage.setText("");
            database.getReference().child("Group Chat")
                    .push()
                    .setValue(model)
                    .addOnSuccessListener(unused -> {

                    });
        });
    }
}