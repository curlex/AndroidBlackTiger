package abt.androidblacktiger;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maria on 22/10/2015.
 */
public class HistoryDBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "HistoryDB";
    // Database Table
    private static final String TABLE_HISTORY = "History";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_TRANSLATION = "translation";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_SHOWN = "shown";
    public static final String COLUMN_IMAGE = "image";

    public HistoryDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_HISTORY = "CREATE TABLE " +
                TABLE_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_WORD + " TEXT,"
                + COLUMN_TRANSLATION + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_SHOWN + " INTEGER,"
                + COLUMN_IMAGE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public void addWordHistory(WordHistory wordData) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, wordData.getWord());
        values.put(COLUMN_TRANSLATION, wordData.getTranslation());
        values.put(COLUMN_LOCATION, wordData.getTranslation());
        values.put(COLUMN_SHOWN, wordData.getShown());
        values.put(COLUMN_IMAGE, wordData.getImagePath());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public WordHistory findWord(String word) {
        String query = "Select * FROM " + TABLE_HISTORY + " WHERE " + COLUMN_WORD + " =  \"" + word + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        WordHistory data = new WordHistory();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            data.setID(Integer.parseInt(cursor.getString(0)));
            data.setWord(cursor.getString(1));
            data.setTranslation(cursor.getString(2));
            data.setLocation(cursor.getString(3));
            data.setShown(Integer.parseInt(cursor.getString(4)));
            data.setImagePath(cursor.getString(5));
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }



    // Getting All Contacts
    public List<WordHistory> getAllWords() {
        List<WordHistory> wordList = new ArrayList<WordHistory>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WordHistory data = new WordHistory();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setWord(cursor.getString(1));
                data.setTranslation(cursor.getString(2));
                data.setLocation(cursor.getString(3));
                data.setShown(Integer.parseInt(cursor.getString(4)));
                data.setImagePath(cursor.getString(5));
                // Adding contact to list
                wordList.add(data);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;
    }
}


