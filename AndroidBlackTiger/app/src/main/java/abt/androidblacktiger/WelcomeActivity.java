package abt.androidblacktiger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Maria on 14/10/2015.
 */
public class WelcomeActivity extends Activity implements AdapterView.OnItemSelectedListener {
    HistoryDBHandler db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Spinner spinner = (Spinner) findViewById(R.id.language_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pref_language_titles, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //db = ((ABTApplication)getApplication()).getDB();

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


    public void OpenNewActivity(View view)
    {
        Intent intent = null;
        if(view.getId()==R.id.save_button)
        {
            String preferences_language = "setLanguage";
            SharedPreferences sharedTime = getSharedPreferences(preferences_language, 0);
            sharedTime.edit().putBoolean("setLanguage", false).apply();
            intent = new Intent(this, MainMenuActivity.class);
        }
        if(intent != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences langPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String option =  parent.getItemAtPosition(position).toString();
        String[] values = getResources().getStringArray(R.array.pref_language_values);
        String lang = values[position];
        SharedPreferences.Editor editor = langPrefs.edit();
        editor.putString(getString(R.string.preference_language), lang);
        editor.apply();
        System.out.println(langPrefs.getAll());
        System.out.println(lang);
        System.out.println(option);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


