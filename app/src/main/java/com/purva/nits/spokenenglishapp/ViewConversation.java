package com.purva.nits.spokenenglishapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

public class ViewConversation extends AppCompatActivity {
    public static final String EXTRA_TO_CONV = "id";
    int convid;
    private Intent speechServiceIntent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public ViewConversation() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_conversation);
        final DBHelper dbHelper = new DBHelper(this);
        //int conversationId = Integer.parseInt(getIntent().getStringExtra(ViewConversation.EXTRA_TO_CONV));
        Bundle extras = getIntent().getExtras();
        String  id= extras.getString("EXTRA_TO_CONV");
        int convid = Integer.parseInt(id);
        System.out.println("ConID::"+convid);
        Cursor conv = dbHelper.getDialogue(convid);

        conv.moveToFirst();
      //  String s=conv.getString(conv.getColumnIndex("sentenceId"));
       // System.out.println("Sentence last"+s);
        int size=0;
        while (conv.isAfterLast()==false)
        {
            size++;
            conv.moveToNext();
        }
        conv.moveToFirst();
        System.out.println("Sentence last"+size);
        final TextView[] listSentence=new TextView[size];
        LinearLayout layout=(LinearLayout) findViewById(R.id.conversationLayout);
//        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String line = "";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        conv.moveToFirst();
        for (int i = 0; i < size && conv.isAfterLast()== false; i++) {
            listSentence[i]=new TextView(this);
            listSentence[i].setId(i+1);
            line=conv.getString(conv.getColumnIndex("person"))+": "+conv.getString(conv.getColumnIndex("sentences"));
            listSentence[i].setText(line);
            listSentence[i].setTextSize(Float.parseFloat(pref.getString("textSize","18")));
            listSentence[i].setTextAppearance(this, R.style.fontForViewConversation);
            //listSentence[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            /////Set alternate colors by even/odd logic
            if((i&1)==0){
                listSentence[i].setTextColor(Color.BLACK);
            }else{
                listSentence[i].setTextColor(Color.GRAY);
            }
            layout.addView(listSentence[i]);
            final String temp=conv.getString(conv.getColumnIndex("sentences"));
            listSentence[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Context context = getApplicationContext();
                    speechServiceIntent = new Intent(context, SpeechService.class);
                    speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, temp);
                    context.startService(speechServiceIntent);
                }
            }); 
            conv.moveToNext();
        }

        Button btnPlayConv= (Button) findViewById(R.id.playConversation);
        String line1 = "";
        conv.moveToFirst();
        for (int i = 0; i < size && conv.isAfterLast()== false; i++) {
            line1=line1+"\n"+conv.getString(conv.getColumnIndex("sentences"));
            conv.moveToNext();
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
    @Override
    public void onPause(){
        super.onPause();
        Context context = getApplicationContext();
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
        context.stopService(speechServiceIntent);
    }

}
