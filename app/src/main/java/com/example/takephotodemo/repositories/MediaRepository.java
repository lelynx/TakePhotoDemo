package com.example.takephotodemo.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.takephotodemo.dao.MediaDao;
import com.example.takephotodemo.dao.MediaDatabase;
import com.example.takephotodemo.model.Media;

import java.util.List;


public class MediaRepository {

    private final MediaDao mediaDao;
    private final LiveData<List<Media>> mediaList;

    public MediaRepository(Context context) {
        MediaDatabase db = MediaDatabase.getInstance(context);
        mediaDao = db.getMediaDao();
        mediaList = mediaDao.getAllMedia();
    }

    public LiveData<List<Media>> getAllMedias() {
        return mediaList;
    }

    public LiveData<List<Media>> getMediaById(int estateId) {
        return mediaDao.getMediaByEstate(estateId);
    }

    public void upsert(Media media) {

        MediaDatabase.databaseWriteExecutor.submit(() -> {
            mediaDao.upsert(media);
            return null;
        });

    }

    public void delete(int estateId) {
        MediaDatabase.databaseWriteExecutor.submit(() -> {
            mediaDao.delete(estateId);
            return null;
        });

    }


}
