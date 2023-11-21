package com.example.takephotodemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Media implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String estateId;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    private String description;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstateId() {
        return estateId;
    }

    public void setEstateId(String estateId) {
        this.estateId = estateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Media(String estateId, byte[] image, String description) {
        this.estateId = estateId;
        this.image = image;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return id == media.id && estateId == media.estateId && Arrays.equals(image, media.image) && Objects.equals(description, media.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, estateId, description);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
    @NonNull
    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", estateId=" + estateId +
                ", image=" + Arrays.toString(image) +
                ", description='" + description + '\'' +
                '}';
    }
}
