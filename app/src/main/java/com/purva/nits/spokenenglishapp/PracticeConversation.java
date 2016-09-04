package com.purva.nits.spokenenglishapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class PracticeConversation extends AppCompatActivity {
    private Intent speechServiceIntent;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    DBHelper dbHelper=new DBHelper(this);
    int curr=0,prv=0,nxt=0;
    String[] qa;
    TextView tv1,tv2,tv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_practice_conversation);
        curr=1;
        prv=1;
        qa=dbHelper.getQuestionAnswer(curr);
        tv1=(TextView) findViewById(R.id.question);
        tv2=(TextView) findViewById(R.id.answer);
        tv3=(TextView) findViewById(R.id.replyA);
        nxt=2;
        tv1.setText(qa[1]);
        tv2.setText("");
        tv3.setText("");
        tv1.setClickable(true);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                speechServiceIntent = new Intent(context, SpeechService.class);
                speechServiceIntent.putExtra(SpeechService.EXTRA_TO_SPEAK, tv1.getText());
                context.startService(speechServiceIntent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tv2.setText(qa[2]);
                    tv3.setText(result.get(0));
                }
                break;
            }
        }
    }
    public void nextQ(View view) {
        if (nxt <= dbHelper.getCountQ())
        {
            qa = dbHelper.getQuestionAnswer(nxt);
        curr = nxt;
        prv = curr - 1;
        nxt = curr + 1;
        if (!qa[1].isEmpty())
            tv1.setText(qa[1]);
        tv2.setText("");
        tv3.setText("");
    }
    }

    public void previousQ(View view)
    {
     if(prv>0)
     {
        qa=dbHelper.getQuestionAnswer(prv);
        curr=prv;
        prv=curr-1;
        nxt=curr+1;
        if(!qa[1].isEmpty())
            tv1.setText(qa[1]);
         tv2.setText("");
        tv3.setText("");

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

}
