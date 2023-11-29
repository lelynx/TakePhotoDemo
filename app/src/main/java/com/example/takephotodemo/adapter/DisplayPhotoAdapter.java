package com.example.takephotodemo.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takephotodemo.R;
import com.example.takephotodemo.model.PhotoBitmap;

public class DisplayPhotoAdapter extends ListAdapter<PhotoBitmap, DisplayPhotoAdapter.PhotoViewHolder> {

    public static final DiffUtil.ItemCallback<PhotoBitmap> DIFF_CALLBACK = new DiffUtil.ItemCallback<PhotoBitmap>() {
        @Override
        public boolean areItemsTheSame(@NonNull PhotoBitmap oldItem, @NonNull PhotoBitmap newItem) {
            return oldItem.getName() == newItem.getName();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull PhotoBitmap oldItem, @NonNull PhotoBitmap newItem) {
            return oldItem.getName() == newItem.getName();
        }
    };

    public DisplayPhotoAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_photo_item, parent, false);
        Log.d("TEST", "onCreateViewHolder: ");
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoBitmap photoBitmap = getItem(position);
        Log.d("TEST", "onBindViewHolder: "+photoBitmap.toString());
        holder.imageView.setImageBitmap(photoBitmap.getBitmap());
//        holder.name.setText(photoBitmap.getName());
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
//        TextView name;
        public PhotoViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imvPhotoItem);
//            name = itemView.findViewById(R.id.textView);
        }
    }
}

