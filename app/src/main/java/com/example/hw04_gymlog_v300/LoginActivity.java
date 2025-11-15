package com.example.hw04_gymlog_v300;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hw04_gymlog_v300.database.GymLogRepository;
import com.example.hw04_gymlog_v300.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private GymLogRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new GymLogRepository(getApplication());

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameLoginEditText.getText().toString();
            String password = binding.passwordLoginEditText.getText().toString();
            repository.getUserByUsername(username).observe(this, user -> {
                if (user != null && user.getPassword().equals(password)) {
                    SharedPreferences sharedPref = getSharedPreferences("com.example.gymlog.PREFERENCE_KEY", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("com.example.gymlog.USER_ID_KEY", user.getId());
                    editor.apply();

                    // Correctly call the factory method from MainActivity
                    Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext(), user.getId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}