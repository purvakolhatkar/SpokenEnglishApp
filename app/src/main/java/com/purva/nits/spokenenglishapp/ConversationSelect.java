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
        TextView textView=(TextView) findViewById(R.id.conversationListTitle);
        textView.setText(R.string.conversationPracticePage);
        String[] values=new String[convs.size()+1];
        int i=0;
        for(;i<convs.size();i++)
        {
            values[i]=convs.get(i);
        }
        values[i]=MORE;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);
        /**LinearLayout layout=(LinearLayout)findViewById(R.id.content_conversation_select);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView[] listConv=new TextView[convs.size()];
        String line="";
        String list="Conversation";

        for(int i=1; i<=convs.size();i++)
        {
            final int temp=i;
            line=list+" "+i+"::"+convs.get(i-1);
            listConv[i-1]=new TextView(this);
          //  listConv[i-1].setLayoutParams(lp);
            listConv[i-1].setId(i+1);
            listConv[i-1].setText(line);
            //listConv[i-1].setMovementMethod(LinkMovementMethod.getInstance());
            listConv[i-1].setClickable(true);
            layout.addView(listConv[i-1]);
            line="";
            final int temp1=dbHelper.getConversations(convs.get(temp-1));
            System.out.println("Conversation id::"+temp1);
            listConv[i-1].setOnClickListener(new View.OnClickListener(){
                public void onClick(View c)
                {
                    toConversation(temp1);

                }
            });
        }**/
        //textView.setText(line);
        /**ArrayList<String> conv=dbHelper.getDialogue(1);
       TextView textView=(TextView) findViewById(R.id.ttsStoryView);
        String line="";
        for (int i=0;i<conv.size();i++)
        {
            line=line+conv.get(i);
            line=line+"\n";
        }
        textView.setText(line);**/
        //textView.setText("Conversations here");
       // ViewGroup layout = (ViewGroup) findViewById(R.id.content_conversation_select);
       // layout.addView(textView);
      }

    private void setupActionBar() {
        ActionBar actionBar = new AppCompatActivity().getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {

        super.onListItemClick(listView, view, position, id);
        // ListView Clicked item index
        //int itemPosition = position;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
