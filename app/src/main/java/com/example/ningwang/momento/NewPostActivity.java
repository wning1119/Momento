package com.example.ningwang.momento;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                //Get all the fields and create new Post

                //Subject
                EditText subjectToShare = (EditText)findViewById(R.id.subjectToShare);
                String subject = subjectToShare.getText().toString();

                //Detail
                EditText contentToShare = (EditText)findViewById(R.id.contentToShare);
                String detail = contentToShare.getText().toString();

                //Default values
                int favorite = 0;
                int timeout = 1;

                //timestamp
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                //String timestamp = DateFormat.getDateTimeInstance().format(new Date());

                //Longitude and latitude
                double longitude = 1;
                double latitude = 1;

                //Implement category
                ArrayList<String> category = new ArrayList<String>();

                //replayIds
                ArrayList<Reply> replies = new ArrayList<Reply>();

                //Ownder ID
                //Should be inherent
                User user = new User();
                String ownerID = user.getId();

                //Post ID: set to -1. databse will handle it
                int postID = -1;

                Post newPost = new Post(favorite, timeout, subject, detail, timestamp, longitude, latitude, category, replies, ownerID, postID);

                //API call to write post
                //writePostToDB(User user, Post post);

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
