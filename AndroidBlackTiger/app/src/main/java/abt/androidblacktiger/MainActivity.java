package abt.androidblacktiger;
//Diarmuid is best or at least he likes to think so...

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rmtheis.yandtran.ApiKeys;
import com.rmtheis.yandtran.language.Language;

public class MainActivity extends AppCompatActivity {

    String preferences_language = "setLanguage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTime();
    }

    public  void  firstTime() {

        SharedPreferences langPrefs = getSharedPreferences(Translator.preferencesLabel, 0);
        langPrefs.edit().putString(Translator.sourceLanguage, Language.ENGLISH.toString());
        langPrefs.edit().putString(Translator.destinationLanguage, Language.IRISH.toString());

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
}
