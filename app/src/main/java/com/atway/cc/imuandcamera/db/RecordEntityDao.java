package com.atway.cc.imuandcamera.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordEntityDao {

    @Query("SELECT * FROM record")
    LiveData<List<RecordEntity>> getAll();


    @Query("SELECT * FROM record WHERE id = (:id)")
    RecordEntity findById(int id);

    @Insert
    void insert(RecordEntity... records);

    @Delete
    void delete(RecordEntity... records);

    @Update
    void update(RecordEntity... records);
}
