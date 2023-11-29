package com.example.takephotodemo.ui;

import static com.example.takephotodemo.Utils.MEDIA_LIST;
import static com.example.takephotodemo.Utils.MEDIA_LIST_CODE;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.takephotodemo.UseCase.ListPhotosUseCase;
import com.example.takephotodemo.Utils;
import com.example.takephotodemo.databinding.ActivityMainBinding;
import com.example.takephotodemo.model.Media;
import com.example.takephotodemo.model.MediaData;
import com.example.takephotodemo.repositories.MediaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// https://developer.android.com/training/data-storage/shared/media?hl=fr#java
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Button mBtnTakePhoto;
    Button mBtnListPhotos;
    ArrayList<MediaData> mMediaDataList = new ArrayList<>();
    List<Media> mMediaList = new ArrayList<Media>();

    ListPhotosUseCase listPhotosUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBtnTakePhoto = binding.btnTakePhoto;
        mBtnListPhotos = binding.btnListPhotos;
        listPhotosUseCase = new ListPhotosUseCase(this);

        setListeners();

    }

    private void setListeners() {

        mBtnTakePhoto.setOnClickListener(view -> runCamera());

        mBtnListPhotos.setOnClickListener(view -> displayPhotos());
    }

    private void displayPhotos() {
        Intent intent = new Intent(this, DisplayPhotoActivity.class);
        startActivity(intent);
    }


    private void runCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, MEDIA_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        ArrayList<MediaData> mediaDataArrayList = data.getParcelableArrayListExtra(MEDIA_LIST);
        // IMPORTANT: ici, j'ai déconnecté le test sur resultCode, qui est toujours bad!
        if (requestCode == MEDIA_LIST_CODE ) {
//        if (requestCode == MEDIA_LIST_CODE && resultCode == RESULT_OK) {
            // on récupère la liste des MediaData
            if (mediaDataArrayList != null) {
                mMediaDataList = mediaDataArrayList;

                for (MediaData mediaData : mMediaDataList) {
                    getImageFromMediaDataList(mediaData);
                }
                // Enregsitrer les Media dans SQLite (Room)
                registerMediaInDB();
            }
        }
    }

    private void registerMediaInDB() {
        // Pas de ViewModel--Interagi directement avec le Repository!
        MediaRepository mediaRepository = new MediaRepository(this);
        for (Media media : mMediaList) {
            UUID estateId = UUID.randomUUID();
            media.setEstateId(estateId.toString());
            mediaRepository.upsert(media);
            Log.d(Utils.TAG, "registerMediaInDB: estateId: "+estateId.toString());
        }
    }

    private void getImageFromMediaDataList(MediaData mediaData) {

        try {
            // On récupére la photo, dont l'uri est dans mediaData, le paramètre
            ContentResolver resolver = getContentResolver();
            // Photo récupérée sous forme de bitmap
            Bitmap image = MediaStore.Images.Media.getBitmap(resolver, mediaData.getUri());
            // La bitmap est converti en octets(Byte)--stockée dans un Media--enregsitrée dans la liste
            mMediaList.add(new Media("0", Utils.convertBitmapToBytes(image), mediaData.getDescription()));

        } catch (Exception e) {
            Log.d("TAG", "Unable to find the picture : " + e.getMessage());
            Toast.makeText(MainActivity.this, "Unable to find the picture in gallery", Toast.LENGTH_SHORT).show();
        }
    }
}