package com.example.ningwang.momento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class choose_categories extends AppCompatActivity {
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_categories);
        post = (Post) getIntent().getSerializableExtra("post");
        Toast toast = Toast.makeText(getApplicationContext(), post.getSubject(), Toast.LENGTH_SHORT);
        toast.show();

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(24);
        np.setWrapSelectorWheel(true);
    }

    public void cancelCreation(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sharePost(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
