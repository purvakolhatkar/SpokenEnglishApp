package com.purva.nits.spokenenglishapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class ConversationSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_select);
        //TextView textView=(TextView) findViewById(R.id.tv1);
        //textView.setText("Conversations here");
       // ViewGroup layout = (ViewGroup) findViewById(R.id.content_conversation_select);
       // layout.addView(textView);
      }
/** Start of file handling**/
    public void createFile(View view)
    {
        String FILENAME = "hello_file";
        String string = "hello world!";
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void readFile(View view) {
        TextView tv;
        try {
            BufferedReader input=null;
            File file=null;
            file=new File(getCacheDir(),"hello_file");
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line,line1;
            line1=null;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
                line1=line1+line;
            }
            tv=(TextView) findViewById(R.id.tv1);
            int i=buffer.length();
            tv.setText(line1);
            //ViewGroup layout = (ViewGroup) findViewById(R.id.content_conversation_select);
            //layout.addView(tv);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        }

    /** End of File handling**/
}
