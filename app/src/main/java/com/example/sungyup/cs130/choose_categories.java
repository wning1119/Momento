package com.example.sungyup.cs130;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.logging.Logger.global;

public class choose_categories extends AppCompatActivity {
    private MyPosts post;
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;
    private RadioButton r4;
    private ArrayList<String> categories;
    private NumberPicker np;
    private Firebase mRef;
    private long mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_categories);
        post = (MyPosts) getIntent().getSerializableExtra("post");
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://cs130-f3703.firebaseio.com/");

        r1 = (RadioButton) findViewById(R.id.radioButton);
        r2 = (RadioButton) findViewById(R.id.radioButton5);
        r3 = (RadioButton) findViewById(R.id.radioButton6);
        r4 = (RadioButton) findViewById(R.id.radioButton7);

        categories = new ArrayList<>();


        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(24);
        np.setWrapSelectorWheel(true);
    }

    public void cancelCreation(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sharePost(View v){
        //Add categories
        if(r1.isChecked())
            categories.add(r1.getText().toString());
        if(r2.isChecked())
            categories.add(r2.getText().toString());
        if(r3.isChecked())
            categories.add(r3.getText().toString());
        if(r4.isChecked())
            categories.add(r4.getText().toString());

        post.setCategory(categories);
        post.setTimeout(np.getValue());

        addPost(post);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addPost(MyPosts post){
        mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                mCount = dataSnapshot.getChildrenCount();
                //Get a list of all data
//                for (com.firebase.client.DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    MyPosts post = postSnapshot.getValue(MyPosts.class);
//                    System.out.println(post.getLatitude());
//                    System.out.println(post.getLongitude());
//                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String ownerID = user.getUid();
        post.setId((int)(new Date().getTime()/1000));
        mRef.child("Posts").child("Post"+post.getId()).setValue(post);
        mRef.child("Users/" + ownerID).push().setValue(post);
    }
}
