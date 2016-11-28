package com.example.sungyup.cs130;

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

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private Firebase mRef;

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
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://cs130-f3703.firebaseio.com/");

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

                //Longitude and latitude
                Bundle extras = getIntent().getExtras();
                double latitude = 0;
                double longitude = 0;
                if(extras != null) {
                    latitude = extras.getDouble("latitude");
                    longitude = extras.getDouble("longitude");
                }

                //Owner ID
                //Should be inherent
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String ownerID = user.getUid(); //mRef.getAuth().getUid();

                //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                //Test Purposes
//                newPost.setCategory(new ArrayList<String>());
//                newPost.setTimeout(1);
                //
                MyPosts newPost = new MyPosts(0, 1, subject, detail, System.currentTimeMillis(), longitude, latitude, new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Reply>(), ownerID, 0);

                goToChooseCats(newPost);

            }
        });

        imgSpecimentPhoto = (ImageView) findViewById(R.id.imgSpecimenPhoto);
    }
    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToChooseCats(MyPosts post){
        Intent intent = new Intent(this, choose_categories.class);
        intent.putExtra("post", post);
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
