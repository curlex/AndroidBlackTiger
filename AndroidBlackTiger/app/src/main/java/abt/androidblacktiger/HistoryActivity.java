package abt.androidblacktiger;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends ListActivity {

    HistoryDBHandler db = ABTApplication.db;
    public static final String wordKey = "abt.wordkey";
    public static final String transKey = "abt.transkey";
    public static final String locaKey = "abt.locakey";
    private ArrayList<WordHistory> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences langPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println("prefs");
        System.out.println(langPrefs.getAll());
        words = (ArrayList<WordHistory>) db.getAllWords();
        System.out.println(words);
        WordArrayAdapter adapter = new WordArrayAdapter(this, words);
        setListAdapter(adapter);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        WordHistory clickedWord = words.get(position);
        NewLocationNotification.notify(
                getApplicationContext(),
                clickedWord.getWord(),
                clickedWord.getTranslation(),
                clickedWord.getLocation());
        Intent intent = new Intent(getApplicationContext(), NewVocabActivity.class);
        intent.putExtra(wordKey, clickedWord.getWord());
        intent.putExtra(transKey, clickedWord.getTranslation());
        intent.putExtra(locaKey, clickedWord.getLocation());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
}
