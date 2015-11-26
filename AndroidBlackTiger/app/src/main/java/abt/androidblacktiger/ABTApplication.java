package abt.androidblacktiger;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maria.
 */
public class ABTApplication extends Application {
    public static HistoryDBHandler db;
    private boolean activeDB = false;
    @Override
    public void onCreate()
    {
        super.onCreate();
        if(!activeDB) {
            db = new HistoryDBHandler(this.getApplicationContext());
            //initDB();
            activeDB = true;
        }

    }

    protected void initDB(){
        List<CoOrdinates> mockLoc = new LinkedList<>();
        mockLoc.add(new CoOrdinates(0, 1));
        WordHistory mock1 = new WordHistory("bus stop","ga","staid an mbus", mockLoc,1 ,true, "imagepath1");
        db.addWordHistory(mock1);
        List<CoOrdinates> mockLoc2 = new LinkedList<>();
        mockLoc2.add(new CoOrdinates(0,2));
        WordHistory mock2 = new WordHistory("college","ga", "ullscoil", mockLoc2,2 ,true, "imagepath2");
        db.addWordHistory(mock2);
        List<CoOrdinates> mockLoc3 = new LinkedList<>();
        mockLoc3.add(new CoOrdinates(0,3));
        WordHistory mock3 = new WordHistory("shop","ga", "siopa", mockLoc3, 3 , true, "imagepath3");
        db.addWordHistory(mock3);
        List<CoOrdinates> mockLoc4 = new LinkedList<>();
        mockLoc4.add(new CoOrdinates(0,4));
        WordHistory mock4 = new WordHistory("airport","ga", "aerfort", mockLoc4, 3 , true, "imagepath3");
        db.addWordHistory(mock4);
        List<CoOrdinates> mockLoc5 = new LinkedList<>();
        mockLoc5.add(new CoOrdinates(0,5));
        WordHistory mock5 = new WordHistory("bank","ga", "banc", mockLoc5, 3 , true, "imagepath3");
        db.addWordHistory(mock5);
        List<CoOrdinates> mockLoc6 = new LinkedList<>();
        mockLoc6.add(new CoOrdinates(0,6));
        WordHistory mock6 = new WordHistory("post office","ga", "oifig an phoist", mockLoc6, 3 , true, "imagepath3");
        db.addWordHistory(mock6);
        List<CoOrdinates> mockLoc7 = new LinkedList<>();
        mockLoc7.add(new CoOrdinates(0,7));
        WordHistory mock7 = new WordHistory("doctor","ga", "doctúir", mockLoc7, 3 , true, "imagepath3");
        db.addWordHistory(mock7);
        List<CoOrdinates> mockLoc8 = new LinkedList<>();
        mockLoc8.add(new CoOrdinates(0,8));
        WordHistory mock8 = new WordHistory("school","ga", "scoil", mockLoc8, 3 , true, "imagepath3");
        db.addWordHistory(mock8);
        List<CoOrdinates> mockLoc9 = new LinkedList<>();
        mockLoc9.add(new CoOrdinates(0,9));
        WordHistory mock9 = new WordHistory("train station","ga", "staisúin traenach", mockLoc9, 3 , true, "imagepath3");
        db.addWordHistory(mock9);
        List<CoOrdinates> mockLoc10 = new LinkedList<>();
        mockLoc10.add(new CoOrdinates(0,10));
        WordHistory mock10 = new WordHistory("florist","ga", "bláthadóir", mockLoc10, 3 , true, "imagepath3");
        db.addWordHistory(mock10);
        List<CoOrdinates> mockLoc11 = new LinkedList<>();
        mockLoc11.add(new CoOrdinates(0,11));
        WordHistory mock11 = new WordHistory("mosque","ga", "mosc", mockLoc11, 3 , true, "imagepath3");
        db.addWordHistory(mock11);
        List<CoOrdinates> mockLoc12 = new LinkedList<>();
        mockLoc12.add(new CoOrdinates(0,12));
        WordHistory mock12 = new WordHistory("bowling alley","ga", "lána babhlála", mockLoc12, 3 , true, "imagepath3");
        db.addWordHistory(mock12);
        List<CoOrdinates> mockLoc13 = new LinkedList<>();
        mockLoc13.add(new CoOrdinates(0,13));
        WordHistory mock13 = new WordHistory("bakery","ga", "bácús", mockLoc13, 3 , true, "imagepath3");
        db.addWordHistory(mock13);
        List<CoOrdinates> mockLoc14 = new LinkedList<>();
        mockLoc14.add(new CoOrdinates(0,14));
        WordHistory mock14 = new WordHistory("laundry","ga", "neachtlann", mockLoc14, 3 , true, "imagepath3");
        db.addWordHistory(mock14);
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