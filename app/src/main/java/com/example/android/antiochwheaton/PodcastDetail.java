package com.example.android.antiochwheaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PodcastDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);

        TextView podcastText = (TextView)findViewById(R.id.tv_podcast);

        Intent intent = getIntent();

        String podcast = intent.getStringExtra("podcast");

        podcastText.setText(podcast);

    }
}
