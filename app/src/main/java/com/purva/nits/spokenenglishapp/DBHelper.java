package com.purva.nits.spokenenglishapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "SpokenEnglish.db";
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }
    /**
     * 2 tables to save one conversation,  one contains conversation id and type. Example of type can be basic introduction,asking help etc.
     * Can be retrieved by type on one page and select one of the conversations on next page
     * Conversations consists of multiple sentences. Sentences of one conversation can be retrieved by convid and sentenceid
     * **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table conversationid "+"(convid integer primary key autoincrement,type text)");
        db.execSQL("create table conversations "+"(convid integer,sentid integer, sentences text,foreign key(convid) references conversationid(convid))");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS conversationid");
        db.execSQL("DROP TABLE IF EXISTS conversations");
        onCreate(db);
    }
    //Insert into conversationid and conversations table
    public boolean insertConversation(String sentence, String type, int numberoflines)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        long i=db.insert("conversationid", null, contentValues); //returns the primary key of the row inserted
        //Cursor rec= db.rawQuery("SELECT * FROM CONVERSATIONID WHERE CONVID = (SELECT MAX(CONVID) FROM CONVERSATIONID);",null);
        //int i=rec.getInt(1);
        for (int j=0;j<numberoflines;j++) {
            ContentValues cv = new ContentValues();
            cv.put("convid", i);
            cv.put("sentid", j);
            cv.put("sentences", sentence);
            db.insert("conversations", null, cv);
        }
            return true;
    }
    // Getting all sentences in one conversation
    public Cursor getConversation(int convid)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor rec= db.rawQuery("select * from conversations where convid="+convid+"",null);
        return rec;
    }

}
