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
import java.util.ArrayList;

public class ViewStory extends AppCompatActivity {
    BufferedReader storyReader;
/**    String prv="";
    String nxt="";
    String curr="";**/
    int prv,nxt,curr,index;
    TextView tv1,tv2;
    ArrayList<String> story=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_story);
        Bundle extras = getIntent().getExtras();
        String  title= extras.getString("EXTRA_TO_STORY");
        AssetManager assetManager = getAssets();
        Button next=(Button)findViewById(R.id.next);
        Button previous=(Button)findViewById(R.id.previous);
        tv1=(TextView)findViewById(R.id.tts_story);
        tv2= (TextView)findViewById(R.id.stt_story);
        index=0;
        try {
            storyReader = new BufferedReader(new InputStreamReader(assetManager.open(title + ".txt")));
                String temp;

                while ((temp = storyReader.readLine()) != null) {
                    story.add(temp);
                }
            if (!story.isEmpty()) {
                tv1.setText(story.get(0));
                prv = 0;
                curr = 0;
                nxt = 1;
            }

        }

        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("ERROR_MSG","Error in reading from file");
        }
    }

    public void prev(View view){
        if (prv >= 0) {

            tv1.setText(story.get(prv));
            nxt=curr;
            curr=prv;
            prv=prv-1;

        }

    }

    public void next(View view){
        if (nxt < story.size()) {
            tv1.setText(story.get(nxt));
            curr=nxt;
            nxt=curr+1;
            prv=curr-1;
        }
    }
}
