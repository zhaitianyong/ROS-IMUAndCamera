package com.atway.cc.imuandcamera.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {RecordEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    private static AppDatabase   INSTANCE;

    public static synchronized AppDatabase getDatabase(Context context){
        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "ros_record")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract RecordEntityDao getRecordEntityDao();
}
