package com.victor.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.victor.whatsappclone.databinding.ActivitySignUpBinding;
import com.victor.whatsappclone.model.Users;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.btnSignUp.setOnClickListener(v -> {
            if (!binding.txtEmail.getText().toString().isEmpty() && !binding.txtUsername.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Users user = new Users(binding.txtUsername.getText().toString(), binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(user);

                                Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

            } else {
                Toast.makeText(SignUpActivity.this,"Enter the Credentials", Toast.LENGTH_SHORT).show();
            }
        });

    }
}