package com.purva.nits.spokenenglishapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    int previous, next, current,index;
    TextView ttsStoryView, sttStoryView, storyTitleView;
    ArrayList<String> story=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_story);
        setupActionBar();
        Bundle extras = getIntent().getExtras();
        String  title= extras.getString("EXTRA_TO_STORY");
        AssetManager assetManager = getAssets();
        Button next=(Button)findViewById(R.id.next);
        Button previous=(Button)findViewById(R.id.previous);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ttsStoryView =(TextView)findViewById(R.id.tts_story);
        ttsStoryView.setTextSize(Float.parseFloat(pref.getString("textSize","18")));
        sttStoryView = (TextView)findViewById(R.id.stt_story);
        sttStoryView.setTextSize(Float.parseFloat(pref.getString("textSize","18")));
        storyTitleView = (TextView)findViewById(R.id.storyTitle);
        storyTitleView.setText(title);
        index=0;
        try {
            storyReader = new BufferedReader(new InputStreamReader(assetManager.open(title + ".txt")));
                String temp;

                while ((temp = storyReader.readLine()) != null) {
                    story.add(temp);
                }
            if (!story.isEmpty()) {
                ttsStoryView.setText(story.get(0));
                this.previous = 0;
                current = 0;
                this.next = 1;
            }
            ttsStoryView.setClickable(true);
            ttsStoryView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Context context = getApplicationContext();
                    speechServiceIntent = new Intent(context, SpeechService.class);
                    speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, ttsStoryView.getText());
                    context.startService(speechServiceIntent);
                }
            });

        }

        catch (IOException e)
        {
            e.printStackTrace();
          }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void prev(View view){
        if (previous >= 0) {

            ttsStoryView.setText(story.get(previous));
            next = current;
            current = previous;
            previous = previous -1;
            sttStoryView.setText("");
        }
        Context context = getApplicationContext();
        if (speechServiceIntent!=null)
            context.stopService(speechServiceIntent);

    }

    public void next(View view){
        if (next < story.size()) {
            ttsStoryView.setText(story.get(next));
            current = next;
            next = current +1;
            previous = current -1;
            sttStoryView.setText("");
        }
        Context context = getApplicationContext();
        if (speechServiceIntent!=null)
            context.stopService(speechServiceIntent);
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
                    sttStoryView.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Context context = getApplicationContext();
        if (speechServiceIntent!=null)
        context.stopService(speechServiceIntent);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
        @Override
    public void onDestroy(){
        super.onDestroy();
        Context context = getApplicationContext();
            if (speechServiceIntent!=null)
                context.stopService(speechServiceIntent);
    }
}
