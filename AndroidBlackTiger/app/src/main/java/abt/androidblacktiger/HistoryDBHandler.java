package abt.androidblacktiger;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 22/10/2015.
 */
public class HistoryDBHandler extends SQLiteOpenHelper {
    private static HistoryDBHandler db;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "HistoryDB";
    // Database Table
    private static final String TABLE_HISTORY = "History";
    // Locations Table
    private static final String TABLE_LOCATIONS = "Locations";


    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_LANG = "language";
    public static final String COLUMN_TRANSLATION = "translation";
    public static final String COLUMN_LOCATION_X = "locationX";
    public static final String COLUMN_LOCATION_Y = "locationY";
    public static final String COLUMN_SHOWN = "shown";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_AGAIN = "again";
    public static final String COMPOSITE_KEY_HIST = "PRIMARY KEY ("+COLUMN_WORD+", "+COLUMN_LANG+")";
    public static final String COMPOSITE_KEY_LOC = "PRIMARY KEY ("+COLUMN_WORD+", "+COLUMN_LOCATION_X+", "+COLUMN_LOCATION_Y+")";

    public HistoryDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("Constructor  db handler");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Creating DB in onCreate");
        String CREATE_TABLE_HISTORY = "CREATE TABLE " +
                TABLE_HISTORY + "("
                + COLUMN_WORD + " TEXT,"
                + COLUMN_LANG + " TEXT,"
                + COLUMN_TRANSLATION + " TEXT,"
                + COLUMN_SHOWN + " INTEGER,"
                + COLUMN_AGAIN + " INTEGER,"
                + COLUMN_IMAGE + " TEXT,"
                + COMPOSITE_KEY_HIST
                + ")";
        db.execSQL(CREATE_TABLE_HISTORY);

        String CREATE_TABLE_LOCATIONS= "CREATE TABLE " +
                TABLE_LOCATIONS + "("
                + COLUMN_WORD + " TEXT,"
                + COLUMN_LOCATION_X +" REAL,"
                + COLUMN_LOCATION_Y +" REAL,"
                + COMPOSITE_KEY_LOC
                + ")";
        db.execSQL(CREATE_TABLE_LOCATIONS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        onCreate(db);
    }

    public void addWordHistory(WordHistory wordData) {
        System.out.println("Add word history");
        addWord(wordData);
        System.out.println("Add locations");
        addLocations(wordData.getWord(), wordData.getLocations());
    }



    private void addWord(WordHistory wordData){
        System.out.println("In addWord");
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null)  System.out.println("DB is null");
        System.out.println("Got writeable db");
        try{
            WordHistory found = findWord(wordData.getWord(),wordData.getLang());
            wordData.setShown(found.getShown()+1);
            /**
             *   ContentValues args = new ContentValues();
             args.put(KEY_NAME, name);
             args.put(KEY_EMAIL, email);
             return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
             */

            ContentValues values = new ContentValues();
            //values.put(COLUMN_WORD, wordData.getWord());
            // values.put(COLUMN_LANG, wordData.getLang());
            values.put(COLUMN_TRANSLATION, wordData.getTranslation());
            values.put(COLUMN_SHOWN, wordData.getShown());
            if (wordData.getAgain() == true ) values.put(COLUMN_AGAIN, 1);
            else values.put(COLUMN_AGAIN, 0);
            values.put(COLUMN_IMAGE, wordData.getImagePath());
            if (db != null) {
                System.out.println("Updating Row");
                db.update(TABLE_HISTORY, values,
                                COLUMN_WORD + " =  \"" + wordData.getWord() + "\"" +
                                " AND "+ COLUMN_LANG +  " = \"" + wordData.getLang()+"\"", null );
            }
            else System.out.println("DB was null");
            /*long rows = db.update(TABLE_HISTORY, values, COLUMN_SHOWN+"=",
                    new String[] {((Integer) (findWord(wordData.getWord(),wordData.getLang()).getShown()+1)).toString()});
            System.out.println("Update Word in DB");*/
        }
        catch (Exception e) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_WORD, wordData.getWord());
            values.put(COLUMN_LANG, wordData.getLang());
            values.put(COLUMN_TRANSLATION, wordData.getTranslation());
            values.put(COLUMN_SHOWN, wordData.getShown());
            if (wordData.getAgain() == true ) values.put(COLUMN_AGAIN, 1);
            else values.put(COLUMN_AGAIN, 0);
            values.put(COLUMN_IMAGE, wordData.getImagePath());
            System.out.println("New Word for DB");




            db.insert(TABLE_HISTORY, null, values);
            System.out.print("close");

        }
        finally {
            //db.close();
        }
    }

    private void addLocations(String word, List<CoOrdinates> locations){

        for(CoOrdinates loc : locations){
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_WORD, word);
                values.put(COLUMN_LOCATION_X, loc.getLat());
                values.put(COLUMN_LOCATION_Y, loc.getLng());
                db.insert(TABLE_LOCATIONS, null, values);

            }
            catch (Exception e){
                System.out.println("Location already in DB");
            }
            finally {
                //db.close();
            }

        }
    }

    public WordHistory findWord(String word, String lang) {
        WordHistory result = queryWord(word, lang);
        List<CoOrdinates> locations = queryLoc(word);
        result.setLocations(locations);
        return result;
    }


    private WordHistory queryWord(String word, String lang){
        String query = "Select * FROM " + TABLE_HISTORY + " WHERE "
                                        + COLUMN_WORD + " =  \"" + word + "\""
                                        + " AND "
                                        + COLUMN_LANG + " =  \"" + lang + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        WordHistory data = new WordHistory();
        data.setLocations(null);
        if (cursor.moveToFirst()) {
            data.setWord(cursor.getString(0));
            data.setLang(cursor.getString(1));
            data.setTranslation(cursor.getString(2));;
            data.setShown(Integer.parseInt(cursor.getString(3)));
            if (Integer.parseInt(cursor.getString(4)) == 1) data.setAgain(true);
            else data.setAgain(false);
            data.setImagePath(cursor.getString(5));

        } else {
            data = null;
        }
        cursor.close();
        //db.close();
        List<CoOrdinates> loc = queryLoc(word);
        data.setLocations(loc);
        return data;
    }


    private List<CoOrdinates> queryLoc(String word){
        String query = "Select * FROM " + TABLE_LOCATIONS + " WHERE " + COLUMN_WORD + " =  \"" + word + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<CoOrdinates> data = new LinkedList<CoOrdinates>();
        if (cursor.moveToFirst()) {
            do {
                double x = cursor.getDouble(1);
                double y = cursor.getDouble(2);
                data.add(new CoOrdinates(x,y));
            } while (cursor.moveToNext());

        } else {
            data = null;
        }
        cursor.close();
        //db.close();
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
                data.setWord(cursor.getString(0));
                data.setLang(cursor.getString(1));
                data.setTranslation(cursor.getString(2));;
                data.setShown(Integer.parseInt(cursor.getString(3)));
                if (Integer.parseInt(cursor.getString(4)) == 1) data.setAgain(true);
                else data.setAgain(false);
                data.setImagePath(cursor.getString(5));
                List<CoOrdinates> loc = queryLoc(data.getWord());
                data.setLocations(loc);
                wordList.add(data);
            } while (cursor.moveToNext());

        }
        cursor.close();
        // return contact list
        //db.close();
        return wordList;
    }
}


