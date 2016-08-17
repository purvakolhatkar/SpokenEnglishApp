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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table conversationid "+"(convid integer primary key autoincrement,type text)");
        db.execSQL("create table conversations "+"(convid integer,sentences text,foreign key(convid) references conversationid(convid))");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS conversationid");
        db.execSQL("DROP TABLE IF EXISTS conversations");
        onCreate(db);
    }

    public boolean insertConversation(String sentence, String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        long i=db.insert("conversationid", null, contentValues); //returns the primary key of the row inserted
        //Cursor rec= db.rawQuery("SELECT * FROM CONVERSATIONID WHERE CONVID = (SELECT MAX(CONVID) FROM CONVERSATIONID);",null);
        //int i=rec.getInt(1);

        ContentValues cv = new ContentValues();
        cv.put("convid", i);
        cv.put("sentences", sentence);
        db.insert("conversations",null,cv);
        return true;
    }

}
