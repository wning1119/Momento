package com.example.ningwang.momento;

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

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {
    private List<MyPosts> myPosts = new ArrayList<MyPosts>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        populatePostList();
        populateListView();
    }

    private void populatePostList() {
        myPosts.add(new MyPosts("Test1", "Bunch of Contents", "Nov 3, 2016"));
        myPosts.add(new MyPosts("Test2", "Sharing about the week", "Oct 30, 2016"));
        myPosts.add(new MyPosts("Test3", "Updating the project and catching up", "Oct 27, 2016"));
        myPosts.add(new MyPosts("Test4", "Meeting at the library", "Oct 21, 2016"));
    }

    private void populateListView(){
        ArrayAdapter<MyPosts> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.myPostsListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<MyPosts>{
        public MyListAdapter(){
            super(MyPostsActivity.this, R.layout.mypost_view, myPosts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //Make sure we have a view to work with
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.mypost_view, parent, false);
            MyPosts currentPost = myPosts.get(position);

            TextView subjectText = (TextView)itemView.findViewById(R.id.txtSubject);
            subjectText.setText(currentPost.getSubject());

            TextView contentText = (TextView)itemView.findViewById(R.id.txtContent);
            contentText.setText(currentPost.getContent());

            TextView dateText = (TextView)itemView.findViewById(R.id.txtDate);
            dateText.setText(currentPost.getDate());

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
