package com.example.sungyup.cs130;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {
    private List<MyPosts> myPosts = new ArrayList<>();
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://cs130-f3703.firebaseio.com/");

        populatePostList();

    }

    private void populatePostList() {
        //Call API to get a list of user's posts
//Change return type of this function to list: and make myPosts equal to the return
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String ownerID = user.getUid();

        mRef.child("Users/" + ownerID).addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            int count = 0;
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot){
                for(com.firebase.client.DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    count++;
                    System.out.println("count="+count);
                    Long timestamp = postSnapShot.child("timestamp").getValue(Long.class);
                    String subject = postSnapShot.child("subject").getValue(String.class);
                    String detail = postSnapShot.child("detail").getValue(String.class);

                    MyPosts newPost = new MyPosts(0, 0, subject, detail, timestamp, 0, 0, null, null, null, "", 0);
                    myPosts.add(newPost);
                }
                populateListView();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError){

            }
        });
        System.out.println("#posts = " + myPosts.size());
    }

    private void populateListView(){
        ArrayAdapter<com.example.sungyup.cs130.MyPosts> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.myPostsListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<com.example.sungyup.cs130.MyPosts>{
        public MyListAdapter(){
            super(MyPostsActivity.this, R.layout.mypost_view, myPosts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //Make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.mypost_view, parent, false);
            com.example.sungyup.cs130.MyPosts currentPost = myPosts.get(position);

            TextView subjectText = (TextView)itemView.findViewById(R.id.txtSubject);
            subjectText.setText(currentPost.getSubject());

            TextView contentText = (TextView)itemView.findViewById(R.id.txtContent);
            contentText.setText(currentPost.getDetail());

            TextView dateText = (TextView)itemView.findViewById(R.id.txtDate);
            dateText.setText("" + currentPost.getTimestamp());

            return itemView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.action_allPosts:
                goToMainActivity();
                return true;
            case R.id.action_favorites:
                goToFavoriteActivity();
                return true;
            case R.id.action_myPosts:
                return true;
            case R.id.action_logout:
                goToLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToFavoriteActivity(){
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
