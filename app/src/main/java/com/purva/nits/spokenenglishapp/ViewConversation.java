package com.purva.nits.spokenenglishapp;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class ViewConversation extends AppCompatActivity {
    public static final String EXTRA_TO_CONV = "id";
    int convid;
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
        //int convid = Integer.parseInt(getIntent().getStringExtra(ViewConversation.EXTRA_TO_CONV));
        Bundle extras = getIntent().getExtras();
         String  id= extras.getString("EXTRA_TO_CONV");
        int convid = Integer.parseInt(id);
        System.out.println("ConID::"+convid);
        Cursor conv = dbHelper.getConversation(convid);
        conv.moveToFirst();
      //  String s=conv.getString(conv.getColumnIndex("sentid"));
       // System.out.println("Sentence last"+s);
        int size=0;
        while (conv.isAfterLast()==false)
        {
            size++;
            conv.moveToNext();
        }
        conv.moveToFirst();
        System.out.println("Sentence last"+size);
        TextView[] listSentence=new TextView[size];
        LinearLayout layout=(LinearLayout)findViewById(R.id.content_conversation_select);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String line = "";
        conv.moveToFirst();
        for (int i = 0; i < size && conv.isAfterLast()== false; i++) {
            listSentence[i]=new TextView(this);
            listSentence[i].setId(i+1);
            line=conv.getString(conv.getColumnIndex("person"))+"::"+conv.getString(conv.getColumnIndex("sentences"));
            listSentence[i].setText(line);
            layout.addView(listSentence[i]);
            conv.moveToNext();
        }
        //textView.setText(line);
       }


}
