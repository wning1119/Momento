package com.example.ningwang.momento;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by ningwang on 11/28/16.
 */

public class Image {
    private Bitmap bitmap;
    private String filename;

    Image(Bitmap bitmap, String filename) {
        this.bitmap = bitmap;
        this.filename = filename;
    }

    Image(String filename) {
        this.filename = filename;
    }

    // store image in firebase with name "postID.jpg"
    public void storeImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] image = baos.toByteArray();

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://momento-758c2.appspot.com");
        StorageReference imagesRef = storageRef.child(filename);

        UploadTask uploadTask = imagesRef.putBytes(image);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    public Uri retrieveImageUri() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://momento-758c2.appspot.com");
        StorageReference imagesRef = storageRef.child(filename);

        Uri uri = imagesRef.getDownloadUrl().getResult();
        return uri;
    }

}

