package abt.androidblacktiger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rmtheis.yandtran.language.Language;

/**
 * Created by Maria on 14/10/2015.
 */
public class WelcomeActivity extends Activity implements AdapterView.OnItemSelectedListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Spinner spinner = (Spinner) findViewById(R.id.language_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
            intent = new Intent(this, MainMenuActivity.class);
        }
        if(intent != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences langPrefs = getSharedPreferences(Translator.preferencesLabel, 0);
        String option =  parent.getItemAtPosition(position).toString();
        if(option == "Irish"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.IRISH.toString());
        }
        else if(option == "French"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.FRENCH.toString());
        }
        else if(option == "Dutch"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.DUTCH.toString());
        }
        else if(option == "Italian"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.ITALIAN.toString());
        }
        else if(option == "Portuguese"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.PORTUGUESE.toString());
        }
        else if(option == "Spanish"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.SPANISH.toString());
        }
        else if(option == "German"){
            langPrefs.edit().putString(Translator.destinationLanguage, Language.GERMAN.toString());
        }
        System.out.println(option);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


