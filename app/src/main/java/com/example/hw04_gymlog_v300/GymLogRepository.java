package com.example.hw04_gymlog_v300;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GymLogRepository {

    private static final String TAG = "GymLogRepo";
    private static GymLogRepository REPOSITORY;

    private final GymLogDAO dao;

    // make constructor private so we force getRepository()
    private GymLogRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        dao = db.gymLogDAO();
    }

    // Singleton getter that runs on the DB executor (video 5)
    public static GymLogRepository getRepository(Application application) {
        if (REPOSITORY != null) return REPOSITORY;

        Future<GymLogRepository> future =
                AppDataBase.databaseWriteExecutor.submit(() -> new GymLogRepository(application));

        try {
            REPOSITORY = future.get();
            return REPOSITORY;
        } catch (ExecutionException | InterruptedException e) {
            Log.i(TAG, "Problem getting GymLogRepository (thread error).", e);
            return null;
        }
    }

    public void insert(GymLog log) {
        AppDataBase.databaseWriteExecutor.execute(() -> dao.insert(log));
    }

    public ArrayList<GymLog> getAllLogs() {
        Future<List<GymLog>> future =
                AppDataBase.databaseWriteExecutor.submit(dao::getAllRecords);
        try {
            return new ArrayList<>(future.get());
        } catch (ExecutionException | InterruptedException e) {
            Log.i(TAG, "Problem running getAllLogs()", e);
            return new ArrayList<>();
        }
    }
}
