package com.example.ningwang.momento;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URI;

public class NewPostActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 10;
    public static final int GALLERY_REQUEST = 11;
    private ImageView imgSpecimentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Button buttonCancel = (Button) findViewById(R.id.button3);
        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMainActivity();
            }
        });

        Button buttonShare = (Button) findViewById(R.id.button2);
        buttonShare.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMainActivity();
            }
        });

        imgSpecimentPhoto = (ImageView) findViewById(R.id.imgSpecimenPhoto);
    }
    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnTakePhotoClicked(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void btnGalleryClicked(View v){
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                imgSpecimentPhoto.setImageBitmap(cameraImage);
            }

            if(requestCode == GALLERY_REQUEST){
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imgSpecimentPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
