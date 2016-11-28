package com.example.sungyup.cs130;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class activity_viewposts extends AppCompatActivity {
//    private List<com.example.sungyup.cs130.MyPosts> myPosts = new ArrayList<com.example.sungyup.cs130.MyPosts>();
    private ArrayList<String> myReplies = new ArrayList<String>();
    private ImageButton fav;
    private EditText commentBox;
    private TextView Subject;
    private TextView Detail;
    private TextView Date;

    private Firebase mRef;
    private int saved_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewposts);


        //populateListView();

        fav = (ImageButton) findViewById(R.id.favButton);
        fav.setImageResource(android.R.drawable.btn_star_big_off);
        Subject = (TextView) findViewById(R.id.textView);
        Detail = (TextView) findViewById(R.id.postContent);
        Date = (TextView) findViewById(R.id.textView4);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            saved_id = extras.getInt("id");
        }

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://cs130-f3703.firebaseio.com/");

        mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                //MyPosts post;
                try {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        //post = child.getValue(MyPosts.class);
                        Integer id = child.child("id").getValue(Integer.class);

                        if(id == saved_id) {
                            String ownerId = child.child("ownerId").getValue(String.class);
                            Long timestamp = child.child("timestamp").getValue(Long.class);
                            String subject = child.child("subject").getValue(String.class);
                            String detail = child.child("detail").getValue(String.class);
                            Subject.setText(subject);
                            Detail.setText(detail);
                            Date.setText(""+timestamp);

                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(activity_viewposts.this, "failed to load post", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        populateReplyList();
    }

    public void favButtonClicked(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String ownerID = user.getUid();
        fav.setImageResource(android.R.drawable.btn_star_big_on);
        mRef.child("Users/" + ownerID + "/favoritePosts").push().setValue(saved_id);
    }

    public void addComment(View v){
        mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                commentBox = (EditText)findViewById(R.id.comment);
                String comment = commentBox.getText().toString();

                //MyPosts post;
                try {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        //post = child.getValue(MyPosts.class);
                        Integer id = child.child("id").getValue(Integer.class);

                        if(id == saved_id && !comment.equals("")) {
                            mRef.child("Posts/Post" + saved_id + "/replies").push().setValue(comment);
                        }
                        else if(id == saved_id) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Comment must contain text", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(activity_viewposts.this, "failed to load post", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private void populateReplyList() {
        mRef.child("Posts/Post" + saved_id + "/replies").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            int count = 0;
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot){
                for(com.firebase.client.DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    count++;
                    System.out.println("count="+count);
//                    Long timestamp = postSnapShot.child("timestamp").getValue(Long.class);
//                    String subject = postSnapShot.child("subject").getValue(String.class);
//                    String detail = postSnapShot.child("detail").getValue(String.class);
                    String reply = postSnapShot.getValue(String.class);

                    myReplies.add(reply);
                }
                populateListView();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError){

            }
        });
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test1", "Bunch of Contents", "Nov 3, 2016"));
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test2", "Sharing about the week", "Oct 30, 2016"));
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test3", "Updating the project and catching up", "Oct 27, 2016"));
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test4", "Meeting at the library", "Oct 21, 2016"));
    }

    private void populateListView(){
        ArrayAdapter<String> adapter = new activity_viewposts.MyListAdapter();
        ListView list = (ListView) findViewById(R.id.myPostsListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<String>{
        public MyListAdapter(){
            super(activity_viewposts.this, R.layout.mypost_view, myReplies);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //Make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.mypost_view, parent, false);
            String currentPost = myReplies.get(position);

            TextView subjectText = (TextView)itemView.findViewById(R.id.txtSubject);
            subjectText.setText(currentPost);

            TextView contentText = (TextView)itemView.findViewById(R.id.txtContent);
            contentText.setText("");
//
            TextView dateText = (TextView)itemView.findViewById(R.id.txtDate);
            dateText.setText("");
            return itemView;
        }
    }
}
