package com.example.hw04_gymlog_v300;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gymlog_table")
public class GymLog {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;

    private String mExercise;
    private double mWeight;
    private int mReps;

    public GymLog(String exercise, double weight, int reps) {
        this.mExercise = exercise;
        this.mWeight = weight;
        this.mReps = reps;
    }

    public int getLogId() { return mLogId; }
    public void setLogId(int logId) { this.mLogId = logId; }

    public String getExercise() { return mExercise; }
    public void setExercise(String exercise) { this.mExercise = exercise; }

    public double getWeight() { return mWeight; }
    public void setWeight(double weight) { this.mWeight = weight; }

    public int getReps() { return mReps; }
    public void setReps(int reps) { this.mReps = reps; }

    @Override
    public String toString() {
        return "Exercise: " + mExercise + ", Weight: " + mWeight + ", Reps: " + mReps;
    }
}
