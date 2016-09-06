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
    int countQ;


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
     * Conversations consists of multiple sentences. Sentences of one conversation can be retrieved by conversationId and sentenceId
     * **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table conversationTable "+"(conversationId integer primary key,type text,title text)");
        db.execSQL("create table dialogueTable "+"(conversationId integer,sentenceId integer, sentences text,person text,foreign key(conversationId) references conversationTable(conversationId))");
        db.execSQL("create table questionTable "+"(id integer primary key,question text,answer text)");
        Log.d("Insert:","...Inserting...");
        //db.close();
        try {
            AssetManager assetManager=activity_context.getAssets();
            BufferedReader conversationListReader = new BufferedReader(new InputStreamReader(assetManager.open("conversationDump.csv")));
            String tempDialog,tempConversation;
            while((tempConversation=conversationListReader.readLine())!=null)
            {
                String[] conversationColumn=tempConversation.split(",");
                int conversationId=Integer.parseInt(conversationColumn[0]);
                db.execSQL("insert into conversationTable (conversationId,type,title) values ("+conversationId+",'"+conversationColumn[1]+"','"+conversationColumn[2]+"')");

                BufferedReader dialogueListReader = new BufferedReader(new InputStreamReader(assetManager.open("dialogueDump.csv")));
                while((tempDialog=dialogueListReader.readLine())!=null)
                {
                    System.out.println("Inserting from file::"+tempDialog);
                    String[] dialogueColumn=tempDialog.split(",");

                    ////Avoid inserting invalid conversationIds
                    if(conversationColumn[0].equals(dialogueColumn[0])){
                        insertDialogues(conversationId,Integer.parseInt(dialogueColumn[1]),dialogueColumn[2],dialogueColumn[3],db);
                    }
                }
                dialogueListReader.close();  ///Close to reopen from beginning
            }
            conversationListReader.close();
            BufferedReader questionReader = new BufferedReader(new InputStreamReader(assetManager.open("questionDump.csv")));
            String q;
             countQ=0;
            while((q=questionReader.readLine())!=null)
            {
                String[] qa=q.split(",");
                System.out.println("Inserting from file::"+q);

                int id=Integer.parseInt(qa[0]);
                db.execSQL("insert into questionTable (id,question,answer) values ("+id+",'"+qa[1]+"','"+qa[2]+"')");
                countQ++;
            }
            System.out.println("CountQ::"+countQ);
            questionReader.close();
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
        db.execSQL("DROP TABLE IF EXISTS conversationTable");
        db.execSQL("DROP TABLE IF EXISTS dialogueTable");
        db.execSQL("DROP TABLE IF EXISTS questionTable");
        onCreate(db);
    }
    //Insert into conversationTable and dialogueTable table
    public boolean insertDialogues(int conversationId, int sentenceId, String sentence, String person, SQLiteDatabase db1)
    {
            ContentValues contentValues = new ContentValues();
            contentValues.put("conversationId", conversationId);
            contentValues.put("sentenceId", sentenceId);
            contentValues.put("sentences", sentence);
            contentValues.put("person",person);
            db1.insert("dialogueTable", null, contentValues);
            return true;
    }
    // Getting all sentences in one conversation
    public Cursor getDialogue(int conversationId)
    {
        System.out.println(this.getDatabaseName());
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor cursor= db2.rawQuery("select * from dialogueTable where conversationId="+conversationId+"",null);
        cursor.moveToFirst();
        db2.close();
        return cursor;
    }

    public ArrayList<String> getConversationTitles()
    {
        ArrayList<String> conversationTitles= new ArrayList<String>();
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor cursor=db2.rawQuery("select * from conversationTable",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            conversationTitles.add(cursor.getString(cursor.getColumnIndex("title")));
            cursor.moveToNext();
        }
        cursor.close();
        db2.close();
        return conversationTitles;
    }
    public int getConversations(String conversationTitle)
    {
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor cursor=db2.rawQuery("select conversationId from conversationTable where title like '"+conversationTitle+"'",null);
        cursor.moveToFirst();
        int conversationId=cursor.getInt(0);
        cursor.close();
        db2.close();
        return conversationId;
    }
   public int getCountQ(){

       return countQ;
   }
    public void setCountQ() {
        SQLiteDatabase db2=this.getReadableDatabase();
        Cursor cursor=db2.rawQuery("select max(id) from questionTable",null);
        cursor.moveToFirst();
        this.countQ=cursor.getInt(0);
        cursor.close();
    }

    public String[] getQuestionAnswer(int id)
    {
        setCountQ();
        System.out.println("Count in getQA::"+getCountQ()+" "+id);
        if(id<=getCountQ()) {
            SQLiteDatabase db2 = this.getReadableDatabase();
            System.out.println("Id found");
            Cursor cursor = db2.rawQuery("select * from questionTable where id=" + id, null);
            String[] qa = new String[3];
            cursor.moveToFirst();
            qa[0] = cursor.getString(0);
            qa[1] = cursor.getString(1);
            qa[2] = cursor.getString(2);
            cursor.close();
            return qa;
        }
        else
            return null;
    }

}
