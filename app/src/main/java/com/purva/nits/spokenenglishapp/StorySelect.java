package com.purva.nits.spokenenglishapp;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StorySelect extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_story_select);
        try {
            AssetManager assetManager = getAssets();
            BufferedReader storyListReader = new BufferedReader(new InputStreamReader(assetManager.open("Titles.txt")));
            String temp;
            String[] values;
            ArrayList<String> story=new ArrayList<>();
            int i=0;
            while ((temp=storyListReader.readLine())!=null)
            {

                String[] tempS=temp.split(",");
                for(int j=0;j<tempS.length;j++)
                {
                    story.add(tempS[j]);

                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, story);

            setListAdapter(adapter);

        }
        catch (Exception e)
        {
            Log.d("ErrorMessage","Error in Story Reader");
            e.printStackTrace();
        }
        }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        // ListView Clicked item index
        int itemPosition = position;
        // ListView Clicked item value
        String itemValue = (String) l.getItemAtPosition(position);
        Intent toStory_intent = new Intent(this, ViewStory.class);
        toStory_intent.putExtra("EXTRA_TO_STORY",itemValue);
        System.out.println("Story selected::"+itemValue);
        startActivity(toStory_intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
