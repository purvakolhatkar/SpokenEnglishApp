package com.purva.nits.spokenenglishapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class ViewStory extends AppCompatActivity {
    private Intent speechServiceIntent;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    BufferedReader storyReader;
/**    String prv="";
    String nxt="";
    String curr="";**/
    int prv,nxt,curr,index;
    TextView tv1,tv2,tv3;
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
        tv3= (TextView)findViewById(R.id.title);
        tv3.setText(title);
       // tv2.setClickable(true);
        /**tv2.setOnClickListener(new View.OnClickListener() {             //
            @Override                                                          //
            public void onClick(View view) {
                promptSpeechInput();
            }                                                                  //
        });**/
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
            tv1.setClickable(true);
            tv1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Context context = getApplicationContext();
                    speechServiceIntent = new Intent(context, SpeechService.class);
                    speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, tv1.getText());
                    context.startService(speechServiceIntent);
                }
            });

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
            tv2.setText("");
        }

    }

    public void next(View view){
        if (nxt < story.size()) {
            tv1.setText(story.get(nxt));
            curr=nxt;
            nxt=curr+1;
            prv=curr-1;
            tv2.setText("");
        }
    }
    //////////////Speech to Text/////////////////
    public void promptSpeechInput(View view){
       Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tv2.setText(result.get(0));
                }
                break;
            }
        }
    }
        }
