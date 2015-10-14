package abt.androidblacktiger;

/**
 * Created by Ciarán on 14/10/2015.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button button_word_history = (Button) findViewById(R.id.word_history_button);
        Button button_dictionary = (Button) findViewById(R.id.dictionary_button);
        Button button_map_discovery = (Button) findViewById(R.id.map_discovery_button);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OpenNewActivity(View view)
    {
        //Intent intent = null;
        if(view.getId()==R.id.word_history_button)
        {
            //intent = new Intent(this, HistoryActivity.class);
        }
        else if (view.getId()==R.id.dictionary_button)
        {
            //Replace MainMenuActivity.class with the name of activity corresponding to the dictionary
            //intent = new Intent(this, MainMenuActivity.class);
        }
        else
        {
            //Replace MainMenuActivity.class with the name of activity corresponding to Map
            //intent = new Intent(this, MainMenuActivity.class);
        }
        // startActivity(intent);
    }
}