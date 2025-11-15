package com.example.hw04_gymlog_v300.database.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.hw04_gymlog_v300.database.AppDataBase;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 * This is the main Entity for the application
 * Room uses this class to generate the table structure in the SQL database
 * We reference the table name from AppDataBase to keep things consistent
 */
@Entity(tableName = AppDataBase.GYM_LOG_TABLE)
public class GymLog {
    // The Unique ID for each log
    // @PrimaryKey(autoGenerate = true) means Room will automatically handle numbering
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String exercise;
    private int reps;
    private double weight;
    private int userId;
    private LocalDateTime date;

    // Constructor used to create new log entries.
    public GymLog(String exercise, int reps, double weight, int userId) {
        this.exercise = exercise;
        this.reps = reps;
        this.weight = weight;
        this.userId = userId;
        date = LocalDateTime.now();
    }

    //Utility Methods
    // Checks if two GymLog objects are identical.
    // This is very important for the RecyclerView Adapter to know if it needs to refresh a row.
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GymLog gymLog = (GymLog) o;
        return Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && userId == gymLog.userId && Objects.equals(id, gymLog.id) && Objects.equals(exercise, gymLog.exercise) && Objects.equals(date, gymLog.date);
    }

    // Generates a unique hash for the object, also used for comparison efficiency
    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, weight, reps, date, userId);
    }

    // Controls how the log looks when displayed as text.
    @Override
    public String toString() {
        return "Exercise: " + exercise + "\n" +
                "Weight: " + weight + "\n" +
                "Reps: " + reps + "\n" +
                "Date: " + date.toString() + "\n" + //
                "====================\n";
    }

    // --- Getters and Setters ---
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getExercise() {
        return exercise;
    }
    public void setExercise(String exercise) {
        this.exercise = exercise;
    }
    public int getReps() {
        return reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}