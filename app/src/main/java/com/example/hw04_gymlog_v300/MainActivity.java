package com.example.hw04_gymlog_v300;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;

import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GymLogRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = GymLogRepository.getRepository(getApplication());

        binding.logButton.setOnClickListener(v -> {
            insertGymLogRecord();
            // Pull from DB after insert (note insert is async; simple apps can just refresh)
            updateDisplay();
        });

        // initial load
        updateDisplay();
    }

    private void insertGymLogRecord() {
        String exercise = safeText(binding.exerciseInput.getText().toString());
        String wStr     = safeText(binding.weightInput.getText().toString());
        String rStr     = safeText(binding.repsInput.getText().toString());

        if (TextUtils.isEmpty(exercise) || TextUtils.isEmpty(wStr) || TextUtils.isEmpty(rStr)) {
            return; // don’t insert empties
        }

        double weight;
        int reps;
        try {
            weight = Double.parseDouble(wStr);
            reps   = Integer.parseInt(rStr);
        } catch (NumberFormatException e) {
            return; // invalid numbers -> skip
        }

        repository.insert(new GymLog(exercise, weight, reps));

        // (optional) clear inputs
        // binding.exerciseInput.setText("");
        // binding.weightInput.setText("");
        // binding.repsInput.setText("");
    }

    private void updateDisplay() {
        ArrayList<GymLog> logs = repository.getAllLogs();
        if (logs.isEmpty()) {
            binding.logDisplayTextView.setText("Nothing to show. Time to hit the gym!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (GymLog log : logs) {
            sb.append(log.toString());
        }
        binding.logDisplayTextView.setText(sb.toString());
    }

    private String safeText(String s) {
        return s == null ? "" : s.trim();
    }
}
