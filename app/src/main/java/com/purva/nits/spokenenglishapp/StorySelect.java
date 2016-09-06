package com.purva.nits.spokenenglishapp;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class StorySelect extends ListActivity {

    private static final String MORE="More...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_select);
        try {
            AssetManager assetManager = getAssets();
            BufferedReader storyListReader = new BufferedReader(new InputStreamReader(assetManager.open("Titles.txt")));
            String temp;
            ArrayList<String> story=new ArrayList<>();
            int i=0;
            while ((temp=storyListReader.readLine())!=null)
            {
                String[] tempS=temp.split(",");
                Collections.addAll(story, tempS);
            }
            story.add(MORE);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, story);

            setListAdapter(adapter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {

        super.onListItemClick(listView, view, position, id);
        // ListView Clicked item value
        String itemValue = (String) listView.getItemAtPosition(position);
        if(itemValue.equals(MORE)){
            Snackbar.make(view, "Coming soon...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            Intent toStory_intent = new Intent(this, ViewStory.class);
            toStory_intent.putExtra("EXTRA_TO_STORY",itemValue);
            System.out.println("Story selected::"+itemValue);
            startActivity(toStory_intent);
        }
    }
}
