package com.example.hw04_gymlog_v300.database;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.hw04_gymlog_v300.MainActivity;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.database.entities.User;
import com.example.hw04_gymlog_v300.database.typeconverters.LocalDateTypeConverter;

/**
 * The main Room database class
 * This serves as the main access point to the data.
 * It defines the entities, version number, and type converter
 */
@TypeConverters({LocalDateTypeConverter.class})
@Database(entities = {GymLog.class, User.class}, version = 3, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    // using Constants for database and table names to prevent typo errors across the app
    public static final String DATABASE_NAME = "gym_log_database";
    public static final String GYM_LOG_TABLE = "gym_log_table";
    public static final String USER_TABLE = "user_table";

    // Singleton instance to ensure we only ever have one database connection open
    private static volatile AppDataBase INSTANCE;

    // Abstract methods that allow the Repository to access the DAOs
    // Room automatically generates the implementation code for these
    public abstract GymLogDAO gymLogDAO();
    public abstract UserDAO userDAO();

    // We use 4 threads for background database operations
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets the singleton instance of the database
     * If it doesn't exist, it builds it using the Room builder
     */
    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()     //If version changes, wipe data and rebuild
                            .addCallback(addDefaultValues)       //Attach the callback to populate data
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback that runs when the database is first created.
     * We use this to insert the default users (admin1 and testuser1) so the app isn't empty on first run.
     */
    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Execute database operations on a background thread
            databaseWriteExecutor.execute(() -> {
                Log.i(MainActivity.TAG, "Database created");

                // Get the UserDAO from the instance (safe because DB is created now)
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();    //Clean slate

                // Insert default Admin user
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                // Insert default Test user
                User testUser = new User("testuser1", "testuser1");
                dao.insert(testUser);
            });
        }
    };

}