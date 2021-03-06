package com.example.ningwang.momento;

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

import java.util.ArrayList;
import java.util.List;


public class activity_viewposts extends AppCompatActivity {
//    private List<com.example.sungyup.cs130.MyPosts> myPosts = new ArrayList<com.example.sungyup.cs130.MyPosts>();
    private ImageButton fav;
    private EditText commentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewposts);

//        populatePostList();
//        populateListView();

        fav = (ImageButton) findViewById(R.id.favButton);
        fav.setImageResource(android.R.drawable.btn_star_big_off);
    }

    public void favButtonClicked(View v){
        fav.setImageResource(android.R.drawable.btn_star_big_on);
    }

    public void addComment(View v){
        commentBox = (EditText)findViewById(R.id.comment);
        String comment = commentBox.getText().toString();
        if(!comment.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

//    private void populatePostList() {
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test1", "Bunch of Contents", "Nov 3, 2016"));
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test2", "Sharing about the week", "Oct 30, 2016"));
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test3", "Updating the project and catching up", "Oct 27, 2016"));
//        myPosts.add(new com.example.sungyup.cs130.MyPosts("Test4", "Meeting at the library", "Oct 21, 2016"));
//    }
//
//    private void populateListView(){
//        ArrayAdapter<com.example.sungyup.cs130.MyPosts> adapter = new activity_viewposts.MyListAdapter();
//        ListView list = (ListView) findViewById(R.id.myPostsListView);
//        list.setAdapter(adapter);
//    }
//
//    private class MyListAdapter extends ArrayAdapter<com.example.sungyup.cs130.MyPosts>{
//        public MyListAdapter(){
//            super(activity_viewposts.this, R.layout.mypost_view, myPosts);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent){
//            //Make sure we have a view to work with
//            View itemView = convertView;
//            if(itemView == null)
//                itemView = getLayoutInflater().inflate(R.layout.mypost_view, parent, false);
//            com.example.sungyup.cs130.MyPosts currentPost = myPosts.get(position);
//
//            TextView subjectText = (TextView)itemView.findViewById(R.id.txtSubject);
//            subjectText.setText(currentPost.getSubject());
//
//            TextView contentText = (TextView)itemView.findViewById(R.id.txtContent);
//            contentText.setText(currentPost.getContent());
//
//            TextView dateText = (TextView)itemView.findViewById(R.id.txtDate);
//            dateText.setText(currentPost.getDate());
//
//            return itemView;
//        }
//    }
}
