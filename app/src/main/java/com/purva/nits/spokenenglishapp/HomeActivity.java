package com.purva.nits.spokenenglishapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private static final int RESULT_SETTINGS = 1;
    private static final int TTS_CHECK_CODE = 101;
    private static final String TTS_MISSING_MESSAGE="Please install TextToSpeech Service";

    Button storyButton;
    TextView storiesSummary;

    private Intent speechServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storyButton=(Button) findViewById(R.id.storyButton);
        storiesSummary=(TextView) findViewById(R.id.storiesSummary);

        Button moreButton = (Button) findViewById(R.id.moreDummy1);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming soon...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        moreButton = (Button) findViewById(R.id.moreDummy2);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming soon...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        ///////Intent to check if TTS Data is available else direct to download it//////////
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_CHECK_CODE);
    }

    //Called when user clicks conversation
    public void startConversationActivity(View view){
        Intent intent = new Intent(this, ConversationSelect.class);
        startActivity(intent);
    }
    public void startStory(View view){
        Intent intent = new Intent(this, StorySelect.class);
        startActivity(intent);
    }
    public void startDialogue(View view){
        Intent intent = new Intent(this, PracticeConversation.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case TTS_CHECK_CODE: {///////Checking TTS Data/////////
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    //success, create the TTS instance
                } else {
                    //missing data, install it
                    Intent installIntent = new Intent();
                    Toast.makeText(getApplicationContext(), TTS_MISSING_MESSAGE, Toast.LENGTH_SHORT).show();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                break;
            }
            case RESULT_SETTINGS: {
                showUserSettings();
                break;
            }
        }
    }

    private void showUserSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //Calling SpeechService
    public void speak() {
        Context context = getApplicationContext();
        speechServiceIntent = new Intent(context, SpeechService.class);
        speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, storiesSummary.getText().toString());
        context.startService(speechServiceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }
        if(id==R.id.action_about){
            Intent i = new Intent(this,AboutActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
