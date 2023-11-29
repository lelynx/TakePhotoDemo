package com.example.takephotodemo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.takephotodemo.model.Media;

import java.util.List;

@Dao
public interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void upsert(Media media);

    @Delete
    void delete(Media media);

    @Query("delete from media where estateId = :estateId")
    void delete(int estateId);

    @Query("select * from media")
    LiveData<List<Media>> getAllMedia();

    @Query("select * from media where estateId = :estateId")
    LiveData<List<Media>> getMediaByEstate(int estateId);
}
