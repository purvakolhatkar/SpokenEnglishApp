package com.purva.nits.spokenenglishapp;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ViewStory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_story);
        Bundle extras = getIntent().getExtras();
        String  title= extras.getString("EXTRA_TO_STORY");
        AssetManager assetManager = getAssets();
        final TextView tv1=(TextView)findViewById(R.id.tts_story);
        TextView tv2= (TextView)findViewById(R.id.stt_story);
        Button next=(Button)findViewById(R.id.next);
        Button previous=(Button)findViewById(R.id.previous);

        try {
            BufferedReader storyReader = new BufferedReader(new InputStreamReader(assetManager.open(title + ".txt")));
            while (true) {
                String temp;
                final String prev;

                if ((temp = storyReader.readLine()) != null) {
                    tv1.setText(temp);
                    prev = temp;
                    final String nxt = storyReader.readLine();
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (nxt != null) {
                                tv1.setText(nxt);
                            }
                        }
                    });
                    previous.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (prev != null) {
                                tv1.setText(prev);
                            }
                        }
                    });
                } else {
                        break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("ERROR_MSG","Error in reading from file");
        }
    }
}
