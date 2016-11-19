package com.example.ningwang.momento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewPostActivity extends AppCompatActivity {

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
                goToMainActivity();
            }
        });
    }
    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
