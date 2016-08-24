package com.purva.nits.spokenenglishapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "SpokenEnglish.db";
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }



    /**
     * 2 tables to save one conversation,  one contains conversation id and type,title. Example of type is daily, help,etc.Example of title can be basic introduction,asking directions etc.
     * Can be retrieved by type on one page and select one of the conversations on next page
     * Conversations consists of multiple sentences. Sentences of one conversation can be retrieved by convid and sentenceid
     * **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table conversationid "+"(convid integer primary key,type text,title text)");
        db.execSQL("create table conversations "+"(convid integer,sentid integer, sentences text,person text,foreign key(convid) references conversationid(convid))");
        Log.d("Insert:","...Inserting...");

        db.execSQL("insert into conversationid (convid,type,title) values (1,'basic', 'basic 1')");

        insertConversation(1,1,"Hello","A",db);
        insertConversation(1,2,"Hi","B",db);
        insertConversation(1,3,"How are you?","A",db);
        insertConversation(1,1,"I'm good. How are you?","B",db);
        insertConversation(1,1,"Good","A",db);
        //db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS conversationid");
        db.execSQL("DROP TABLE IF EXISTS conversations");
        onCreate(db);
    }
    //Insert into conversationid and conversations table
    public boolean insertConversation( int convid, int sentid,String sentence,String person, SQLiteDatabase db1)
    {
     //SQLiteDatabase db1 = this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        //contentValues.put("type", type);
        //long i=db.insert("conversationid", null, contentValues); //returns the primary key of the row inserted
        //Cursor rec= db.rawQuery("SELECT * FROM CONVERSATIONID WHERE CONVID = (SELECT MAX(CONVID) FROM CONVERSATIONID);",null);
        //int i=rec.getInt(1);
            ContentValues cv = new ContentValues();
            cv.put("convid", convid);
            cv.put("sentid", sentid);
            cv.put("sentences", sentence);
            cv.put("person",person);
            db1.insert("conversations", null, cv);
            return true;
    }
    // Getting all sentences in one conversation
    public ArrayList getConversation(int convid)
    {
        ArrayList<String> conv= new ArrayList<String>();
        System.out.println(this.getDatabaseName());
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor rec= db2.rawQuery("select * from conversations where convid="+convid+"",null);
        rec.moveToFirst();
        while (rec.isAfterLast()== false){
            conv.add(rec.getString(rec.getColumnIndex("sentences")));
            rec.moveToNext();
        }
        db2.close();
        return conv;
    }

}
