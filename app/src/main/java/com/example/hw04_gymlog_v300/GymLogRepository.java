package com.example.hw04_gymlog_v300;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepository {

    private final GymLogDAO dao;

    public GymLogRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        dao = db.gymLogDAO();
    }

    public void insert(GymLog log) {
        AppDataBase.databaseWriteExecutor.execute(() -> dao.insert(log));
    }

    // Simple blocking read for demo/homework (fine here)
    public List<GymLog> getAllBlocking() {
        Future<List<GymLog>> future = AppDataBase.databaseWriteExecutor.submit(dao::getAll);
        try {
            List<GymLog> out = future.get();
            return out != null ? out : new ArrayList<>();
        } catch (ExecutionException | InterruptedException e) {
            Log.i("GymLogRepo", "Error reading logs", e);
            return new ArrayList<>();
        }
    }
}
