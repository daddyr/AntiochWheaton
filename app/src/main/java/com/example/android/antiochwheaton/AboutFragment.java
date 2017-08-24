package com.example.android.antiochwheaton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by ryan_ on 7/25/2017.
 */

public class AboutFragment extends Fragment {
    public AboutFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about,container,false);

        ImageButton imageButton = (ImageButton)rootView.findViewById(R.id.ibLocation);
        final TextView textView = (TextView)rootView.findViewById(R.id.textView2);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("geo:0,0?q=" + textView.getText());
                intent.setData(uri);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void onCLick(){

    }
}
