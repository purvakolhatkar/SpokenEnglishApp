package com.purva.nits.spokenenglishapp;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "SpokenEnglish.db";
    private Context activity_context;
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        activity_context=context;
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
        //db.close();
        try {

            AssetManager manager=activity_context.getAssets();
            BufferedReader appdata = new BufferedReader(new InputStreamReader(manager.open("AppData.csv")));
            BufferedReader conid = new BufferedReader(new InputStreamReader(manager.open("Conversationid.csv")));
            String temp,temp1;
            int convid=0;
            while((temp1=conid.readLine())!=null)
            {
                String[] columns1=temp1.split(",");
                convid=Integer.parseInt(columns1[0]);
                db.execSQL("insert into conversationid (convid,type,title) values ("+convid+",'"+columns1[1]+"','"+columns1[2]+"')");

                while((temp=appdata.readLine())!=null)
            {
                System.out.println("Inserting from file::"+temp);
                String[] columns=temp.split(",");

                insertConversation(convid,Integer.parseInt(columns[1]),columns[2],columns[3],db);
            }
            }
        }
        catch (FileNotFoundException e)
        {
            Log.d("FileNotFound","Data file not found");
        }
        catch (IOException e)
        {
            Log.d("IOException","End of file reached or file is empty/invalid");
        }
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
    public Cursor getConversation(int convid)
    {
       // ArrayList<String> conv= new ArrayList<String>();
        System.out.println(this.getDatabaseName());
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor rec= db2.rawQuery("select * from conversations where convid="+convid+"",null);
        rec.moveToFirst();
        /**while (rec.isAfterLast()== false){
            conv.add(rec.getString(rec.getColumnIndex("sentences")));
            rec.moveToNext();
        }
 **/       db2.close();
        return rec;
    }

    public ArrayList getTitleList()
    {
        ArrayList<String> conv= new ArrayList<String>();
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor rec=db2.rawQuery("select * from conversationid",null);
        rec.moveToFirst();
        while (rec.isAfterLast()== false){
            conv.add(rec.getString(rec.getColumnIndex("title")));
            rec.moveToNext();
        }
        db2.close();
        return conv;
    }
    public int getConversationid(String title)
    {
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor rec=db2.rawQuery("select convid from conversationid where title like '"+title+"'",null);
        rec.moveToFirst();
        int convid=rec.getInt(0);
        return convid;
    }
}
