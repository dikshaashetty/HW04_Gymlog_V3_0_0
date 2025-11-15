package com.example.hw04_gymlog_v300.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import java.util.List;

/**
 * Data Access Object (DAO) for the Gym Logs
 * This interface acts as a bridge between Java code and the database
 * methods are defined here, and Room automatically generates the SQL code to run them.
 */
@Dao
public interface GymLogDAO {
    /**
     * Inserts a new GymLog into the database
     * onConflict = REPLACE means if a log with the same ID already exists
     * it will overwrite it
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymLog);

    /**
     * Retrieves every single log in the database, regardless of who has posted it
     * Results are sorted by date (descending), so newest logs appear first.
     * Returns a standard List
     */
    @Query("SELECT * FROM " + AppDataBase.GYM_LOG_TABLE + " ORDER BY date DESC")
    List<GymLog> getAllRecords();

    /**
     * Retrieves logs for the specific user, but returns 'LiveData'
     * This is special because it allows the UI (RecyclerView) to observe the database
     * If a new log is added, the UI will automatically refresh without us needing to query again
     */
    @Query("SELECT * FROM " + AppDataBase.GYM_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<GymLog>> getRecordsByUserIdLiveData(int loggedInUserId);

    /**
     * Retrieves logs only for the specific user currently logged in
     * This returns a standard List, which means it's a "snapshot" of the data at that moment
     */
    @Query("SELECT * FROM " + AppDataBase.GYM_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<GymLog> getRecordsByUserId(int loggedInUserId);
}