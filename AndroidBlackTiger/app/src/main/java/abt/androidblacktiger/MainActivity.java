package abt.androidblacktiger;
//Diarmuid is best or at least he likes to think so...

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String preferences_language = "setLanguage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstTime();
    }

    public  void  firstTime() {

        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.pref_general, false);

        SharedPreferences sharedTime = getSharedPreferences(preferences_language, 0);
        if (sharedTime.getBoolean("setLanguage", true)) {
            //Call Language Set Activity
            sharedTime.edit().putBoolean("setLanguage", false).apply();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        } else {
            //Call Main Menu Activity
            startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
        }
    }
// :)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//hi
    @Override
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
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
    //Testing

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
