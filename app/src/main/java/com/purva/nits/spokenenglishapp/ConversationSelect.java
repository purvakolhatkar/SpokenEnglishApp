package com.purva.nits.spokenenglishapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConversationSelect extends ListActivity {

    private static final String MORE="More...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_select);
        DBHelper dbHelper=new DBHelper(this);
        final ArrayList<String> convs=dbHelper.getConversationTitles();
        TextView titleView=(TextView) findViewById(R.id.conversationListTitle);
        titleView.setText(R.string.conversationPracticePage);
        String[] values=new String[convs.size()+1];
        int i=0;
        for(;i<convs.size();i++)
        {
            values[i]=convs.get(i);
        }
        values[i]=MORE;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {

        super.onListItemClick(listView, view, position, id);
        // ListView Clicked item index
        DBHelper dbHelper=new DBHelper(this);
        // ListView Clicked item value
        String itemValue = (String) listView.getItemAtPosition(position);
        if(itemValue.equals(MORE)){
            Snackbar.make(view, "Coming soon...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            int temp1=dbHelper.getConversations(itemValue);
            toConversation(temp1);
        }
    }

    public void toConversation(int conversationId) {
        Intent toConversation_intent = new Intent(this, ViewConversation.class);
        String s=String.valueOf(conversationId);
        toConversation_intent.putExtra("EXTRA_TO_CONV",s);
        System.out.println("Conversation selected::"+s);
        startActivity(toConversation_intent);
    }
}
