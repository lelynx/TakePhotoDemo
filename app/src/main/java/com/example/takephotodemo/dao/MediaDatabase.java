package com.example.takephotodemo.dao;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.takephotodemo.Media;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Media.class},version = 1, exportSchema = false)
public abstract class MediaDatabase extends RoomDatabase {

    public abstract MediaDao getMediaDao();

    static MediaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MediaDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MediaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MediaDatabase.class, "media_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

}
