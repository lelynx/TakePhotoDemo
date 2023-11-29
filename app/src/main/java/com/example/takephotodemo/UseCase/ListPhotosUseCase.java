package com.example.takephotodemo.UseCase;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.takephotodemo.model.PhotoBitmap;

import java.util.ArrayList;

public class ListPhotosUseCase {

    public interface Callback {
        void getListPhotoBitmap(ArrayList<PhotoBitmap> list);
    }

    private Callback observer;

    private Context context;

    public ArrayList<PhotoBitmap> getListPhotoBitmap() {
        return listPhotoBitmap;
    }

    private final ArrayList<PhotoBitmap> listPhotoBitmap = new ArrayList<>();

    public ListPhotosUseCase(Context context) {
        this.context = context;
    }

    public void registerObserver(Callback observer) {
        Log.d("TEST", "registerObserver: enter");
        this.observer = observer;
    }

    public void listerPhotos() {
        // But: lister les photos qui se trouvent dans la gallery
        // Obtenez une instance de MediaStore.Images.Media

        // Obtenez le chemin d'accès au répertoire des photos

//        MediaStore.Images.Media images = MediaStore.Images.Media.getContentUri(dirPath);
        // Uri: content://media/internal/images/
        // Uri: content://media/external/images/media

        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.d("GalleryActivity", "listerPhotos: Uri: "+imagesUri.toString());
        // Obtenez une instance de MediaStore.Images.Media

        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = { MediaStore.Images.Media.DATA };
        String sortOrder = MediaStore.Images.Media._ID + " desc";
        Cursor cursor = contentResolver.query(imagesUri, null, null, null, sortOrder);
        assert cursor != null;
        Log.d("GalleryActivity", "listerPhotos: cursor.getCount(): "+cursor.getCount());
        // Parcourir la liste des photos
        while (cursor.moveToNext()) {
            // Obtenez les informations sur la photo
            int id = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            String name = cursor.getString(id);
            id = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            String date = cursor.getString(id);
            id = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            long size = cursor.getLong(id);
            id = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
            String mimeType = cursor.getString(id);
            id = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(id);

            // Traitez les informations de la photo
            Log.d("GalleryActivity", "Nom de la photo : " + name);
            Log.d("GalleryActivity", "Date de la photo : " + date);
            Log.d("GalleryActivity", "Taille de la photo : " + size);
            Log.d("GalleryActivity", "Type de fichier de la photo : " + mimeType);
            Log.d("GalleryActivity", "data : " + imagePath);

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            listPhotoBitmap.add(new PhotoBitmap(name, bitmap));
//            ImageView imageView = ((MainActivity) context).findViewById(R.id.imageView);
//            imageView.setImageBitmap(bitmap);
        }
        cursor.close();
        Log.d("TEST", "listerPhotos: observer==null?: "+(observer==null));
        observer.getListPhotoBitmap(listPhotoBitmap);

    }//listerPhotos
}
