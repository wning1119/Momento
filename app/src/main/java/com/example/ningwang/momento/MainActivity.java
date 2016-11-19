package com.example.ningwang.momento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setImageResource(R.drawable.ic_action_add);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToNewPost();
            }
        });
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
                return true;
            case R.id.action_favorites:
                goToFavoriteActivity();
                return true;
            case R.id.action_myPosts:
                goToMyPostsActivity();
                return true;
            case R.id.action_logout:
                goToLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToFavoriteActivity(){
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    private void goToMyPostsActivity(){
        Intent intent = new Intent(this, MyPostsActivity.class);
        startActivity(intent);
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToNewPost(){
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }
}