package com.purva.nits.spokenenglishapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ViewConversation extends AppCompatActivity {
    public static final String EXTRA_TO_CONV = "id";
    private Intent speechServiceIntent;


    public ViewConversation() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_conversation);
        setupActionBar();
        final DBHelper dbHelper = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        String  id= extras.getString("EXTRA_TO_CONV");
        int conversationId = Integer.parseInt(id);
        System.out.println("ConID::"+conversationId);
        Cursor conversationCursor = dbHelper.getDialogue(conversationId);
        conversationCursor.moveToFirst();
        int size=0;
        while (!conversationCursor.isAfterLast())
        {
            size++;
            conversationCursor.moveToNext();
        }
        conversationCursor.moveToFirst();
        System.out.println("Sentence last"+size);
        final TextView[] listSentence=new TextView[size];
        LinearLayout layout=(LinearLayout) findViewById(R.id.conversationLayout);
        String line;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        conversationCursor.moveToFirst();
        for (int i = 0; i < size && !conversationCursor.isAfterLast(); i++) {
            listSentence[i]=new TextView(this);
            listSentence[i].setId(i+1);
            line=conversationCursor.getString(conversationCursor.getColumnIndex("person"))+": "+conversationCursor.getString(conversationCursor.getColumnIndex("sentences"));
            listSentence[i].setText(line);
            listSentence[i].setTextSize(Float.parseFloat(pref.getString("textSize","18")));
            listSentence[i].setTextAppearance(this, R.style.fontForViewConversation);
            /////Set alternate colors by even/odd logic
            if((i&1)==0){
                listSentence[i].setTextColor(Color.BLACK);
            }else{
                listSentence[i].setTextColor(Color.GRAY);
            }
            layout.addView(listSentence[i]);
            final String temp=conversationCursor.getString(conversationCursor.getColumnIndex("sentences"));
            listSentence[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Context context = getApplicationContext();
                    speechServiceIntent = new Intent(context, SpeechService.class);
                    speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, temp);
                    context.startService(speechServiceIntent);
                }
            }); 
            conversationCursor.moveToNext();
        }

        Button btnPlayConv= (Button) findViewById(R.id.playConversation);
        String line1 = "";
        conversationCursor.moveToFirst();
        for (int i = 0; i < size && !conversationCursor.isAfterLast(); i++) {
            line1=line1+"\n"+conversationCursor.getString(conversationCursor.getColumnIndex("sentences"));
            conversationCursor.moveToNext();
        }
        final String readconversation=line1;

        btnPlayConv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = getApplicationContext();
                speechServiceIntent = new Intent(context, SpeechService.class);
                speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, readconversation);
                context.startService(speechServiceIntent);
            }
        });

        //textView.setText(line);
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
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
