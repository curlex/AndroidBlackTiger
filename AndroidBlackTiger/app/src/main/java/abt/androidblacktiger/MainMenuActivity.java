package abt.androidblacktiger;

/**
 * Created by Ciarán on 14/10/2015.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        setTitle(R.string.title_activity_main_menu);
        firstTime();

    }




    public  void  firstTime() {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.pref_general, false);
        String preferences_language = "setLanguage";
        SharedPreferences sharedTime = getSharedPreferences(preferences_language, 0);
        if (sharedTime.getBoolean("setLanguage", true)) {
            //Call Language Set Activity
            startActivity(new Intent(MainMenuActivity.this, WelcomeActivity.class));
        } else {
            ImageButton button_word_history = (ImageButton) findViewById(R.id.word_history_button);
            ImageButton button_map_discovery = (ImageButton) findViewById(R.id.map_discovery_button);
            button_map_discovery.setBackgroundResource(R.drawable.mao); // name map gave error :P
            if(!isMyServiceRunning(GPS.class)) {
                startService(new Intent(this, GPS.class));
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OpenNewActivity(View view)
    {
        Intent intent = null;
        if(view.getId()==R.id.word_history_button)
        {
            intent = new Intent(this, HistoryActivity.class);
        }
        else
        {
            //Replace MainMenuActivity.class with the name of activity corresponding to Map
            intent = new Intent(this, DiscoverMap.class);
        }
        if(intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}