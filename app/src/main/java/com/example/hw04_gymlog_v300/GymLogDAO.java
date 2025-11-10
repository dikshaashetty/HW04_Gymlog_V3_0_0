package com.example.hw04_gymlog_v300;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GymLogDAO {

    @Insert
    void insert(GymLog log);

    @Query("SELECT * FROM gymlog_table ORDER BY date DESC")
    List<GymLog> getAllRecords();
}
