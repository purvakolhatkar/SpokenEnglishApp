package com.purva.nits.spokenenglishapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import com.purva.nits.spokenenglishapp.R;

public class ConversationSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_select);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Conversations here");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_conversation_select);
        layout.addView(textView);
    }
}
