package com.example.hw04_gymlog_v300;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw04_gymlog_v300.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GymLogRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repo = new GymLogRepository(getApplication());

        // make the gray log box scroll
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        // show any logs already saved
        refreshDisplayFromDb();

        binding.logButton.setOnClickListener(v -> {
            String exercise = binding.exerciseInput.getText().toString().trim();
            String wStr = binding.weightInput.getText().toString().trim();
            String rStr = binding.repsInput.getText().toString().trim();

            double weight = 0.0;
            int reps = 0;
            try { weight = Double.parseDouble(wStr); } catch (NumberFormatException ignored) {}
            try { reps   = Integer.parseInt(rStr); }  catch (NumberFormatException ignored) {}

            GymLog log = new GymLog(exercise, weight, reps);
            repo.insert(log);              // save to Room
            prependToDisplay(log);         // show immediately

            // clear inputs
            binding.exerciseInput.getText().clear();
            binding.weightInput.getText().clear();
            binding.repsInput.getText().clear();

            Toast.makeText(this, "Logged!", Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshDisplayFromDb() {
        List<GymLog> logs = repo.getAllBlocking();
        StringBuilder sb = new StringBuilder();
        for (GymLog g : logs) {
            sb.append(formatLog(g)).append("\n\n");
        }
        binding.logDisplayTextView.setText(
                sb.length() == 0 ? "(Logs will appear here)" : sb.toString().trim()
        );
    }

    private void prependToDisplay(GymLog g) {
        String current = binding.logDisplayTextView.getText().toString();
        String block = formatLog(g) + "\n\n" + (current == null ? "" : current);
        binding.logDisplayTextView.setText(block.trim());
    }

    private String formatLog(GymLog g) {
        return String.format(Locale.getDefault(),
                "Exercise: %s\nWeight: %.2f\nReps: %d",
                g.getExercise(), g.getWeight(), g.getReps());
    }
}
