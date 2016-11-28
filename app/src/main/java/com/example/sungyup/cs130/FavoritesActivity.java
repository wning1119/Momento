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

public class FavoritesActivity extends AppCompatActivity {
    private List<MyPosts> favorites = new ArrayList<>();
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://cs130-f3703.firebaseio.com/");

        populatePostList();
    }

    private void populatePostList() {
    //API call to get all the favorites
    //Make the return type List: equate it to the favrotiesPosts List

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String ownerID = user.getUid();

        mRef.child("Users/" + ownerID + "/favoritePosts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                final List<Integer> favoriteID = new ArrayList<>();
                //MyPosts post;
                try {
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        favoriteID.add(child.getValue(Integer.class));
                    }
                    mRef.child("Posts").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            try {
                                for(DataSnapshot child : dataSnapshot.getChildren()) {
                                    for(int i = 0; i < favoriteID.size(); i++){
                                        if(favoriteID.get(i) == child.child("id").getValue(Integer.class)){
                                            //String ownerId = child.child("ownerId").getValue(String.class);
                                            Long timestamp = child.child("timestamp").getValue(Long.class);
                                            String subject = child.child("subject").getValue(String.class);
                                            String detail = child.child("detail").getValue(String.class);
                                            MyPosts newPost = new MyPosts(0, 0, subject, detail, timestamp, 0, 0, null, null, null, "", 0);
                                            favorites.add(newPost);
                                        }
                                    }
                                }
                                populateListView();
                            } catch(Exception e) {
                                System.out.println("ERROR");
                            }

                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                } catch(Exception e) {
                    System.out.println("ERROR");
                }


            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private void populateListView(){
        ArrayAdapter<com.example.sungyup.cs130.MyPosts> adapter = new FavoritesActivity.MyListAdapter();
        ListView list = (ListView) findViewById(R.id.favoritesListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<com.example.sungyup.cs130.MyPosts>{
        public MyListAdapter(){
            super(FavoritesActivity.this, R.layout.favorites_view, favorites);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //Make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.favorites_view, parent, false);
            com.example.sungyup.cs130.MyPosts currentPost = favorites.get(position);

            TextView subjectText = (TextView)itemView.findViewById(R.id.txtSubject);
            subjectText.setText(currentPost.getSubject());

            TextView contentText = (TextView)itemView.findViewById(R.id.txtContent);
            contentText.setText(currentPost.getDetail());

            TextView dateText = (TextView)itemView.findViewById(R.id.txtDate);
            dateText.setText(currentPost.getTimestamp() + "");

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
                return true;
            case R.id.action_myPosts:
                goToMyPosts();
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

    private void goToMyPosts(){
        Intent intent = new Intent(this, MyPostsActivity.class);
        startActivity(intent);
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
