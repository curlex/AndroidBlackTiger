package abt.androidblacktiger;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 09/11/2015.
 */
public class ABTApplication extends Application {
    public static HistoryDBHandler db;

    @Override
    public void onCreate()
    {
        super.onCreate();
        db = new HistoryDBHandler(this.getApplicationContext());
        initDB();
        // Initialize the singletons so their instances
        // are bound to the application process.

    }

    protected void initDB(){
        Context contextNew = this;
        SQLiteDatabase  database = this.openOrCreateDatabase("HistoryDB", MODE_PRIVATE, null);
        //db.onCreate(database);
        List<CoOrdinates> mockLoc = new LinkedList<CoOrdinates>();
        mockLoc.add(new CoOrdinates(0,0));
        WordHistory mock1 = new WordHistory("bus stop","Irish","staid an mbus", mockLoc,1 ,true, "image path1");
        WordHistory mock2 = new WordHistory("college","Irish", "ullscoil", mockLoc,2 ,true, "image path2");
        WordHistory mock3 = new WordHistory("shop","Irish", "siopa", mockLoc, 3 , true, "image path3");
        db.addWordHistory(mock1);
        db.addWordHistory(mock2);
        db.addWordHistory(mock3);


    }

     public HistoryDBHandler getDB() {
        return db;
    }

    @Override
    public void  onTerminate(){
        super.onTerminate();
        db.close();

    }


}