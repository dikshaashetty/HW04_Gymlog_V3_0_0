package com.example.hw04_gymlog_v300;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GymLog.class}, version = 1, exportSchema = false)
@TypeConverters({LocalDateTimeTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {

    public abstract GymLogDAO gymLogDAO();

    private static volatile AppDataBase INSTANCE;

    // Single thread executor, fine for this small app
    public static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDataBase.class,
                                    "gymlog_db")
                            // ✅ Correct place for fallbackToDestructiveMigration()
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // optional: code to prefill database can go here
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
