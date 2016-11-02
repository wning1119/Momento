package com.example.sungyup.cs130;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.button);
//        button.setBackgroundColor(0xFF33AFFF);
        button.setOnClickListener(new View.OnClickListener(){
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
