package abt.androidblacktiger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HistoryActivity extends AppCompatActivity {

    public String wordKey = "abt.wordkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences langPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println("prefs");
        System.out.println(langPrefs.getAll());
        HistoryDBHandeler historyDBHandeler = new HistoryDBHandeler(getApplicationContext());

        final ArrayList<String> words = new ArrayList<>();
        String house;
        try {
            house = new Translator().execute(new TranslatorParams(getApplicationContext(), "house"), new TranslatorParams(getApplicationContext(), "house")).get().get(0);
            words.add(house);
        } catch (InterruptedException | ExecutionException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "An error was encountered running the translation", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }
        words.add("house");
        words.add("car");
        words.add("field");
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.textviewlay, words);
        ListView lv = (ListView) findViewById(R.id.history_listview);
        lv.setAdapter(adapter);
        final HistoryActivity historyActivity = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(historyActivity, NewVocabActivity.class);
                String clickedWord = words.get(position);
                NewLocationNotification.notify(historyActivity,clickedWord, "teach1");
                intent.putExtra(wordKey, clickedWord);
                startActivity(intent);
            }
        });
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
