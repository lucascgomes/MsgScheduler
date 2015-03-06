package com.example.messagesscheduler;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.messagesscheduler.MessageRecord.MessageEntry;

public class MessageRecordDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MessageRecord.db";
    
    //********** SQL QUERIES *************
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MessageEntry.TABLE_NAME + " (" +
            		MessageEntry._ID + " INTEGER PRIMARY KEY," +
            		MessageEntry.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
            		MessageEntry.COLUMN_NAME_MESSAGE_NUMBER + INTEGER_TYPE + COMMA_SEP +
            		MessageEntry.COLUMN_NAME_DATETIME + TEXT_TYPE + COMMA_SEP +
            		MessageEntry.COLUMN_NAME_WAS_SENT + INTEGER_TYPE + COMMA_SEP +
            " )";
        
    private static final String SQL_DELETE_ENTRIES =
        	    "DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME;
    
    //************* STANDARD ABSTRACT METHODS *******************
    public MessageRecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    //************* ADD MESSAGE RECORD *******************
    public void addMessageRecord(MessageRecord messageRecord){
    	Log.d("addMessage", messageRecord.toString()); 
    	
    	// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(MessageEntry.COLUMN_NAME_MESSAGE, messageRecord.getMessage()); 
        values.put(MessageEntry.COLUMN_NAME_MESSAGE_NUMBER, messageRecord.getMessage_number()); 
        values.put(MessageEntry.COLUMN_NAME_DATETIME, messageRecord.getDatetime()); 
        values.put(MessageEntry.COLUMN_NAME_WAS_SENT, messageRecord.was_sent() ? 1:0); 
        
		// 3. insert
		db.insert(MessageEntry.TABLE_NAME, null, values); 
		
		// 4. close
        db.close(); 
    }
    
    //*************** GET ALL MESSAGES SENT *****************
    public List<MessageRecord> getAllMessagesSentWith(int sent){
    	List<MessageRecord> messageRecords = new LinkedList<MessageRecord>();
    	
    	// 1. build the query
        String query = "SELECT  * FROM " + MessageEntry.TABLE_NAME + " WHERE " + MessageEntry.COLUMN_NAME_WAS_SENT + "=" + sent;
        
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        // 3. go over each row, build book and add it to list
        MessageRecord messageRecord = null;
        if (cursor.moveToFirst()) {
            do {
            	messageRecord = new MessageRecord();
            	messageRecord.setMessage(cursor.getString(1));
            	messageRecord.setMessage_number(cursor.getInt(2));
            	messageRecord.setDatetime(cursor.getString(3));
            	messageRecord.set_sent(cursor.getInt(4)==1 ? true:false);
            	
                // Add book to books
                messageRecords.add(messageRecord);
            } while (cursor.moveToNext());
        }
        
        Log.d("getAllMessages", messageRecords.toString());
        
        //4. close db and return message records
        db.close();
        return messageRecords;
    }
    
}
