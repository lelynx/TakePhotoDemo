package com.example.takephotodemo.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takephotodemo.UseCase.ListPhotosUseCase;
import com.example.takephotodemo.adapter.DisplayPhotoAdapter;
import com.example.takephotodemo.databinding.ActivityDisplayPhotoBinding;
import com.example.takephotodemo.model.PhotoBitmap;

import java.util.ArrayList;

public class DisplayPhotoActivity extends AppCompatActivity implements ListPhotosUseCase.Callback {

    ActivityDisplayPhotoBinding binding;
    DisplayPhotoAdapter photoAdapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        initRv();

    }

    private void initRv() {
        rv = binding.rvDisplayPhotos;
        photoAdapter = new DisplayPhotoAdapter();
        rv.setAdapter(photoAdapter);

        ListPhotosUseCase listPhotosUseCase = new ListPhotosUseCase(this);
        listPhotosUseCase.registerObserver(this);
        listPhotosUseCase.listerPhotos();
//        listPhotosUseCase.registerObserver(new ListPhotosUseCase.Callback() {
//            @Override
//            public void getListPhotoBitmap(ArrayList<PhotoBitmap> list) {
//                photoAdapter.submitList(list);
//                rv.setAdapter(photoAdapter);
//            }
//        });
    }

    @Override
    public void getListPhotoBitmap(ArrayList<PhotoBitmap> list) {
        Log.d("TEST", "getListPhotoBitmap: list.size : "+list.size());
        photoAdapter.submitList(list);
    }
}