package com.example.android.antiochwheaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void podcast(View view){
        Intent intent = new Intent(this,Sermons.class);
        startActivity(intent);
    }

    public void posts(View view){
        Intent intent = new Intent(this,Blog.class);
        startActivity(intent);
    }
}
