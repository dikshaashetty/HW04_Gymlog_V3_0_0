package com.example.hw04_gymlog_v300;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.viewmodels.GymLogViewModel;
import com.example.hw04_gymlog_v300.viewmodels.GymLogAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCE_KEY = "com.example.gymlog.PREFERENCE_KEY";
    private static final String USER_ID_KEY = "com.example.gymlog.USER_ID_KEY";
    public static final String TAG = "GymLog_Activity";

    private ActivityMainBinding binding;
    private GymLogViewModel mGymLogViewModel;

    private int loggedInUserId = -1;

    // --- FIXED METHOD HERE ---
    public static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
    // ------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. CHECK LOGIN STATUS
        SharedPreferences sharedPref = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        loggedInUserId = sharedPref.getInt(USER_ID_KEY, -1);

        // Check if we received the ID via Intent (fallback)
        if (loggedInUserId == -1) {
            loggedInUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        }

        if (loggedInUserId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 2. SETUP RECYCLER VIEW
        RecyclerView recyclerView = binding.logDisplayRecyclerView;
        final GymLogAdapter adapter = new GymLogAdapter(new GymLogAdapter.GymLogDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. SETUP VIEW MODEL
        mGymLogViewModel = new ViewModelProvider(this).get(GymLogViewModel.class);
        mGymLogViewModel.getAllLogsByUserId(loggedInUserId).observe(this, logs -> {
            adapter.submitList(logs);
        });

        // 4. SETUP LOG BUTTON
        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exercise = binding.exerciseInputEditText.getText().toString();
                String weightStr = binding.weightInputEditText.getText().toString();
                String repsStr = binding.repInputEditText.getText().toString();

                if (!exercise.isEmpty() && !weightStr.isEmpty() && !repsStr.isEmpty()) {
                    try {
                        double weight = Double.parseDouble(weightStr);
                        int reps = Integer.parseInt(repsStr);

                        // Correct Constructor Order
                        GymLog log = new GymLog(exercise, reps, weight, loggedInUserId);
                        mGymLogViewModel.insert(log);

                        // Clear inputs
                        binding.exerciseInputEditText.setText("");
                        binding.weightInputEditText.setText("");
                        binding.repInputEditText.setText("");

                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // --- MENU LOGIC ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out_menu_item) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, id) -> logout());
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void logout() {
        SharedPreferences sharedPref = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(USER_ID_KEY, -1);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}