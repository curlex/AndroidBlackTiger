package abt.androidblacktiger;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria on 09/11/2015.
 */
public class ABTApplication extends Application {
    public static HistoryDBHandler db;
    private boolean activeDB = false;
    private String LOG = "ABTApplication";
    @Override
    public void onCreate()
    {
        super.onCreate();
        if(!activeDB) {
            db = new HistoryDBHandler(this.getApplicationContext());
            System.out.println("onCreate ABTapp");
            initDB();
            System.out.println("onCreate after ABTapp: initDB");
            activeDB = true;
        }

    }

    protected void initDB(){
        //Context contextNew = this;
        //SQLiteDatabase  database = this.openOrCreateDatabase("HistoryDB", MODE_PRIVATE, null);
        //db.onCreate(db.getWritableDatabase());
        System.out.println("initDB start ABTapp");
        List<CoOrdinates> mockLoc = new LinkedList<CoOrdinates>();
        mockLoc.add(new CoOrdinates(0,0));
        WordHistory mock1 = new WordHistory("bus stop","Irish","staid an mbus", mockLoc,1 ,true, "image path1");
        WordHistory mock2 = new WordHistory("college","Irish", "ullscoil", mockLoc,2 ,true, "image path2");
        WordHistory mock3 = new WordHistory("shop","Irish", "siopa", mockLoc, 3 , true, "image path3");
        System.out.println("initDB try add mock data 2");
        db.addWordHistory(mock2);
        System.out.println("initDB try add mock data 1");
        System.out.print("\n\n\n\n\n\n\n\n");
        db.addWordHistory(mock1);
        System.out.println("initDB try add mock data 3");
        db.addWordHistory(mock3);
        System.out.println("initDB after mock data");

        db.close();
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