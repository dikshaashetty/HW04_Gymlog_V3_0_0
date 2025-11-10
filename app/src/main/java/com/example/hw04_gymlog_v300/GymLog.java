package com.example.hw04_gymlog_v300;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "gymlog_table")
public class GymLog {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;

    private String mExercise;
    private double mWeight;
    private int mReps;

    // store as column "date" so SQL reads nicer
    @ColumnInfo(name = "date")
    private LocalDateTime mDate;

    public GymLog(String exercise, double weight, int reps) {
        this.mExercise = exercise;
        this.mWeight = weight;
        this.mReps = reps;
        this.mDate = LocalDateTime.now();
    }

    // getters/setters
    public int getLogId() { return mLogId; }
    public void setLogId(int logId) { this.mLogId = logId; }

    public String getExercise() { return mExercise; }
    public void setExercise(String exercise) { this.mExercise = exercise; }

    public double getWeight() { return mWeight; }
    public void setWeight(double weight) { this.mWeight = weight; }

    public int getReps() { return mReps; }
    public void setReps(int reps) { this.mReps = reps; }

    public LocalDateTime getDate() { return mDate; }
    public void setDate(LocalDateTime date) { this.mDate = date; }

    @Override
    public String toString() {
        String dateStr = (mDate == null)
                ? ""
                : mDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return  "Exercise: " + mExercise + "\n" +
                "Weight: "   + mWeight   + "\n" +
                "Reps: "     + mReps     + "\n" +
                "date: "     + dateStr   + "\n" +
                "--------------------\n";
    }
}
