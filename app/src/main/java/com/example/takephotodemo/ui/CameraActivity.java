package com.example.takephotodemo.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.takephotodemo.R;
import com.example.takephotodemo.Utils;
import com.example.takephotodemo.databinding.ActivityCameraBinding;
import com.example.takephotodemo.model.MediaData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    ActivityCameraBinding binding;
    private FloatingActionButton fab_Take_Picture;
    private FloatingActionButton fab_finish_camera_activity;
    private PreviewView previewView;
    private ImageView imageView;
    private ConstraintLayout front_layout;
    private Button cancel_button;
    private Button validate_button;
    private Spinner roomTypeSpinner;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Uri mImageUri;
    ArrayList<MediaData> mediaDataList = new ArrayList<>();
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        initBinding();
        initListener();
        initCamera();
        loadRoomSpinner();
    }

    private void loadRoomSpinner() {
        ArrayList<String> roomTypeList = new ArrayList<>();
        roomTypeList.add("1 pièces");
        roomTypeList.add("2 pièces");
        roomTypeList.add("3 pièces");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, roomTypeList);
        roomTypeSpinner.setAdapter(arrayAdapter);

    }

    private void initBinding() {
        fab_Take_Picture = binding.imageCaptureFab;
        fab_finish_camera_activity = binding.fabFinishCameraActivity;
        previewView = binding.cameraPreview;
        imageView = binding.imageviewPreview;
        front_layout = binding.frontLayout;
        cancel_button = binding.cancelButton;
        validate_button = binding.validateButton;
        roomTypeSpinner = binding.roomSelectSpinner;
    }

    private void initCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            ProcessCameraProvider cameraProvider = null;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            bindPreview(cameraProvider);
        }, ContextCompat.getMainExecutor(this));
    }

    private void initListener() {
        fab_Take_Picture.setOnClickListener(view -> {
            Date date = new Date();
            String timestamp = String.valueOf(date.getTime());

            // L’Uri de la collection d’images stockées dans le MediaStore est obtenu
            ContentResolver contentResolver = getContentResolver();
            Uri imageCollection;
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            // L’Uri d’enregistrement de la photo qui va être créée, est créé
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, timestamp + ".jpg");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri imageUri = contentResolver.insert(imageCollection, contentValues);

            // Création d'un flux pour enregistrer l'image
            OutputStream outputStream = null;
            try {
                outputStream = contentResolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            assert outputStream != null;
            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(outputStream).build();

            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(CameraActivity.this),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            mImageUri = imageUri; // L'Uri construite un peu plus haut
                            previewView.setVisibility(View.INVISIBLE);
                            front_layout.setVisibility(View.VISIBLE);
                            fab_Take_Picture.setEnabled(false);
                            fab_finish_camera_activity.setEnabled(false);
                            validate_button.setVisibility(View.VISIBLE);
                            cancel_button.setVisibility(View.VISIBLE);

                            showPhotoPreview(mImageUri);
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Log.d("TAG", "onError: " + exception.getMessage());
                            mImageUri = null;
                        }
                    });

        });

        fab_finish_camera_activity.setOnClickListener(view -> {
            Intent intent = new Intent();
//            intent.putExtra(Utils.MEDIA_LIST, mediaDataList);
            intent.putParcelableArrayListExtra(Utils.MEDIA_LIST, mediaDataList);
            setResult(Utils.MEDIA_LIST_CODE, intent);
            finish();
        });


        cancel_button.setOnClickListener(view -> {
            front_layout.setVisibility(View.INVISIBLE);
            mImageUri = null;
            roomTypeSpinner.setSelection(0);
            previewView.setVisibility(View.VISIBLE);
            fab_Take_Picture.setEnabled(true);
            fab_finish_camera_activity.setEnabled(true);
            validate_button.setVisibility(View.INVISIBLE);
            cancel_button.setVisibility(View.INVISIBLE);
        });

        validate_button.setOnClickListener(view -> {
            if (roomTypeSpinner.getSelectedItem().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "You must select a room type",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Créé un objet de type MediaData pour enregistrer l'uri de la photo
                MediaData mediaData = new MediaData(mImageUri,
                        roomTypeSpinner.getSelectedItem().toString());
                // cet objet de type MediaData est enregistré dans une ArrayList
                mediaDataList.add(mediaData);

                Toast.makeText(CameraActivity.this, roomTypeSpinner.getSelectedItem().toString() + " picture recorded", Toast.LENGTH_SHORT).show();

                front_layout.setVisibility(View.INVISIBLE);
                mImageUri = null;
                roomTypeSpinner.setSelection(0);
                previewView.setVisibility(View.VISIBLE);
                fab_Take_Picture.setEnabled(true);
                fab_finish_camera_activity.setEnabled(true);
                validate_button.setVisibility(View.INVISIBLE);
                cancel_button.setVisibility(View.INVISIBLE);
            }

        });

    }

    private void showPhotoPreview(Uri file) {
        imageView.setImageURI(file);
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageCapture =
                    new ImageCapture.Builder()

                            .setTargetResolution(new Size(1280, 720))
                            .build();
        } else {
            imageCapture =
                    new ImageCapture.Builder()

                            .setTargetResolution(new Size(720, 1280))
                            .build();
        }

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }
}