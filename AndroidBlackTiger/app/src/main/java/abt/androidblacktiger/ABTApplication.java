package abt.androidblacktiger;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Maria on 09/11/2015.
 */
public class ABTApplication extends Application {
    public static HistoryDBHandler db;

    @Override
    public void onCreate()
    {
        super.onCreate();
        db = new HistoryDBHandler(this);
        initDB();
        // Initialize the singletons so their instances
        // are bound to the application process.

    }

    protected void initDB(){
        Context contextNew = this;
        SQLiteDatabase  database = this.openOrCreateDatabase("HistoryDB", MODE_PRIVATE, null);
        //db.onCreate(database);
        WordHistory mock1 = new WordHistory(0,"bus stop", "staid an mbus", "ucd", 2, "image path1");
        WordHistory mock2 = new WordHistory(1,"college", "ullscoil", "ucd", 2, "image path2");
        WordHistory mock3 = new WordHistory(2,"shop", "siopa", "ucd", 2, "image path3");
        db.addWordHistory(mock1);
        db.addWordHistory(mock2);
        db.addWordHistory(mock3);

    }

     public HistoryDBHandler getDB() {
        return db;
    }
}